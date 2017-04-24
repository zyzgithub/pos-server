package com.wm.service.impl.merchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.dto.merchant.MerchantOpenStoreDto;
import com.wm.dto.org.ProvinceAndCityDto;
import com.wm.entity.category.CategoryEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantDeductionEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.user.UserloginEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.merchant.MerchantOpenStoreServiceI;
import com.wm.service.org.OrgServiceI;

@Service("merchantOpenStoreService")
@Transactional
public class MerchantOpenStoreServiceImpl extends CommonServiceImpl implements MerchantOpenStoreServiceI{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private OrgServiceI orgService;
	
	@Override
	public AjaxJson ifExistPhone(String phone){
		AjaxJson json = new AjaxJson(true, "该手机号可以注册", "00");
		
		if(StringUtils.isBlank(phone)){
			return new AjaxJson(false, "参数缺失", "01");
		}
		
		String sql = " select id from user where (username = ? or mobile = ?) and user_type = 'merchant' and is_delete = 1 "; 
		List<Map<String, Object>> userMapList = this.findForJdbc(sql, phone, phone);
		if(userMapList!=null && userMapList.size()>0){
			json = new AjaxJson(false, "该号码暂不能注册，有疑问请联系客服", "02");
			return json;
		}
		
		sql = " select id from user where (username = ? or mobile = ?) and user_type = 'merchant' and is_delete = 0 ";
		userMapList = this.findForJdbc(sql, phone, phone);
		if(userMapList!=null && userMapList.size()>0){
			json = new AjaxJson(false, "手机号已注册，请直接登录", "01");
			return json;
		}
		logger.info("phone={}的用户是否可以注册:", json.isSuccess());
		return json;
	}
	
