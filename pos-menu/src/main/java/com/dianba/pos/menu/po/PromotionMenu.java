package com.dianba.pos.menu.po;

import java.math.BigDecimal;
import java.util.Date;

public class PromotionMenu {
    private Integer id;

    private Integer promotionActivityId;

    private Integer menuId;

    private Integer promotionQuantity;

    private BigDecimal promotionPrice;

    private Date createTime;

    private Integer merchantId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPromotionActivityId() {
        return promotionActivityId;
    }

    public void setPromotionActivityId(Integer promotionActivityId) {
        this.promotionActivityId = promotionActivityId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getPromotionQuantity() {
        return promotionQuantity;
    }

    public void setPromotionQuantity(Integer promotionQuantity) {
        this.promotionQuantity = promotionQuantity;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}