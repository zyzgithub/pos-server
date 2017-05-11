package com.dianba.pos.menu.service;

import com.dianba.pos.menu.po.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
public interface MenuManager {



    /**
     * 获取增值服务商品信息
     * @param isFlash
     * @param merchantId
     * @param
     * @return
     */
    List<Menu> findAllByIsFlashAndMerchantIdAndPrintTypeAndCodeId(Integer isFlash, Integer merchantId,
      Integer printTypeId,Integer codeId);

    Menu findByPrintType(Integer printType);


}
