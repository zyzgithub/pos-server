package com.team.wechat.message.resp;

/**
 * 音乐消息
 * 
 * @time 2013-10-11 下午5:30:55   @author WEIZHANG_CHEN
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}