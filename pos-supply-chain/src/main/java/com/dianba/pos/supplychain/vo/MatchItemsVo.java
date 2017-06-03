package com.dianba.pos.supplychain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MatchItemsVo {

    private String barcode;
    private List<ItemsVo> items;

    private Integer repertory;
    private Long menuTypeId;
    private Integer standardInventory = 12;
    private Integer warnInventory = 20;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<ItemsVo> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<ItemsVo> items) {
        this.items = items;
    }

    public Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public Long getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(Long menuTypeId) {
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
