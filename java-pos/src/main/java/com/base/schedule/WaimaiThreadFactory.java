package com.base.schedule;

import java.util.concurrent.ThreadFactory;

public class WaimaiThreadFactory implements ThreadFactory {
	
	private String factoryName;
	
	public WaimaiThreadFactory(String factoryName) {
		this.factoryName = factoryName;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);  
        t.setName(WaimaiThreadFactory.class.getSimpleName() + factoryName);  
        return t; 
	}

}
