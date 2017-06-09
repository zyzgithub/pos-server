package com.dianba.pos.purchase.mapper;

import com.dianba.pos.purchase.pojo.OneKeyPurchase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OneKeyPurchaseMapper {

    List<OneKeyPurchase> findWarnSaleItems(Long passportId);
}
