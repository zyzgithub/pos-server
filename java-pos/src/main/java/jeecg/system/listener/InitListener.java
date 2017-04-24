package jeecg.system.listener;


import javax.servlet.ServletContextEvent;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.life.commons.LifeInitialManager;

import jeecg.system.service.SystemService;


/**
 * 系统初始化监听器,在系统启动时运行,进行一些初始化工作
 * @author laien
 *
 */
public class InitListener  implements javax.servlet.ServletContextListener {
	
	private static WebApplicationContext webApplicationContext = null;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		SystemService systemService = (SystemService) webApplicationContext.getBean("systemService");
		//对数据字典进行缓存
		systemService.initAllTypeGroups();
		
		LifeInitialManager.getInstance().notifySystemInitialComplate(event);
	}
	
	/**
	 * 必须系统初始化完成后才能使用该方法
	 * @return
	 * @deprecated
	 */
	public static WebApplicationContext getApplicationContext() {
		return webApplicationContext;
	}
}
