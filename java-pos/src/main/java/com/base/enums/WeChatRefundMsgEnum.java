package com.base.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信退款异常信息枚举
 * @author 黄聪
 *
 * @deprecated 微信支付APP支付开发者文档也没有详细描述完整的错误代码，实际测试中发现有其他的错误代码如ERROR。使用此枚举需要繁复的空检查，建议用完成的响应信息日志代替本枚举类。
 */
@Deprecated
public enum WeChatRefundMsgEnum {

	SYSTEMERROR( 0, "SYSTEMERROR","接口返回错误","系统超时","请用相同参数再次调用API" ),

	INVALID_TRANSACTIONID( 1, "INVALID_TRANSACTIONID","无效transaction_id","请求参数未按指引进行填写","请求参数错误，检查原交易号是否存在或发起支付交易接口返回失败" ),

	PARAM_ERROR( 2, "PARAM_ERROR","参数错误","请求参数未按指引进行填写","请求参数错误，请重新检查再调用退款申请" ),
	
	APPID_NOT_EXIST( 3, "APPID_NOT_EXIST","APPID不存在","参数中缺少APPID","请检查APPID是否正确" ),

	MCHID_NOT_EXIST( 4, "MCHID_NOT_EXIST","MCHID不存在","参数中缺少MCHID","请检查MCHID是否正确" ),

	APPID_MCHID_NOT_MATCH( 5, "APPID_MCHID_NOT_MATCH","appid和mch_id不匹配","appid和mch_id不匹配","请确认appid和mch_id是否匹配" ),

	REQUIRE_POST_METHOD( 6, "REQUIRE_POST_METHOD","请使用post方法","未使用post传递参数 ","请检查请求参数是否通过post方法提交" ),

	SIGNERROR( 7, "SIGNERROR","签名错误","参数签名结果不正确","请检查签名参数和方法是否都符合签名算法要求" ),
	
	XML_FORMAT_ERROR( 8, "XML_FORMAT_ERROR","XML格式错误","XML格式错误","请检查XML参数格式是否正确" );

	private int id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 原因
	 */
	private String reason;
	/**
	 * 解决方案
	 */
	private String solution;
	
	private static Map<Integer, String> map = new HashMap<Integer, String>();

	private static List<WeChatRefundMsgEnum> list = new ArrayList<WeChatRefundMsgEnum>();

	static {
		for( WeChatRefundMsgEnum obj : WeChatRefundMsgEnum.values() ) {
			map.put( obj.getId(), obj.getName() );
		}
		list.addAll( Arrays.asList( WeChatRefundMsgEnum.values() ) );
	}

	public int getId() {
		return id;
	}

	public void setId( int id ) {
		this.id = id;
	}
	/**
	 * 名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 名称
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 原因
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * 原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 解决方案
	 */
	public String getSolution() {
		return solution;
	}

	/**
	 * 解决方案
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}

	private WeChatRefundMsgEnum(int id, String name,String description,String reason,String solution) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.reason = reason;
		this.solution = solution;
	}

	/**
	 * 返回map类型形式
	 * 
	 * @return
	 */
	public static Map<Integer, String> getMap() {
		return map;
	}

	/**
	 * 返回list类型形式
	 * 
	 * @return
	 */
	public static List<WeChatRefundMsgEnum> getList() {
		return list;
	}

	/**
	 * 根据id获取枚举类型
	 * 
	 * @param id
	 * @return
	 */
	public static WeChatRefundMsgEnum getCategory( int id ) {
		for( WeChatRefundMsgEnum status : list ) {
			if( status.getId() == id ) {
				return status;
			}
		}
		return null;
	}
	
	/**
	 * 根据name获取枚举类型
	 * 
	 * @param name
	 * @return
	 */
	public static WeChatRefundMsgEnum getCategoryByName( String name ) {
		for( WeChatRefundMsgEnum status : list ) {
			if( status.getName().equals(name) ) {
				return status;
			}
		}
		return null;
	}
}
