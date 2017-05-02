package com.dianba.pos.common.util;

/**
 * 携带成功状态和传递消息的对象
 */
public class MsgResp {

	/**
	 * 成功状态
	 */
	private boolean success;

	/**
	 * 传递的消息
	 */
	private String msg;

	public MsgResp() {

	}

	public MsgResp(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}
	
	public static MsgResp successResp() {
		return new MsgResp(true, null);
	}

	public static MsgResp successResp(String msg) {
		return new MsgResp(true, msg);
	}

	public static MsgResp failResp(String msg) {
		return new MsgResp(false, msg);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
