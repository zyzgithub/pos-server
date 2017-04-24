package com.base.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.VO.UploadFileVO;
import com.base.config.EnvConfig;
import com.base.exception.RollbackException;
import com.base.util.LoginBase;
import com.base.util.SessionKeyGenerator;
import com.execontent.entity.ExeContent;
import com.session.SessionContext;
import com.session.SessionVO;
import com.testuser.entity.TestUser;
 
/**
 * @Description: 通用接口
 */
@Controller
@RequestMapping("controller")
public class BaseInterfaceController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseInterfaceController.class);

	@Autowired
	private SystemService systemService;
	
//	@Value("${session.interval}") 
//	public static String sessionTimeout;

	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(params = "execute")
	@ResponseBody
	public AjaxJson execute(String ids, HttpServletRequest request, String params) {
		AjaxJson j = new AjaxJson();
		List<UploadFileVO> files = null;
		String typeId = null;

		String sessionkey = request.getParameter("sessionkey");
		String register = request.getParameter("register");

		// 是否是注册
		boolean isRegister = false;
		if (register != null && register.equals("true")) {
			isRegister = true;
		}

		if (StringUtils.isEmpty(sessionkey) && !isRegister) {
			logger.warn("未授权的接口访问！sessionkey=" + sessionkey + ", isRegister=" + isRegister + ", ids=" + ids + ", params=" + params);
			j.setMsg("请先登录");
			j.setState("Not logged in");
			j.setSuccess(false);
		} else {
			if ((StringUtils.isNotEmpty(sessionkey) && SessionContext.getSessionVO(sessionkey) != null) || isRegister) {
				try {
					String filesString = request.getParameter("files");
					if (StringUtil.isNotEmpty(filesString)) {
						JSONArray jsons = JSONArray.fromObject(filesString);
						files = jsons.toList(jsons, UploadFileVO.class);
						typeId = request.getParameter("typeId");
					}
					SessionVO sessionVo = SessionContext.getSessionVO(sessionkey);
					j = systemService.execute(ids, request, params, files, typeId, sessionVo);
				} catch (RollbackException e) {
					logger.error("systemService.execute failed, ids:{}, params:{}, sessionkey:{}", ids, params, sessionkey);
					return e.getAjaxJson();
				}
			} else {
				logger.warn("sessionkey超时！sessionkey=" + sessionkey + ", isRegister=" + isRegister + ", ids=" + ids + ", params=" + params);
				j.setMsg("登录超时,请重新登录");
				j.setState("overtime");
				j.setSuccess(false);
			}
		}
		return j;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "login")
	@ResponseBody
	public AjaxJson login(String id, String params, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		j.setType("login");

		LoginBase lb = new LoginBase();
		JSONObject paramJsons = new JSONObject();
		String sql = null;
		SessionVO session = new SessionVO();
		TestUser tu = new TestUser(); // 测试用户

		try {
			ExeContent exe = systemService.findUniqueByProperty(ExeContent.class, "id", id);
			if (exe != null) {
				sql = exe.getSqlStatement().trim();

				paramJsons = systemService.getParams(params, request);
				Iterator it = paramJsons.keys();
				while (it.hasNext()) {
					String key = String.valueOf(it.next());
					String value = paramJsons.getString(key);

					if (key.equals("login_name")) {
						lb.setUsername(value);
					} else {
						lb.setPassword(value);
					}

					if (value.indexOf(":debug") != -1) {
						value = value.substring(0, value.indexOf(":debug"));
					}
					sql = sql.replace(":" + key, "'" + value + "'");
				}

				if (lb.check()) {
					boolean isDebug = lb.isDebug();

					List data = systemService.findForJdbc(sql);
					if (data != null && data.size() > 0) {
						j.setSuccess(true);
						j.setMsg("登录成功");
						j.setObj(data);

						JSONObject json = (JSONObject) JSONArray.fromObject(data).get(0);

						// 用户表的主键
						String primarykey = null;
						try {
							primarykey = (String) json.get("ID");
							if (primarykey == null || primarykey.length() < 1) {
								primarykey = (String) json.get("id");
							}
						} catch (Exception ex) {
							primarykey = (Integer) json.get("ID") + "";
							if (primarykey == null || primarykey.length() < 1) {
								primarykey = (Integer) json.get("id") + "";
							}
						}
						
						// 添加会话
						session.setCreateTime(new Date());
						String sessionKey = SessionKeyGenerator.getSessionKey();
						logger.debug("sessionKey:{}", sessionKey);
						session.setSessionKey(sessionKey);
						
						long startTime = System.currentTimeMillis();

						// 从配置文件中获取超时时间
						String sessionTimeout = EnvConfig.base.sessionTimeout;
						if(StringUtils.isEmpty(sessionTimeout)){
							sessionTimeout = "1800000";
						}
						logger.debug("sessionTimeout : {}", sessionTimeout);
						long endTime = startTime + Long.parseLong(sessionTimeout);
						session.setStartTime(new Long(startTime).toString());
						session.setEndTime(new Long(endTime).toString());

						if (primarykey == null || primarykey.length() < 1) {
							session.setUserId(lb.getUsername());
						} else {
							session.setUserId(primarykey);
						}
						session.setUserInfo(data.toString());

						if (isDebug) {
							session.setIsDebug(1);
						}
						systemService.save(session);

						sessionKey = session.getSessionKey();
						logger.debug("sessionKey after:{}", sessionKey);
						// 把session放到hashMap中
						SessionContext.putSessionVO(sessionKey, session);

						j.setSessionkey(sessionKey);

						// 记录进行debug模式的用户
						if (session.getIsDebug() == 1) {
							tu.setUserId(lb.getUsername());
							tu.setCreateTime(session.getCreateTime());
							tu.setTimeOut(new Date(Long.parseLong(session.getEndTime())));
							systemService.save(tu);
						}

						// 记录操作日志
						String p = new String();
						if (params != null && params.length() > 0) {
							p = params.replaceAll("\\'", "\\\\'");
						} else {
							p = paramJsons.toString();
						}
						systemService.saveOperateLog(id, p, j, lb.getUsername(), "login");
					} else {
						j.setSuccess(false);
						j.setMsg("登录失败,账号或密码错误");
					}
				} else {
					j.setSuccess(false);
					j.setMsg("登录失败,账号(login_name)或密码不能为空");
				}
				j.setSql(sql);
			} else {
				j.setMsg("接口ID不存在");
			}
		} catch (RollbackException e) {
			j.setMsg("登录失败,请检查sql语句");
			j.setSuccess(false);
			j.setSql(sql);
			j.setSessionkey(session.getSessionKey());
			e.setAjaxJson(j);
			return e.getAjaxJson();
		} catch (Exception ex) {
			logger.error(ex.toString());
			j.setMsg("程序错误");
			j.setSuccess(false);
		}
		return j;
	}
}
