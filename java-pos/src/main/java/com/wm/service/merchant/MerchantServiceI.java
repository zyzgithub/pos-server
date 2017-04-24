package com.wm.service.merchant;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import java.math.BigDecimal;

import com.wm.controller.takeout.dto.WXHomeDTO;
import com.wm.controller.takeout.vo.MerchantSimpleVo;
import com.wm.controller.takeout.vo.OrderSimpleVo;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantStatisticsMenuVo;
import com.wm.util.PageList;

public interface MerchantServiceI extends CommonService{
	/**
	 * 商家转账
	 * @param userName
	 * @param money
	 * @param merchantId
	 * @return
	 */
	public AjaxJson MerchantTransferAccounts(String userName,double money,int merchantId);
	/**
	 * 商家申请提现
	 * @param merchantId
	 * @param money
	 * @param cardId
	 * @return
	 */
	public AjaxJson merchantWithdraw(int merchantId,double money,int cardId) throws Exception;
	
	/**
	 * 商家群发短信
	 * @param merchantId
	 */
	public void merchanTmassTexting(int merchantId,String title,String content,HttpServletRequest request);
	
	/**
	 * 审核发短信通过
	 * @param id
	 */
	public AjaxJson passTmassTexting(int id);
	/**
	 * 审核发短信不通过 
	 * @param id
	 */
	public void noPassTmassTextting(int id);
	
	/**
	 * 根据经纬度查询附近所有店铺
	 * @param lng
	 * @param lat
	 * @return
	 */
	public List<MerchantSimpleVo> findByLocation(WXHomeDTO wx);
	
	public OrderSimpleVo lastedOrder(Integer userId);

	/**
	 * 查询用户的商家收藏列表
	 * @param userId
	 * @param page 从1开始
	 * @param rows
	 * @return
	 */
	List<MerchantSimpleVo> queryUserFavMerchantByUserId(Integer userId, double lng, double lat, int page, int rows);
	
	public MerchantEntity queryByMenuId(Integer menuId);
	
	/**
	 *  门店热销菜品统计
	 * @param title
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageList<MerchantStatisticsMenuVo> statisticsMenu(String title,String startTime, String endTime, Integer page, Integer rows);
	
	/**
	 * 推送新订单到商家版APP
	 * @param orderId
	 */
	public void pushOrder(Integer orderId);
	
	/**
	 * 我的商家店铺
	 * @param orderId
	 */
	public Map<String, Object> getMyMerchant(Long merchantId);
	
	/**
	 * 获得商家营业时间
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getMerchantOpenTime(Long merchantId);
	
	/**
	 * 更新商家营业时间
	 * @param id
	 * @param startTime
	 * @param endTime
	 */
	public void updateMerchantDeliveryScope(int merchantId,BigDecimal deliveryScope);
	
	/**
	 * 	添加或修改营业时间
	 * @param params
	 * @return
	 */
	public AjaxJson updateOrSaveMerchantOpenTime(String params);
	
	/**
	 * 删除商家营业时间
	 * @param id
	 * @param merchantId 商家id
	 */
	public AjaxJson deleteOpentime(Integer id, Integer merchantId);
	
	/**
	 * 获取商家信息根据商家ID
	 * @param merchantId
	 * @return
	 */
	public String getMerchantPlatformType(int merchantId);
	
	public List<Map<String, Object>> merchantDeviceList(String codeValue) throws Exception;
	
	/**
	 * 查找所有的有效商家
	 * @param isDetele 是否删除，默认0=不删除，1=删除, null时查询所有
	 * @return
	 */
	public List<Map<String, Object>> findByDelState(Integer isDetele);
	
	/**
	 * 获取商家来源类型
	 * @param merchantId
	 * @return
	 */
	public String getMerchantSource(Integer merchantId);
	
	/**
	 * 判断商家店铺是分店(0)、总店(1)、或普通店铺(2)
	 * @param merchantId
	 * @return
	 */
	public int getStoreType(Integer merchantId);

	/**
	 * 获取商家的userId
	 * @param merchantId
	 * @return
	 */
	public abstract int getMerchantUserId(int merchantId);
}
