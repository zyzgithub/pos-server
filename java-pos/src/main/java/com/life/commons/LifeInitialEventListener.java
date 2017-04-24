package com.life.commons;

import javax.servlet.ServletContextEvent;

public interface LifeInitialEventListener {

	public void notifySystemInitialComplate(ServletContextEvent event);
}
