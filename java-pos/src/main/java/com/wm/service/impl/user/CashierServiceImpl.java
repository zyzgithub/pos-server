package com.wm.service.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MD5;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.VO.PageVO;
import com.base.constant.ErrorCode;
import com.base.exception.BusinessException;
import com.sms.service.SmsByBusinessServiceI;
import com.wm.controller.user.vo.CashierVo;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.supermarket.CashierEntity;
import com.wm.entity.supermarket.CashierLoginLogEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.service.syscode.SysCodeServiceI;
import com.wm.service.user.CashLoginLogServiceI;
import com.wm.service.user.CashierServiceI;
import com.wm.util.AliOcs;

@Service("cashierService")
public class CashierServiceImpl extends CommonServiceImpl implements

		CashierServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(CashierServiceImpl.class);

	@Autowired
	private MerchantServiceI merchantService;
	
	@Autowired
	private SmsByBusinessServiceI smsForCashierLoginService;
	
	@Autowired
	private CashLoginLogServiceI cashierLoginLogService;
	
	@Autowired
	private SuperMarketServiceI superMarketService;
	@Autowired
	private SysCodeServiceI sysCodeService;
	
	@Override
	public List<CashierEntity> getCashiers(Integer merchantId){
		return findHql(" from CashierEntity where merchantId = ? and status = '1'", merchantId);
	}
	
	@Override
	public PageVO<Map<String, Object>> getCashiersByPage(Integer merchantId, int pageNo, int pageSize){
		//实际页码是从0开始，但是app传参是从1开始
		pageNo = pageNo < 1 ? 0: pageNo - 1;
		
		Long totalRecords = getCountForJdbcParam("select count(*) from 0085_cashier where merchant_id=? and status = '1'", new Object[]{merchantId});
		
		PageVO<Map<String, Object>> pageVo = new PageVO<Map<String, Object>>(totalRecords.intValue(), pageSize);
		String sql = "select id, name, mobile, id_card idCard, head_image_url headImageUrl, password, cashier_type cashierType from 0085_cashier where merchant_id=? and status = '1'";
		List<Map<String, Object>> results = findForJdbcParam(sql, pageNo, pageSize, merchantId);
		
		pageVo.setResults(results);
		return pageVo;
	}
	
	
	public boolean isMobileRegistered(String mobile){
		String hql = " from CashierEntity where mobile=? and status='1'";
		List<CashierEntity> list = this.findHql(hql, mobile);
		return list.size() > 0;
	}
	
	public boolean isIdCardRegistered(String idCard){
		String hql = " from CashierEntity where idCard = ? and status='1'";
		List<CashierEntity> list = this.findHql(hql, idCard);
		return list.size() > 0;
	}
	
	public boolean isNameRegistered(String name, Integer merchantId){
		String hql = " from CashierEntity where name = ? and merchant_id = ? and status='1'";
		List<CashierEntity> list = this.findHql(hql, name, merchantId);
		return list.size() > 0;
	}
	
	@Override
	public void saveOrUpdate(CashierVo vo) throws BusinessException{
		MerchantEntity merchant = merchantService.get(MerchantEntity.class, vo.getMerchantId());
		if(merchant == null){
			logger.warn("参数错误，无法根据merchantId:{}找到对应的商家", vo.getMerchantId());
			throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "参数错误");
		}
		
		try {
			CashierEntity entity = null;
			if(vo.getId() == null){
				if(isMobileRegistered(vo.getMobile())){
					logger.warn("参数错误，手机号码:{}已绑定到其他收银员", vo.getMobile());
					throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "手机号码重复");
				}
				if(isIdCardRegistered(vo.getIdCard())){
					logger.warn("参数错误，身份证号码:{}已绑定到其他收银员", vo.getIdCard());
					throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "身份证号码重复");
				}
				if(isNameRegistered(vo.getName(), merchant.getId())){
					logger.warn("参数错误，用户名:{}已存在", vo.getName());
					throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "姓名重复");
				}
				
				entity = new  CashierEntity();
				entity.setStatus(CashierEntity.valid);
			}
			else{
				entity = this.get(CashierEntity.class, vo.getId());
				if(entity == null || entity.getStatus().equals(CashierEntity.unvalid)){
					logger.warn("参数错误，无法根据id:{}找到对应的收银员", vo.getId());
					throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "参数错误");
				}
				//如果修改用户名
				if(!StringUtils.equals(entity.getName(), vo.getName())){
					if(isNameRegistered(vo.getName(), merchant.getId())){
						logger.warn("参数错误，用户名:{}已存在", vo.getName());
						throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "姓名重复");
					}
				}
				
				//如果修改手机号码
				if(!StringUtils.equals(entity.getMobile(), vo.getMobile())){
					if(isMobileRegistered(vo.getMobile())){
						logger.warn("参数错误，手机号码:{}已绑定到其他收银员", vo.getMobile());
						throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "手机号码重复");
					}
				}
				
