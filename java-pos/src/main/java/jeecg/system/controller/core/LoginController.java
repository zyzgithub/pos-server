package jeecg.system.controller.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jeecg.system.pojo.base.TSFunction;
import jeecg.system.pojo.base.TSRole;
import jeecg.system.pojo.base.TSRoleFunction;
import jeecg.system.pojo.base.TSRoleUser;
import jeecg.system.pojo.base.TSUser;
import jeecg.system.service.SystemService;
import jeecg.system.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.common.SessionInfo;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.extend.datasource.DataSourceContextHolder;
import org.jeecgframework.core.extend.datasource.DataSourceType;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ListtoMenu;
import org.jeecgframework.core.util.NumberComparator;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * 登陆初始化控制器-接口管理后台
 */
@Controller
@RequestMapping("/loginController")
public class LoginController {
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserService userService;
	
	private String message = null;
	
	@Value("${session.interval}") 
	private String sessionTimeout;

	
	/**
	 * 进度接口管理登录页
	 * @author lfq
	 * @return
	 */
	@RequestMapping(params = "index")
	public String index() {
		return "login/login";
	}
	
	/**
	 * 检查用户名称
	 * @param user
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(TSUser user, HttpServletRequest req) {
		HttpSession session = ContextHolderUtils.getSession();
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
		AjaxJson j = new AjaxJson();
		TSUser u = userService.checkUserExits(user);
		if (u != null) {
			if (true) {
				message = "用户: " + user.getUserName() + "登录成功";
				SessionInfo sessionInfo = new SessionInfo();
				sessionInfo.setUser(u);
				String strSessionTimeout = sessionTimeout;
				if(StringUtils.isEmpty(sessionTimeout)){
					strSessionTimeout = "3600000";
				}
				Long sessionTimeout = Long.parseLong(strSessionTimeout);// 单位：ms
				session.setMaxInactiveInterval(sessionTimeout.intValue()/1000); // 单位：s
				session.setAttribute(Globals.USER_SESSION, sessionInfo);
				// 添加登陆日志
				systemService.addLog(message, Globals.Log_Type_LOGIN, Globals.Log_Leavel_INFO);
			} else {
				j.setMsg("请检查U盾是否正确");
				j.setSuccess(false);
			}
		} else {
			j.setMsg("用户名或密码错误!");
			j.setSuccess(false);
		}
		return j;
	}

	/**
	 * 用户登录
	 * @param user
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "login")
	public String login(HttpServletRequest request) {
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
		TSUser user = ResourceUtil.getSessionUserName();
		String roles = "";
		if (user != null) {
			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				roles += role.getRoleName() + ",";
			}
			request.setAttribute("roleName", roles);
			request.setAttribute("userName", user.getRealName());
			return "main/main";
		} else {
			return "login/login";
		}

	}

	/**
	 * 退出系统
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView modelAndView = null;

		HttpSession session = ContextHolderUtils.getSession();
		String versionCode = oConvertUtils.getString(request.getParameter("versionCode"));
		TSUser user= ResourceUtil.getSessionUserName();
		List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			session.removeAttribute(role.getId());
		}
		
		// 判断用户是否为空不为空则清空session中的用户object
		session.removeAttribute(Globals.USER_SESSION);// 注销该操作用户
		systemService.addLog("用户" + user.getUserName() + "已退出", Globals.Log_Type_EXIT, Globals.Log_Leavel_INFO);
		modelAndView = new ModelAndView(new RedirectView("loginController.do?login"));

		return modelAndView;
	}
	
	/**
	 * 菜单跳转
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		String roles = "";
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if(user==null || user.getId()==null){
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(new RedirectView("loginController.do?login"));
		}
		Set<TSFunction> loginActionlist = new HashSet<TSFunction>();// 已有权限菜单
		List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			roles += role.getRoleName() + ",";
			List<TSRoleFunction> roleFunctionList = ResourceUtil.getSessionTSRoleFunction(role.getId());
			if (roleFunctionList == null) {
				session.setMaxInactiveInterval(60 * 30);
				roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
				session.setAttribute(role.getId(), roleFunctionList);
			}else{
				if(roleFunctionList.get(0).getId()==null){
					roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
				}
			}
			for (TSRoleFunction roleFunction : roleFunctionList) {
				TSFunction function = roleFunction.getTSFunction();
				loginActionlist.add(function);
			}
		}
		
		List<TSFunction> bigActionlist = new ArrayList<TSFunction>();// 一级权限菜单
		List<TSFunction> smailActionlist = new ArrayList<TSFunction>();// 二级权限菜单
		if (loginActionlist.size() > 0) {
			for (TSFunction function : loginActionlist) {
				if (function.getFunctionLevel() == 0) {
					bigActionlist.add(function);
				} else if (function.getFunctionLevel() == 1) {
					smailActionlist.add(function);
				}
			}
		}
		// 菜单栏排序
		Collections.sort(bigActionlist, new NumberComparator());
		Collections.sort(smailActionlist, new NumberComparator());
		String logString = ListtoMenu.getEasyuiMenu(bigActionlist, smailActionlist);
		request.setAttribute("loginMenu", logString);
		request.setAttribute("parentFun", bigActionlist);
		request.setAttribute("roleName", roles);
		request.setAttribute("userName", user.getRealName());
		request.setAttribute("childFun", smailActionlist);
		request.setAttribute("userName", user.getRealName());

		return new ModelAndView("main/left");
	}
	/**
	 * 首页跳转
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		request.setAttribute("user", user);
			return new ModelAndView("main/home");
	}

	/**
	 * 无权限页面提示跳转
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
			return new ModelAndView("common/noAuth");
	}
}
