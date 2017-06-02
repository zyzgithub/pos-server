package com.dianba.pos.supplychain.mapper;

import com.dianba.pos.supplychain.po.WarehouseGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarehouseGoodsMapper {

    List<WarehouseGoods> getItemsByTemplate(@Param("warehouseId") Long warehouseId
            , @Param("itemTemplates") String itemTemplates);
}