	@Override
	public AjaxJson confirmRegister(MerchantOpenStoreDto merchantOpenStoreDto){
		AjaxJson json = new AjaxJson(true, "注册成功", "00");
		logger.info("《商家自助开店注册接口》, phone={}", merchantOpenStoreDto.getPhone());
		
		if(merchantOpenStoreDto==null || StringUtils.isBlank(merchantOpenStoreDto.getPhone())
			|| StringUtils.isBlank(merchantOpenStoreDto.getMerchantName()) || StringUtils.isBlank(merchantOpenStoreDto.getPassword())){
			return new AjaxJson(false, "参数缺失", "01");
		}
		
		/*
		 * 验证商家店铺名称不能重复
		 */
		String sql = "SELECT COUNT(1) FROM merchant WHERE title=?";
		Integer count = this.findOneForJdbc(sql, Integer.class, merchantOpenStoreDto.getMerchantName());
		if(count!=0){
			return new AjaxJson(false, "店铺名称不能重复哦!", "01");
		}
		
		//添加商家用户 user
		WUserEntity wuser = new WUserEntity();
		wuser.setUsername(merchantOpenStoreDto.getPhone());
		wuser.setPassword(merchantOpenStoreDto.getPassword());
		wuser.setNickname("");
		wuser.setUserType("merchant");
		wuser.setMobile(merchantOpenStoreDto.getPhone());
		wuser.setUserState(1); //有效
		wuser.setIsDelete(0);
		wuser.setCreateTime(DateUtils.getSeconds());
		this.save(wuser);
		Integer userId = wuser.getId();
		wuser.setCreator(userId);
		this.updateEntitie(wuser);
		
		//添加商家 merchant
		MerchantEntity merchant = new MerchantEntity();
		merchant.setTitle(merchantOpenStoreDto.getMerchantName());
		merchant.setMobile("");
		merchant.setDisplay("N"); //关店
		merchant.setCostDelivery(0.0);
		merchant.setLng(0.0);
		merchant.setLat(0.0);
		merchant.setCityId(0);
		merchant.setNotice("公告");
		merchant.setPrintCode(""); //打印机编号
		merchant.setCardActivity("N"); //代金券活动标志
		merchant.setBiddingMoney(0.0); //竞价金额
		merchant.setType("饮食"); //店铺类型
		merchant.setDeliveryBegin(0.00); //起送金额
		merchant.setDeduction(0.00); //扣点（外卖）
		merchant.setOrderNum(1); //排号
		merchant.setIncomeDate(1); //预收入到账期
		merchant.setDineOrderPrint("Y"); //是否打印堂食订单
		merchant.setCreateTime(DateUtils.getSeconds());
		merchant.setWuser(wuser);
		merchant.setCategory(new CategoryEntity(543)); //商家类别设置为其它
		this.save(merchant);
		
		//添加商家默认营业时间
		String merchantOpenTimeInfoSql = " insert 0085_merchant_open_time(merchant_id) values (?)";
		this.executeSql(merchantOpenTimeInfoSql, merchant.getId());
		
		//添加 MerchantInfo,来源为默认,未开通外卖服务
		String updateMerchantInfoSql = " insert 0085_merchant_info(merchant_id,merchant_source,apply_waimai_status,invite_code,is_takeout,is_hall_food,is_scan) values (?,?,?,?,?,?,?)";
		this.executeSql(updateMerchantInfoSql, merchant.getId(), 0, 0, merchantOpenStoreDto.getInviteCode(),1,1,1);
		
		//扫码扣点
		MerchantDeductionEntity deduction = new MerchantDeductionEntity();
		deduction.setDeduction(0.006); //码扣点默认 0.6%.
		deduction.setType(2);  //扣点类型，1=门店扣点，2=扫码扣点
		deduction.setMerchantId(merchant.getId());
		deduction.setIncomeDate(1); //预收入兑现天数
		this.save(deduction);

		//添加用户登录记录 user_login
		UserloginEntity userLogin = new UserloginEntity();
		userLogin.setChannel("unknow");
		userLogin.setIp("0.0.0.0");
		userLogin.setLoginTime((int) System.currentTimeMillis() / 1000);
		userLogin.setWuser(wuser);
		this.save(userLogin);
			
		if(userId !=null){
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("userId", userId);
			obj.put("username", merchantOpenStoreDto.getPhone());
			obj.put("nickname", "");
			obj.put("mobile", merchantOpenStoreDto.getPhone());
			obj.put("money", 0);
			obj.put("userType", "merchant");
			obj.put("applyWaimaiStatus", 0); //0未申请开通外卖 1已申请未回访 2已申请回访中 3已开通外卖 4拒绝开通外卖
			
			if(merchant != null){
				obj.put("merchantId", merchant.getId());
				obj.put("merchantName", merchant.getTitle());
				obj.put("merchantSource", 0);  
				obj.put("storeType", "2");  //分店(0)、总店(1)、普通店铺(2)、供应链(3)
				obj.put("printCode", "");
			}
			json.setObj(obj);
		}else{
			logger.info("《商家自助开店注册接口》, 系统异常，注册失败");
			json.setSuccess(false); 
			json.setMsg("系统异常，注册失败");
			json.setStateCode("01");
		}
		return json;
	}

	@Override
	public AjaxJson retrievePassword(String phone, String password) {
		AjaxJson json = ifExistPhone(phone);
		String sql = " select id from user where (username = ? or mobile = ?) and user_type = 'merchant' and is_delete = '0' ";
		List<Map<String, Object>> userMapList = this.findForJdbc(sql, phone, phone);
		logger.info("list={},list.size={}",userMapList, userMapList.size());
		
		if(userMapList==null || userMapList.size() ==0){
			json = new AjaxJson(false, "该手机号未注册商家", "01");
		}else{
			json = new AjaxJson(true, "密码重置成功", "00");
			Map<String, Object> map = userMapList.get(0);
			Integer id = Integer.valueOf(map.get("id").toString());
			WUserEntity user = this.get(WUserEntity.class, id);
			user.setPassword(password);
			this.updateEntitie(user);
		}
		return json;
	}
	
