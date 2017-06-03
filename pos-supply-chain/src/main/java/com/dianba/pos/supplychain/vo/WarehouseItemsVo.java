package com.dianba.pos.supplychain.vo;

import java.util.ArrayList;
import java.util.List;

public class WarehouseItemsVo {

    private Long warehouseId;
    private List<MatchItemsVo> matchItems = new ArrayList<>();

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<MatchItemsVo> getMatchItems() {
        return matchItems;
    }

    public void setMatchItems(List<MatchItemsVo> matchItems) {
        this.matchItems = matchItems;
    }
}
