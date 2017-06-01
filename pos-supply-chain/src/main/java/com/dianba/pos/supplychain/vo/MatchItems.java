package com.dianba.pos.supplychain.vo;

import java.util.ArrayList;
import java.util.List;

public class MatchItems {

    private String barcode;
    private List<Items> items;

    private Integer repertory;
    private Integer menuTypeId;
    private Integer standardInventory;
    private Integer warnInventory;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<Items> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public Integer getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(Integer menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public Integer getStandardInventory() {
        return standardInventory;
    }

    public void setStandardInventory(Integer standardInventory) {
        this.standardInventory = standardInventory;
    }

    public Integer getWarnInventory() {
        return warnInventory;
    }

    public void setWarnInventory(Integer warnInventory) {
        this.warnInventory = warnInventory;
    }
}
