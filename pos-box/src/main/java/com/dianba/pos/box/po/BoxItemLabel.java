package com.dianba.pos.box.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangyong on 2017/7/4.
 */
@Entity
@Table(name = "life_pos.pos_item_label")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoxItemLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "rfid")
    private String rfid;
    @Column(name = "is_paid")
    private Integer isPaid;
    @Column(name = "create_time")
    private Date createTime = new Date();

    @Transient
    private String showPaidName;
    @Transient
    private String itemName;
    @Transient
    private BigDecimal salesPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShowPaidName() {
        return showPaidName;
    }

    public void setShowPaidName(String showPaidName) {
        this.showPaidName = showPaidName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }
}
