package com.dianba.pos.purchase.mapper;

import com.dianba.pos.purchase.pojo.OneKeyPurchase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OneKeyPurchaseMapper {

    List<OneKeyPurchase> queryWarnInvenstory(
            @Param("merchant_id") Integer merchantId, @Param("exclude_barcode") String excludeBarcode);
}
