package com.dianba.pos.supplychain.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseItemsVo {

    private Long warehouseId;
    private Map<String, String> barcodeRelateionShip = new HashMap<>();
    private List<MatchItemsVo> matchItems = new ArrayList<>();

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Map<String, String> getBarcodeRelateionShip() {
        return barcodeRelateionShip;
    }

    public void setBarcodeRelateionShip(Map<String, String> barcodeRelateionShip) {
        this.barcodeRelateionShip = barcodeRelateionShip;
    }

    public List<MatchItemsVo> getMatchItems() {
        return matchItems;
    }

    public void setMatchItems(List<MatchItemsVo> matchItems) {
        this.matchItems = matchItems;
    }
}
