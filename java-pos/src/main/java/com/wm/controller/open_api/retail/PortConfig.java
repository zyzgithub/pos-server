package com.wm.controller.open_api.retail;

/**
 * 一号生活的 Retail转换中间件接口配置管理
 * @author Roar
 *
 */
public class PortConfig { 
	/** 连接一号生活的 Retail转换中间件的接口地址 */
	public static String URL = "http://112.124.49.40:8082";
	
	/** 是否开启调试 true=开启,false=关闭 */
	public static final boolean IS_DEBUG = true;
	
	static{
		if(IS_DEBUG){//调试配置  测试地址
		    URL = "http://112.124.49.40:8082";
		}
	}
}
