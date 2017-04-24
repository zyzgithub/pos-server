package com.wm.controller.tvlogin;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.twodimensionalcode.util.MatrixToImageWriter;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.tvlogin.TvLoginEntity;
import com.wm.service.tvlogin.TvLoginServiceI;
import com.wm.util.JSONUtil;
import com.wm.util.StringUtil;

/**   
 * @Title: Controller
 * @Description: TV二维码登录控制器
 * @author lfq
 * @date 2015-04-28 15:04:07
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/tvLoginController")
public class TvLoginController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TvLoginController.class);

	@Autowired
	private TvLoginServiceI tvLoginService;
	@Autowired
	private SystemService systemService;

	/**
	 * TV请求生成二维码记录
	 * @author lfq
	 */
	@RequestMapping(params="generateCode")
	@ResponseBody
	public AjaxJson generateCode(HttpServletRequest request,HttpServletResponse response){
		AjaxJson result=new AjaxJson();
		try {
			String port="";
			if (request.getServerPort()!=80) {
				port=":"+request.getServerPort();
			}
			String uuid=UUID.randomUUID().toString();
			String root="http://" + request.getServerName()+port+request.getContextPath();
			String content = "";//二维码内容
			content = root+"/ci/tvLoginController.do?login&id="+uuid;//二维码内容
			TvLoginEntity tvLoginEntity=new TvLoginEntity();
			tvLoginEntity.setId(uuid);
			tvLoginEntity.setCreateTime(new Date());
			tvLoginEntity.setStatus("0");
			tvLoginEntity.setContent(content);
			tvLoginService.save(tvLoginEntity);
			result.setSuccess(true);
			result.setStateCode("00");
			result.setObj(tvLoginEntity.getId());
			result.setMsg("已生成二维码记录");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TV登录二维码记录新增异常");
			result.setSuccess(false);
			result.setStateCode("01");
			result.setMsg("生成TV登录二维码记录失败");
		}
		return result;
	}
	
	/**
	 * 生成TV登录的二维码图片
	 * http://XXXX/ci/tvLoginController.do?code&id=xxxx   获取二维码地址
	 * @author lfq
	 * @param id		TV生成的登录二维码记录id
	 */
	@RequestMapping(params="code")
	public void code(HttpServletRequest request,HttpServletResponse response,String id){
		if (StringUtil.isEmpty(id)) {
			return;
		}
		TvLoginEntity tvLoginEntity=tvLoginService.get(TvLoginEntity.class, id);
		if (tvLoginEntity==null) {
			return;
		}
		int width=200;		//二维码宽度
		int height=200;		//二维码高度
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		String content = tvLoginEntity.getContent();		
		
	    try { 
	    	Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
	    	hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height,hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "jpg", response.getOutputStream());
		} catch (Exception e) {
			logger.error("生成二维码图片异常");
		}
	}
	
	/**
	 * 商家扫一扫,确认登录
	 * @author lfq
	 * @param content  商家扫出的二维码内容(例如：http://XXXXXX/ci/tvLoginController.do?login&id=xxxxx)
	 * @param versionType  确认登录的客户端版本类型
	 * @param merchantId 商家id
	 * @return  操作结果,json格式
	 */
	@RequestMapping(params="loginCofirm")
	@ResponseBody
	public AjaxJson loginCofirm(HttpServletRequest request,HttpServletResponse response,String content,String versionType,Integer merchantId){
		AjaxJson result=new AjaxJson();
		result.setSuccess(false);
		result.setStateCode("01");
		result.setMsg("确认TV登录失败");
		
		try {
			String prefix="/ci/tvLoginController.do?login&id=";
			String id=null;//TV登录二维码id
			if (!StringUtil.isEmpty(content) && content.contains(prefix) && !StringUtil.isEmpty(versionType)) {
				id=content.substring(content.indexOf(prefix)+prefix.length(), content.length());
				MerchantEntity merchantEntity=systemService.get(MerchantEntity.class, merchantId);
				if (merchantEntity!=null ) {
					TvLoginEntity tvLoginEntity=tvLoginService.get(TvLoginEntity.class, id);
					if (tvLoginEntity!=null && tvLoginEntity.getMerchantId()==null 
							&& "0".equals(tvLoginEntity.getStatus())
							&& content.equals(tvLoginEntity.getContent())) {
						tvLoginEntity.setMerchantId(merchantId);
						tvLoginEntity.setStatus("1");
						tvLoginEntity.setVersionType(versionType);
						tvLoginService.updateEntitie(tvLoginEntity);
						result.setSuccess(true);
						result.setStateCode("00");
						result.setObj(tvLoginEntity.getId());
						result.setMsg("确认登录成功");
					}else{
						result.setMsg("二维码已过期");
					}
				}else{
					result.setMsg("店铺信息不存在");
				}
			}else{
				result.setMsg("参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商家确认TV登录失败");
		}
		return result;
	}
	
	/**
	 * 非外卖商家版扫一扫跳转界面
	 * xxxx/tvLoginController/login.do?id=xxxxx
	 * @author lfq
	 * @return
	 */
	@RequestMapping(params="login")
	public String loginMsg(ModelMap modelMap){
		modelMap.put("msg", "抱歉!你的应用不支持该二维码，请使用1号外卖商家版app扫一扫");
		return "takeout/error";
	}
	/**
	 * 非外卖商家版扫一扫跳转界面
	 * xxxx/tvLoginController/login.do?id=xxxxx
	 * @author lfq
	 * @return
	 
	@RequestMapping(params="login")
	public String loginMsgs(ModelMap modelMap){
		modelMap.put("msg", "抱歉!你的应用不支持该二维码，请使用1号外卖商家版app扫一扫");
		return "takeout/error";
	}
	*/
	
	/**
	 * 获取二维码当前状态
	 * 如果二维码为商家已确认状态则返回商家信息，反正返回失败信息
	 * @author lfq
	 * @param id	tv登录二维码记录id
	 * @return 
	 * 状态码：
	 * success : true 验证通过，false验证失败
	 * stateCode: 00 商家已确认;01获取TV登录二维码状态失败;02商家未确认;03二维码已使用过，请刷新二维码;04商家不存在;500程序异常
	 * versionType :客户端版本类型： 01 前台； 02 菜单 ；03 厨房电视；04 配餐PAD ； 05 厨房制作完成； 06 外卖电视； 07 外卖Pad； 08 排号
	 */
	@RequestMapping(params="getTvLogin")
	public void getTvLogin(HttpServletRequest request,HttpServletResponse response,String id){
		AjaxJson result=new AjaxJson();
		result.setSuccess(false);
		result.setStateCode("01");
		result.setMsg("");
		try {
			if (!StringUtil.isEmpty(id)) {
				TvLoginEntity tvLoginEntity=tvLoginService.get(TvLoginEntity.class, id);
				if (tvLoginEntity!=null ) {
					String status=tvLoginEntity.getStatus().toString();
					String versionType= tvLoginEntity.getVersionType();
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("versionType",""+versionType);
					
					if ("0".equals(status) || tvLoginEntity.getMerchantId()==null) {
						result.setMsg("商家未确认");
						result.setStateCode("02");
					}else if ("2".equals(status)) {
						result.setMsg("二维码已使用过，请刷新二维码");
						result.setStateCode("03");
					}else{
						MerchantEntity merchantEntity=this.systemService.get(MerchantEntity.class, tvLoginEntity.getMerchantId());
						if (merchantEntity!=null) {
							tvLoginEntity.setStatus("2");
							tvLoginService.updateEntitie(tvLoginEntity);
							result.setObj(merchantEntity);//设置返回的登录信息]
							result.setAttributes(map);
							result.setSuccess(true);
							result.setStateCode("00");
						}else{
							result.setMsg("商家不存在");
							result.setStateCode("04");
						}
					}
				}else{
					result.setMsg("不存在该登录的二维码信息");
				}
			}else{
				result.setMsg("参数错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取TV登录二维码状态异常");
			result.setMsg("程序异常");
			result.setStateCode("500");
		}
		JSONUtil.printToHTML(response, result);
	}
}
