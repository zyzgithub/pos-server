package com.wm.controller.supplychain;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/supplychainMerchantController")
public class SupplychainMerchantController {
    private static final Logger logger = LoggerFactory.getLogger(SupplychainMerchantController.class);

    @Resource
    private MerchantServiceI merchantService;
    @Resource
    private MenuServiceI menuService;

    /**
     * 获取供应链商家端相关的商家信息
     *
     * @param userId 商家ID
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(params = "getMerchantParameter")
    @ResponseBody
    public AjaxJson getMerchantParameter(Integer userId) throws JsonProcessingException {
        logger.info("1.5被调用，获取供应链商家端相关的商家信息, userId={}", userId);
        Map<String, Object> resultMap = new HashMap<String, Object>();

        MerchantEntity merchantEntity = merchantService.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId);
        if (merchantEntity != null) {
            resultMap.put("merchantId", merchantEntity.getId());
            resultMap.put("merchantName", merchantEntity.getTitle());
            resultMap.put("longitude", merchantEntity.getLng());
            resultMap.put("latitude", merchantEntity.getLat());
            resultMap.put("mobile", merchantEntity.getMobile());
            resultMap.put("address", merchantEntity.getAddress());
            resultMap.put("printCode", merchantEntity.getPrintCode());
            if (merchantEntity.getCategory() != null) {
                resultMap.put("groupId", merchantEntity.getCategory().getId());
            } else {
                resultMap.put("groupId", 0);
            }

            Integer merchantId = merchantEntity.getId();
            MerchantInfoEntity merchantInfoEntity = merchantService.findUniqueByProperty(MerchantInfoEntity.class,
                    "merchantId", merchantId);
            if (merchantInfoEntity != null) {
                resultMap.put("merchantSource", merchantInfoEntity.getMerchantSource());
            }

            List<Map<String, Object>> list = merchantService
                    .findForJdbc("select * from 0085_merchant_org where merchant_id=?", merchantId);
            if (CollectionUtils.isNotEmpty(list)) {
                resultMap.put("orgId", list.get(0).get("org_id"));
            }
        }

        WUserEntity userEntity = merchantService.get(WUserEntity.class, userId);
        if (userEntity != null) {
            resultMap.put("userName", userEntity.getUsername());
            resultMap.put("userMoblie", userEntity.getMobile());
        }

        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setStateCode(AjaxJson.STATE_CODE_SUCCESS);
        ajaxJson.setObj(resultMap);
        return ajaxJson;
    }

    /**
     * 添加库存
     *
     * @param merchantId 商家ID
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(params = "addMenu")
    @ResponseBody
    public AjaxJson addMenu(String merchantId, String mainOrderId, String menuJson) throws JsonProcessingException {
        AjaxJson ajaxJson = new AjaxJson();

        try {
            logger.info(String.format("addMenu -> merchantId :  %s , mainOrderId : %s , MenuJson : %s " , merchantId,mainOrderId,menuJson));
            List<SupplyChainMenuVo> menus = JSONArray.parseArray(menuJson, SupplyChainMenuVo.class);
            Integer id = Integer.valueOf(merchantId);

            Integer intMainOrderId = null;
            if (StringUtils.isNotBlank(mainOrderId)) {
                intMainOrderId = Integer.valueOf(mainOrderId);
            }
            this.menuService.doAddMenus(menus, id, intMainOrderId);
            // barcode
            ajaxJson.setSuccess(true);
            ajaxJson.setStateCode(AjaxJson.STATE_CODE_SUCCESS);
            ajaxJson.setObj(null);
        } catch (Throwable e) {
            logger.error(String.format("供应链添加库存失败 merchantId: %s ,menuJson : %s  ", merchantId, menuJson), e);
            ajaxJson.setSuccess(false);
            ajaxJson.setStateCode(AjaxJson.STATE_CODE_FAIL);
            ajaxJson.setObj(null);
        }
        return ajaxJson;
    }

    // @Autowired
    // private LsToErpTransactionProduct erpTransactionProduct;

    /**
     * 添加库存
     *
     * @param merchantId
     *            商家ID
     * @return
     * @throws JsonProcessingException
     */
    /*
	 * @RequestMapping(params = "testMq")
	 * 
	 * @ResponseBody public AjaxJson testMq(String key, String menuJson) throws
	 * JsonProcessingException { AjaxJson ajaxJson = new AjaxJson(); final
	 * String k = key;
	 * 
	 * erpTransactionProduct.merchantOrderDeliveryOrder(1, new
	 * LocalTransactionExecuter() {
	 * 
	 * @Override public TransactionStatus execute(Message msg, Object arg) { if
	 * ("commit".equals(k)) { return TransactionStatus.CommitTransaction; } else
	 * { return TransactionStatus.Unknow; } } }); return ajaxJson; }
	 */

}
