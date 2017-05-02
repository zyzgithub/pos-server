package com.dianba.supplychain.mapper;

import com.dianba.supplychain.po.WarehouseGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarehouseGoodsMapper {

    List<WarehouseGoods> getItemsByTemplate(@Param("warehouseId") Integer warehouseId
            , @Param("itemTemplates") String itemTemplates);
}
