package com.dianba.pos.menu.po;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "promotion_menu")
public class PromotionMenu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PROMOTION_ACTIVITY_ID")
    private Integer promotionActivityId;

    @Column(name = "MENU_ID")
    private Integer menuId;

    @Column(name = "PROMOTION_QUANTITY")
    private Integer promotionQuantity;

    @Column(name = "PROMOTION_PRICE")
    private BigDecimal promotionPrice;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "MERCHANT_ID")
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
