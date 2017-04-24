package com.team.wechat.message.resp;
/**
 * 图片消息
 * 
 * @time 2013-10-29 上午9:47:04   @author WEIZHANG_CHEN
 */
public class ImageMessage extends BaseMessage {  
    // 图片链接  
    private String PicUrl;  
  
    public String getPicUrl() {  
        return PicUrl;  
    }  
  
    public void setPicUrl(String picUrl) {  
        PicUrl = picUrl;  
    }  
}  
