package com.wm.entity.pos;

/**
 * Created by zc on 2016/11/21.
 */
public class MarketingReportLine {
    /**
     * 排名
     */
    private int position;
    /**
     * 商平名称
     */
    private String menuName;

    /**
     * 条码
     */
    private String barcode;

    /**
     * 商品ID
     */
    private String menuId;
    /**
     * 商品类型ID
     */
    private String menuTypeId;
    /**
     * 商品类型名称
     */
    private String menuTypeName;

    /**
     * 销售数量
     */
    private Long totalSales;
    /**
     * 总销售额
     */
    private String totalMoney;


    /**
     * 销售占比
     */
    private String MoneyRatio;

    /**
     * 毛利
     */
    private String grossMargin;

    /**
     * 毛利率
     */
    private String grossProfit;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(String menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    public Long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Long totalSales) {
        this.totalSales = totalSales;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(String grossMargin) {
        this.grossMargin = grossMargin;
    }

    public String getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(String grossProfit) {
        this.grossProfit = grossProfit;
    }

    public String getMoneyRatio() {
        return MoneyRatio;
    }

    public void setMoneyRatio(String moneyRatio) {
        MoneyRatio = moneyRatio;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