	@Override
	public AjaxJson getOrgList(){
		AjaxJson json = new AjaxJson(true, "操作成功", "00");
		logger.info("《商家开通外卖服务接口》,获取省市...");
		
		List<ProvinceAndCityDto> proDtoList = new ArrayList<ProvinceAndCityDto>();
		List<Map<String, Object>> proMapList = orgService.getOrgIdAndName(2); //所有的省
		for(Map<String, Object> map : proMapList){
			ProvinceAndCityDto provinceAndCityDto = new ProvinceAndCityDto();
			//省
			Integer proId = (Integer) map.get("cityId");
			provinceAndCityDto.setOrgId(proId);
			provinceAndCityDto.setOrgName(String.valueOf(map.get("orgName")));
			//市
			List<Map<String, Object>> citys = orgService.getOrgIdAndNameByPid(proId);
			provinceAndCityDto.setCitys(citys);
			
			proDtoList.add(provinceAndCityDto);
		}
		json.setObj(proDtoList);
		return json;
	}

	@Override
	public AjaxJson openWaimai(MerchantOpenStoreDto merchantOpenStoreDto) {
		AjaxJson json = new AjaxJson(true, "开通成功", "00");
		logger.info("《商家开通外卖服务接口》, mobile={}", merchantOpenStoreDto.getMobile());
		
		if(merchantOpenStoreDto==null || StringUtils.isBlank(merchantOpenStoreDto.getMobile()) || StringUtils.isBlank(merchantOpenStoreDto.getCityName())
			|| StringUtils.isBlank(merchantOpenStoreDto.getAddress()) || merchantOpenStoreDto.getUserId() == null 
			|| merchantOpenStoreDto.getLongitude() == null || merchantOpenStoreDto.getLatitude() == null ){
			return new AjaxJson(false, "请填写店铺地址", "01");
		}
		
		try {
			//更新商家用户 user
			WUserEntity wuser = this.get(WUserEntity.class, merchantOpenStoreDto.getUserId());
			wuser.setNickname(merchantOpenStoreDto.getNickname());
			wuser.setIsDelete(0);
			this.updateEntitie(wuser);
			
			//更新商家 merchant
			String updateMerchantSql = " update merchant set mobile=?,address=?,longitude=?,latitude=? where id = ? ";
			this.executeSql(updateMerchantSql, merchantOpenStoreDto.getMobile(), merchantOpenStoreDto.getAddress(), 
					merchantOpenStoreDto.getLongitude(), merchantOpenStoreDto.getLatitude(), merchantOpenStoreDto.getMerchantId());
			
			//更新商家信息 0085_merchant_info
			String updateMerchantInfoSql = " update 0085_merchant_info set apply_waimai_status=?,apply_waimai_time=CURRENT_TIMESTAMP(),apply_waimai_city=? "
					+ "where merchant_id = ? ";
			this.executeSql(updateMerchantInfoSql, 1, merchantOpenStoreDto.getCityName(), merchantOpenStoreDto.getMerchantId());
		} catch (Exception e) {
			logger.info("《商家开通外卖服务接口》, 系统异常，开通失败");
			json.setSuccess(false); 
			json.setMsg("系统异常，开通失败");
			json.setStateCode("01");
		}
		return json;
	}
	
	@Override
	public AjaxJson getApplyWaimaiStatus(Integer merchantId) {
		AjaxJson json = new AjaxJson(true, "获取成功", "00");
		logger.info("《商家开通外卖服务接口》, 查询开通状态，merchantId={}", merchantId);
		
		if(merchantId==null){
			return new AjaxJson(false, "参数缺失", "01");
		}
		
		try {
			//获取 0085_merchant_info
			MerchantInfoEntity merchantInfoEntity = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
			json.setObj(merchantInfoEntity.getApplyWaimaiStatus());
		} catch (Exception e) {
			logger.info("《商家开通外卖服务接口》, 系统异常，获取失败");
			json.setSuccess(false); 
			json.setMsg("系统异常，获取失败");
			json.setStateCode("01");
		}
		return json;
	}


}
