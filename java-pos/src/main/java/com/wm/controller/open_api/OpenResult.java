package com.wm.controller.open_api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 开放接口返回数据结果
 * @author folo
 *
 */
public class OpenResult {
	private int state = 0;
	private String msg = "失败";
	private Map<String, Object> data;
	
	public static OpenResult ERR(){
		return new OpenResult();
	}
	
	public static OpenResult OK(){
		return new OpenResult().state(State.Success).msg("成功");
	}
	
	public OpenResult state(State state) {
		this.state = state.code;
		return this;
	}
	
	public OpenResult state(int code) {
		this.state = code;
		return this;
	}
	
	public OpenResult msg(String msg) {
		this.msg = msg;
		return this;
	}
	
	public OpenResult put(String name, Object value){
		if(null == data) data = new HashMap<String, Object>();
		data.put(name, value);
		return this;
	}
	
	public OpenResult data(Map<String, Object> obj){
		this.data = obj;
		return this;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}



	/**
	 * <h1>接口状态<h1><p>success=1 成功</p><p>success=0 失败</p>
	 * 1xx 成功
	 * 2xx 失败
	 * 3xx 请求错误
	 * 4xx
	 * 5xx
	 * 6xx
	 * 7xx 已存在
	 * 8xx 未授权，未登录，未找到等
	 * 9xx 系统错误
	 * @author folo
	 *
	 */
	public static enum State{
		Success(100, "成功"),
		
		Error(200, "失败"),
		
		ParamError(300, "请求参数有误"),
		SignError(302, "签名错误"),
		TokenError(305, "token错误"),
		
		RemoteServerError(401, "远程服务器异常"),
		
		NoLogin(600, "未登录"),
		
		AlreadyFound(700, "已存在"),
		AlreadyUser(710, "用户已存在"),
		
		NotFound(801, "未找到"),
		NotUser(810, "用户未找到"),
		
		SysError(900, "系统错误");
		private int code;
		private String msg;
		State(int code, String msg){
			this.code = code;
			this.msg = msg;
		}
		
		public boolean equal(Integer code){
			if(null == code) return false;
			return this.code == code ? true : false;
		}
		
		public OpenResult ret(){
			return OpenResult.ERR().state(code).msg(msg);
		}
		
		public OpenResult ret(String msg){
			return OpenResult.ERR().state(code).msg(msg);
		}
	}
}
