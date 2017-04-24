package com.life.commons;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import com.life.bank.industrial.WithdrawalInitialEventListenerImpl;

public class LifeInitialManager {

	private static final LifeInitialManager instance = new LifeInitialManager();

	private static final List<LifeInitialEventListener> LIFE_INITIAL_EVENT_LISTENERS = new ArrayList<LifeInitialEventListener>();
	static {
		LIFE_INITIAL_EVENT_LISTENERS.add(new WithdrawalInitialEventListenerImpl());
	}

	public static LifeInitialManager getInstance() {
		return instance;
	}

	private LifeInitialManager() {
	}

	public void notifySystemInitialComplate(ServletContextEvent event) {
		for (LifeInitialEventListener lifeInitialEventListener : LIFE_INITIAL_EVENT_LISTENERS) {
			lifeInitialEventListener.notifySystemInitialComplate(event);
		}
	}
}