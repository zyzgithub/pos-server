package com.wm.controller.courier;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * 项目名称：WM 类名称：TVReportWSController 类描述： the class is report current couriers
 * location. 创建人：fly 创建时间：2016年2月19日 上午10:09:20 修改人：fly 修改时间：2016年2月19日
 * 上午10:09:20 修改备注：
 * 
 * @version
 *
 */
@ServerEndpoint("/tv/websocket/{clientId}")
public class TVReportWSController {
	private static Logger LOG = Logger.getLogger(TVReportWSController.class);

	@OnOpen
	public void onOpen(@PathParam("clientId") String clientId, Session session)
			throws InterruptedException {
		LOG.info("Websocket Start Connecting:" + clientId);
		SessionUtils.put(clientId, session);
		LOG.info("current socket client count is : "
				+ SessionUtils.clients.size());
	} 
	
	 @OnMessage
	  public void onMessage(@PathParam("clientId") String clientId, String message) {
		 
	 }
	   
	 @OnError
	  public void onError(@PathParam("clientId") String clientId, Throwable throwable, Session session) {
		  LOG.info("Websocket Connection Exception:"+ clientId);
		  LOG.info(throwable.getMessage(), throwable);
		  SessionUtils.remove(clientId);
	 }
	   
	 @OnClose
	  public void onClose(@PathParam("clientId") String clientId,Session session) {
		 LOG.info("Websocket Close Connection:"+ clientId);
		 SessionUtils.remove(clientId);
	 }
}
