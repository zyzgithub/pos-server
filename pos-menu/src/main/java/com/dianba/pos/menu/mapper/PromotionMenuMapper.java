package com.dianba.pos.menu.mapper;

import com.dianba.pos.menu.po.PromotionMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromotionMenuMapper {

    List<PromotionMenu> findByMenuIdAndMerchantId(Long menuId,Long merchantId);
}