package com.wm.service.menu;

import java.util.List;
import java.util.Map;

import com.wm.entity.pos.MarketingReportLine;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.supplychain.SupplyChainMenuVo;
import com.wm.controller.takeout.dto.Shopcart;
import com.wm.controller.takeout.vo.MenuVo;
import com.wm.util.PageList;

public interface MenuServiceI extends CommonService {

    /**
     * 根据商家id获取菜单列表
     *
     * @param merchant_id 商家id
     * @return
     */
    public List<Map<String, Object>> getMenuList(int merchant_id, int userId);

    /**
     * 根据菜品ID查询菜品促销信息,如果为0则查询全部菜品促销信息，大于0就根据menuId查询菜品促销信息
     *
     * @param menuId
     * @return
     */
    public List<Map<String, Object>> getMenuPromotionByMenuId(int menuId);
    
    public List<Map<String, Object>> getMenuPromotionByMenuId(int menuId, int merchantId);

    /**
     * 订单支付成功后，更新销量、库存
     *
     * @param orderId
     */
    public void buyCount(Integer orderId);

    /**
     * 减库存
     *
     * @param menuId
     * @param quantity
     */
    public void updateMenuRepertory(Integer menuId, Integer quantity);

    /**
     * 加促销销量和减促销库存
     *
     * @param orderId
     */
    public void buyCountMenuPromotion(int orderId);

    /**
     * 减促销销量和加促销库存
     *
     * @param orderId
     */
    public void subtractMenuPromotion(int orderId);

    /**
     * 根据ID查询菜品促销信息
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getMenuPromotionById(int id);

    /**
     * 验证支付的促销菜品信息
     *
     * @param orderId
     * @return
     */
    public AjaxJson verificationPayMenuPromotion(int orderId);

    /**
     * 根据菜品id获取菜品所有相关信息
     *
     * @param id     菜品id
     * @param userId 用户id,没有时传0即可(用于查询该用户的限购量)
     * @return map
     */
    public Map<String, Object> getMenu(Integer menuId, Integer userId);

    public List<MenuVo> findByMerchantId(Integer merchantId, Integer userId);

    /**
     * 加载某个店铺下所有分类 每个分类20条数据
     *
     * @param merchantId
     * @param menuTypeId
     * @param userId
     * @param page
     * @param rows
     * @return
     */
    public List<MenuVo> findFirstPageMenuByMerchantId(Integer merchantId, Integer menuTypeId, Integer userId,
                                                      Integer page, Integer rows);

    public int findBuyCount(Integer merchantId);

    public boolean judgePriceAndCount(List<Shopcart> carts);

    public MenuVo getMenuById(Integer menuId, Integer userId);

    public PageList<com.wm.entity.menu.MenuVo> findByEntityList(com.wm.entity.menu.MenuVo menu, Integer page, Integer rows);
    
    public PageList<com.wm.entity.menu.MenuVo> findByEntityList(com.wm.entity.menu.MenuVo menu, List<Integer> activityIds, Integer page,
                                                                Integer rows);

    public PageList<com.wm.entity.menu.MenuVo> findByPromotionEntityList(com.wm.entity.menu.MenuVo menu,List<Integer> activityIds, Integer page,
            Integer rows);
    
    /**
     * 初始化库存
     */
    public void initRep();

    public com.wm.entity.menu.MenuVo searchItemByBarcode(Integer merchantId, String barcode);

    /**
     * 添加库存. 供应链在计划使用/暂未使用
     *
     * @param menus
     * @param merchantId
     * @param mainOrderId
     * @author zhoucong
     * @date 2016年5月24日 下午7:50:58
     */
    public void doAddMenus(List<SupplyChainMenuVo> menus, Integer merchantId, Integer mainOrderId);

    boolean addExistsMenuRepertory(Integer merchantId, String barcode, Integer num);

    /**
     * 恢复订单的库存
     *
     * @param orderId
     */
    void revertRepertory(int orderId);

    /**
     * 更新菜品信息
     *
     * @param menuVo
     */
    void updateMenu(com.wm.entity.menu.MenuVo menuVo);

    /**
     * 根据二维码获取同一上家的其他商品
     *
     * @param barcode
     * @param menuId
     * @param merchantId
     * @return
     */
    List<Map<String, Object>> getAnotherMenuByBarcode(com.wm.entity.menu.MenuVo menuVo);

    public List<MarketingReportLine> marketingReport(String merchantId, String typeId, String searchKey, String startTime, String endTime, Integer num, Integer pageSize);
}
