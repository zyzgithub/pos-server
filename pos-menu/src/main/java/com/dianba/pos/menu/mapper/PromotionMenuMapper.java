package com.dianba.pos.menu.mapper;

import com.dianba.pos.menu.po.PromotionMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionMenuMapper {

    List<PromotionMenu> findByMenuIdAndMerchantId(@Param(value = "menu_id") Long menuId
            , @Param(value = "merchant_id") Long merchantId);
}