//				//如果修改身份证号码
//				if(!StringUtils.equals(entity.getIdCard(), vo.getIdCard())){
//						logger.warn("参数错误，传入身份证号码 : {} ,与原身份证号码: {} 不一致", vo.getIdCard(), entity.getIdCard());
//						throw new BusinessException(ErrorCode.INVALID_ARGUMENT,  "身份证号码不一致");
//				}
            }

            BeanUtils.copyProperties(entity, vo);
            entity.setCreateTime(DateUtils.getSeconds());
            saveOrUpdate(entity);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "内部错误", e);
        }
    }

    @Override
    public CashierVo get(Integer id) {
        CashierEntity entity = getEntity(CashierEntity.class, id);

        if (entity == null || entity.getStatus().equals(CashierEntity.unvalid)) {
            logger.warn("参数错误，无法根据id:{}找到对应的收银员", id);
            return null;
        }

        try {
            CashierVo vo = new CashierVo();
            BeanUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        CashierEntity entity = getEntity(CashierEntity.class, id);

        if (entity == null) {
            logger.warn("参数错误，无法根据id:{}找到对应的收银员", id);
            return false;
        }

        entity.setStatus(CashierEntity.unvalid);
        this.saveOrUpdate(entity);
        return true;
    }

    @Override
    public Map<String, Object> login(String mobile, String password)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();

        if (mobile == null) {
            throw new NullPointerException("mobile==null");
        }
        if (password == null) {
            throw new NullPointerException("password==null");
        }

        List<CashierEntity> entities = findHql(" from CashierEntity where mobile = ? and status = '1'", mobile);
        if (entities.size() > 1) {
            logger.warn("无法根据mobile:{}确定唯一的账号", mobile);
            throw new BusinessException(ErrorCode.WRONG_DATA, "数据异常");
        }

        //用户名、密码错误
        if (entities.size() == 0) {
            result.put("success", false);
            result.put("msg", "用户名错误");
        } else {
            CashierEntity cashier = entities.get(0);
            MerchantInfoEntity merchantInfoEntity = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", cashier.getMerchantId());

            String password1 = MD5.GetMD5Code(cashier.getPassword() + "@4!@#@W$%@");
            if (!StringUtils.equals(password, password1)) {
                result.put("success", false);
                result.put("msg", "密码错误");
            } else if (merchantInfoEntity == null) {
                result.put("success", false);
                result.put("msg", "找不到商家 信息");
            } else {
                result.put("success", true);
                result.put("msg", "登录成功");

                MerchantEntity merchant = merchantService.get(MerchantEntity.class, cashier.getMerchantId());
                Map<String, Object> cashierInfo = new HashMap<String, Object>();
                cashierInfo.put("cashierId", cashier.getId());
                cashierInfo.put("name", cashier.getName());
                cashierInfo.put("headerImageUrl", cashier.getHeadImageUrl());
                cashierInfo.put("merchantName", merchant.getTitle());
                cashierInfo.put("merchantId", cashier.getMerchantId());
                cashierInfo.put("cashierType", cashier.getCashierType());
                if (1 == cashier.getCashierType().intValue()) {
                    cashierInfo.put("typeName", "营业员");
                }
                if (2 == cashier.getCashierType().intValue()) {
                    cashierInfo.put("typeName", "店长");
                }
                cashierInfo.put("shopFromType", merchantInfoEntity.getShopFromType());
                if (2 == merchantInfoEntity.getShopFromType().intValue()) {
                    cashierInfo.put("fromTypeName", "直营店");
                }
                if (1 == merchantInfoEntity.getShopFromType().intValue()) {
                    cashierInfo.put("fromTypeName", "加盟店");
                }
                cashierInfo.putAll(checkCashierSettlement(cashier.getId()));
                result.put("cashierInfo", cashierInfo);

                //保存登录日志
                this.saveCashierLoginLog(cashier, CashierLoginLogEntity.LOGIN, null);

            }

        }
        return result;
    }

    @Override
    public Map<String, Object> loginByVerifyCode(Integer merchantId, String mobile, String verifyCode) {
        if (merchantId == null) {
            throw new NullPointerException("merchantId==null");
        }
        if (mobile == null) {
            throw new NullPointerException("mobile==null");
        }
        if (verifyCode == null) {
            throw new NullPointerException("password==null");
        }

        Map<String, Object> result = new HashMap<String, Object>();

        String businessCode = smsForCashierLoginService.getBusinessCode();

        Object o = AliOcs.getObject(businessCode + "_" + mobile);
        if (o == null) {
            result.put("success", false);
            result.put("msg", "请重新获取短信验证码");
        } else {
            CashierEntity cashier = this.findUniqueByProperty(CashierEntity.class, "mobile", mobile);
            if (cashier == null || cashier.getStatus().equals(CashierEntity.unvalid)) {
                result.put("success", false);
                result.put("msg", "手机号码不正确");
                return result;
            }

            String verifyCodeFromSms = o.toString().replace(mobile, "");

            if (!StringUtils.equals(verifyCodeFromSms, verifyCode)) {
                result.put("success", false);
                result.put("msg", "短信验证码不正确");
                return result;
            } else {

                //是在指定商家登录
                if (merchantId.equals(cashier.getMerchantId())) {
                    result.put("success", false);
                    result.put("msg", "登录成功");

                    MerchantEntity merchant = merchantService.get(MerchantEntity.class, merchantId);
                    MerchantInfoEntity merchantInfoEntity = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", cashier.getMerchantId());
                    Map<String, Object> cashierInfo = new HashMap<String, Object>();
                    cashierInfo.put("cashierId", cashier.getId());
                    cashierInfo.put("name", cashier.getName());
                    cashierInfo.put("headerImageUrl", cashier.getHeadImageUrl());
                    cashierInfo.put("merchantName", merchant.getTitle());
                    cashierInfo.put("merchantId", merchantId);
                    cashierInfo.put("cashierType", cashier.getCashierType());
                    if (1 == cashier.getCashierType().intValue()) {
                        cashierInfo.put("typeName", "营业员");
                    }
                    if (2 == cashier.getCashierType().intValue()) {
                        cashierInfo.put("typeName", "店长");
                    }
                    cashierInfo.put("shopFromType", merchantInfoEntity.getShopFromType());
                    if (2 == merchantInfoEntity.getShopFromType().intValue()) {
                        cashierInfo.put("fromTypeName", "直营店");
                    }
                    if (1 == merchantInfoEntity.getShopFromType().intValue()) {
                        cashierInfo.put("fromTypeName", "加盟店");
                    }
                    cashierInfo.putAll(checkCashierSettlement(cashier.getId()));
                    result.put("cashierInfo", cashierInfo);

                    //保存登录日志
                    this.saveCashierLoginLog(cashier, CashierLoginLogEntity.LOGIN, null);
                } else {
                    result.put("success", false);
                    result.put("msg", "您不能在这个商家登录");
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> exitLogin(Integer cashierId) {
        Map<String, Object> result = new HashMap<String, Object>();

        CashierEntity cashier = this.get(CashierEntity.class, cashierId);
        if (cashier == null || cashier.getStatus().equals(CashierEntity.unvalid)) {
            result.put("success", false);
            result.put("msg", "参数错误");
            return result;
        }

        //查看是否存在未结算的现金订单
        if (superMarketService.existUnsettledCashOrder(cashierId)) {
            result.put("success", false);
            result.put("msg", "存在未结算的现金订单");
            return result;
        }


        //保存退出登录日志
        this.saveCashierLoginLog(cashier, CashierLoginLogEntity.EXIT, null);

        result.put("success", true);
        result.put("msg", "退出登录成功");
        return result;
    }

    @Override
    public String getLatestLoginTime(Integer cashierId) {
        String sql = "select max(create_time) from 0085_cashier_login_log where cashier_id = ? and login_type = '1'";
        Integer createTime = this.findOneForJdbc(sql, Integer.class, cashierId);

        if (createTime != null) {
            return new DateTime(((long) createTime) * 1000).toString("yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }


    /**
     * 校验收银员是否有未支付的结算订单
     *
     * @param cashierId
     * @return
     */
    private Map<String, Object> checkCashierSettlement(Integer cashierId) {
        StringBuilder query = new StringBuilder();
        query.append("select id, settlement_order_id, cash from 0085_cashier_settlement_log");
        query.append(" where is_paid = '0' and cashier_id = ? order by create_time limit 0,1");
        Map<String, Object> map = findOneForJdbc(query.toString(), cashierId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (map == null) {
            resultMap.put("state", "normal");//正常登陆
            resultMap.put("settlementLogId", "");
            resultMap.put("cash", "");
        } else {
            resultMap.put("state", "unpay");//有未支付的结算订单
            resultMap.put("settlementLogId", map.get("id"));
            resultMap.put("cash", map.get("cash"));
        }
        return resultMap;
    }


    //保存登录日志
    public void saveCashierLoginLog(CashierEntity cashier, int loginType, String deviceCode) {
        CashierLoginLogEntity log = new CashierLoginLogEntity();
        if (CashierLoginLogEntity.EXIT == loginType) {
            Map<String, Object> map = this.getDeviceCodeAndType(cashier.getId());
            if (map != null && map.get("deviceCode") != null) {
                deviceCode = map.get("deviceCode").toString();
            }
        }
        log.setCashierId(cashier.getId());
        log.setMerchantId(cashier.getMerchantId());
        log.setName(cashier.getName());
        log.setLoginType(loginType);
        log.setCreateTime(DateUtils.getSeconds());
        log.setDeviceCode(deviceCode);
        cashierLoginLogService.save(log);
    }

    @Override
    public Map<String, Object> newLogin(String mobile, String password,
                                        String deviceCode, int posEdition) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();

        if (mobile == null) {
            throw new NullPointerException("mobile==null");
        }
        if (password == null) {
            throw new NullPointerException("password==null");
        }

        List<CashierEntity> entities = findHql(" from CashierEntity where mobile = ? and status = '1'", mobile);
        if (entities.size() > 1) {
            logger.warn("无法根据mobile:{}确定唯一的账号", mobile);
            throw new BusinessException(ErrorCode.WRONG_DATA, "数据异常");
        }


        //用户名、密码错误
        if (entities.size() == 0) {
            result.put("success", false);
            result.put("msg", "用户名错误");
        } else {
            CashierEntity cashier = entities.get(0);
//			Map<String, Object> logMap = this.getDeviceCodeAndType(cashier.getId());
//			if(logMap != null && logMap.get("deviceCode") != null){
//				if(CashierLoginLogEntity.LOGIN == Integer.parseInt(logMap.get("loginType").toString()) && !deviceCode.equals(logMap.get("deviceCode").toString())){
//					logger.info("不是同一台POS机");
//					throw new BusinessException(ErrorCode.WRONG_DATA, "账号已登录，还未结算，请不要重复登录");
//				}
//			}
            Integer posEditionValue = this.getPosEdition(cashier.getId());
            logger.info("pos机版本, posEdition:{}", posEditionValue);
            if(posEditionValue == null || posEditionValue.intValue() == 0){
            	throw new BusinessException(ErrorCode.WRONG_DATA, "请绑定pos版本");
            }
            if(posEdition != posEditionValue.intValue()){
            	String  edition = sysCodeService.findSysCodeNameByCodeAndValue("pos_edition", posEditionValue.intValue());
            	throw new BusinessException(ErrorCode.WRONG_DATA, "请登录"+edition);
            }
            MerchantInfoEntity merchantInfoEntity = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", cashier.getMerchantId());
            MerchantEntity merchant = merchantService.get(MerchantEntity.class, cashier.getMerchantId());

            String password1 = MD5.GetMD5Code(cashier.getPassword() + "@4!@#@W$%@");
            if (!StringUtils.equals(password, password1)) {
            	result.put("success", false);
                result.put("msg", "密码错误");
            } else if (merchantInfoEntity == null) {
                result.put("success", false);
                result.put("msg", "找不到商家 信息");
            } else if(1 == merchant.getWuser().getIsDelete().intValue()){
            	result.put("success", false);
            	result.put("msg", "该商户账户已冻结，请联系店主");
            } else {
                result.put("success", true);
                result.put("msg", "登录成功");

                Map<String, Object> cashierInfo = new HashMap<String, Object>();
                cashierInfo.put("cashierId", cashier.getId());
                cashierInfo.put("name", cashier.getName());
                cashierInfo.put("headerImageUrl", cashier.getHeadImageUrl());
                cashierInfo.put("merchantName", merchant.getTitle());
                cashierInfo.put("merchantId", cashier.getMerchantId());
                cashierInfo.put("cashierType", cashier.getCashierType());
                cashierInfo.put("merchantUserId", merchant.getWuser().getId());
                cashierInfo.put("dineOrderPrint", merchant.getDineOrderPrint());
                if (1 == cashier.getCashierType().intValue()) {
                    cashierInfo.put("typeName", "营业员");
                }
                if (2 == cashier.getCashierType().intValue()) {
                    cashierInfo.put("typeName", "店长");
                }
                cashierInfo.put("shopFromType", merchantInfoEntity.getShopFromType());
                if (2 == merchantInfoEntity.getShopFromType().intValue()) {
                    cashierInfo.put("fromTypeName", "直营店");
                }
                if (1 == merchantInfoEntity.getShopFromType().intValue()) {
                    cashierInfo.put("fromTypeName", "加盟店");
                }
                cashierInfo.putAll(checkCashierSettlement(cashier.getId()));
                result.put("cashierInfo", cashierInfo);

                // post 服务信息

                //保存登录日志
                this.saveCashierLoginLog(cashier, CashierLoginLogEntity.LOGIN, deviceCode);

            }

        }


        return result;
    }



    private Map<String, Object> getDeviceCodeAndType(Integer cashierId) {
        StringBuilder query = new StringBuilder();
        query.append("select device_code deviceCode, login_type loginType from 0085_cashier_login_log where cashier_id = ? order by id desc limit 0,1");
        return findOneForJdbc(query.toString(), cashierId);
    }

	@Override
	public Integer getPosEdition(Integer cashierId) {
		StringBuilder query = new StringBuilder();
		query.append(" select mp.edition_code_value posEdition from ");
		query.append(" 0085_cashier c, 0085_merchant_pos mp ");
		query.append("  where c.merchant_id = mp.merchant_id");
		query.append("  and c.id = ?");
		return findOneForJdbc(query.toString(), Integer.class, cashierId);
	}
}
