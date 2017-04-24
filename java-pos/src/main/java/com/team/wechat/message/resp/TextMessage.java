package com.team.wechat.message.resp;

/**
 * 文本消息
 * 
 * @time 2013-10-11 下午5:31:08   @author WEIZHANG_CHEN
 */
public class TextMessage extends BaseMessage {
	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}