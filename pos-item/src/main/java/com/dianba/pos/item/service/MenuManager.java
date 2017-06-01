package com.dianba.pos.item.service;

import com.dianba.pos.item.po.Menu;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
public interface MenuManager {


    /**
     * 获取增值服务商品信息
     *
     * @param isFlash
     * @param merchantId
     * @param
     * @return
     */
    List<Menu> findAllByIsFlashAndMerchantIdAndPrintTypeAndCodeId(Integer isFlash, Integer merchantId,
                                                                  Integer printTypeId, Integer codeId);

    Menu findByPrintType(Integer printType);

    /**
     * 获取商品信息根据 第三方商品id
     * @param menuKey
     * @return
     */
    Menu findByMenuKeyAndDisplayAndIsDelete(String menuKey,String display,String isDelete);


}
