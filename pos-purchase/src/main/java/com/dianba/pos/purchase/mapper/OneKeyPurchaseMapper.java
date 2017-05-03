package com.dianba.pos.purchase.mapper;

import com.dianba.pos.menu.po.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OneKeyPurchaseMapper {

    List<Menu> queryWarnInvenstory(
            @Param("merchant_id")Integer merchantId,@Param("exclude_barcode")String excludeBarcode);
}
