package com.life.bank.industrial.notify;

import com.life.bank.industrial.LifePaymentFlow;

/**
 * <pre>
 * 在线提现结果回调管理器
 * 
 * 注意：实现该回调的地方尽量不要在构造方法执行任何操作
 * 且不要使用Spring自动注入的方式来对某些服务对象进行使用；因为该回调在启动服务器未完成时就已进行初始化，此时Spring的容器未初始化完成，所有自动注入将失效
 * 具体可这样使用：
 * // 获取Web应用程序的上下文容器
 * WebApplicationContext applicationContext = InitListener.getApplicationContext();
 * // 其中XXXService表示特定的某个服务接口
 * XXXService xXXService= (XXXService)applicationContext.getBean("xXXService");
 * 
 * 注册入口为：OnlineWithdrawalCallbackManager.getInstance().registerOnlineWithdrawalCallback(String key, OnlineWithdrawalCallback value);
 * 其中key为监听者定义的名字 管理器会检测是否存在重复的监听器 如果存在 那么会中断整个启动
 * value为实现者，请尽量按模块起名
 * </pre>
 * 
 * @author chinahuangxc
 */
public interface OnlineWithdrawalCallback {

	/**
	 * <pre>
	 * 当结果成功时回调该方法，监听者自行决定是否执行后续的操作
	 * </pre>
	 * @param lifePaymentFlow 对应的提现流水
	 */
	public void success(LifePaymentFlow lifePaymentFlow);
	
	public void fail(LifePaymentFlow lifePaymentFlow);
}