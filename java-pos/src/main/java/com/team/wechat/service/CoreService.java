package com.team.wechat.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team.wechat.message.resp.NewsMessage;
import com.team.wechat.util.MessageUtil;

/*******************************************************************************
 * 核心服务类
 * 
 * @time 2013-10-11 下午5:31:15
 * @author WEIZHANG_CHEN
 */
public class CoreService {
	// private KeywordServiceI keywordService;
	private static Logger log = LoggerFactory.getLogger(CoreService.class);

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		// PropertiesUtil.getBaseUrl();
		// String BASE_URL = null;
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			log.info("fromUserName=" + fromUserName + ";toUserName="
					+ toUserName + ";msgType=" + msgType);

			// 接收用户发送的文本消息内容
			String content = requestMap.get("Content");

			// 创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);

			// List<Article> articleList = new ArrayList<Article>();
			if (StringUtils.isNotBlank(content)) {
				/*
				 * //文本请求 if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
				 * List<KeywordEntity> classfiyList =
				 * keywordService.findByProperty(KeywordEntity.class, "token",
				 * null);
				 *  // 图片请求 } else if
				 * (MessageUtil.REQ_MESSAGE_TYPE_IMAGE.equals(msgType)) {
				 *  }
				 */
//				if ("主页".equals(content)) {
//					String token = request.getParameter("token");
//					HomeEntity homeEntity = homeService.findUniqueByProperty(
//							HomeEntity.class, "token", token);
//					if (homeEntity != null) {
//						List<Article> articleList = new ArrayList<Article>();
//						Article article = new Article();
//						article.setTitle(homeEntity.getTitle());
//						article.setDescription(homeEntity.getInfo());
//						article.setPicUrl(homeEntity.getPicurl());
//						article.setUrl(homeEntity.getHomeurl());
//						articleList.add(article);
//						// 设置图文消息个数
//						newsMessage.setArticleCount(articleList.size());
//						// 设置图文消息包含的图文集合
//						newsMessage.setArticles(articleList);
//						// 将图文消息对象转换成xml字符串
//						respMessage = MessageUtil.newsMessageToXml(newsMessage);
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}
}