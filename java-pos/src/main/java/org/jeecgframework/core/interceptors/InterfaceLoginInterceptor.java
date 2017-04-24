package org.jeecgframework.core.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jeecg.system.service.SystemService;
import net.sf.json.JSONObject;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class InterfaceLoginInterceptor implements HandlerInterceptor{
	@Autowired
	private SystemService systemService;

	private List<String> excludeUrls;
	
	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object arg2) throws Exception {
		// TODO Auto-generated method stub
		ServletContext application = request.getSession().getServletContext();
		String requestPath = ResourceUtil.getRequestPath(request);// 用户访问的资源地址
		
		if(excludeUrls.contains(requestPath)) {
			return true;
		}
		
		//是否注册操作
		String register = request.getParameter("register");
		if(register != null && register.equals("true")){
			return true;
		}
		
		String sessionkey = request.getParameter("sessionkey");
		if(StringUtil.isNotEmpty(sessionkey)){
			List list = systemService.findForJdbc("select SESSION_KEY from c_session where SESSION_KEY = ? and END_TIME > ?", sessionkey.trim(),new Date().getTime());
			if(list != null && list.size() > 0){
				return true;
			}else{
				printInfo(request,response,"登录超时，请重新登录","overtime");
				return false;
			}
		}
		printInfo(request,response,"请登录","Not logged in");
		return false;
	}
	
	private void printInfo(HttpServletRequest request, HttpServletResponse response,String msg,String state) throws ServletException, IOException {
		response.setContentType("text/html; charset=GBK");
		PrintWriter pw = response.getWriter();
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		j.setMsg(msg);
		j.setState(state);
		j.setStateCode("10001");
		JSONObject json = JSONObject.fromObject(j);
		pw.print(json);
	}
}
