package com.dianba.pos.supplychain.po;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "supply_chain_warehouse")
@Cacheable
public class Warehouse implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "handler")
    private Integer handler;

    @Column(name = "user_mobile")
    private String userMobile;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "status")
    private Byte status;

    @Column(name = "type")
    private Byte type;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "address")
    private String address;

    @Column(name = "img")
    private String img;

    @Column(name = "printer_number")
    private String printerNumber;

    @Column(name = "scope_type")
    private Byte scopeType;

    @Column(name = "pid")
    private Integer pid;

    @Column(name = "owner_area")
    private String ownerArea;

    @Column(name = "shared_stock")
    private Byte sharedStock;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "delivery_scope")
    private Double deliveryScope;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "channelId")
    private Long channelId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getHandler() {
        return handler;
    }

    public void setHandler(Integer handler) {
        this.handler = handler;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    public Byte getScopeType() {
        return scopeType;
    }

    public void setScopeType(Byte scopeType) {
        this.scopeType = scopeType;
    }

    public Integer getPid() {
        return pid == null ? 0 : pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getOwnerArea() {
        return ownerArea;
    }

    public void setOwnerArea(String ownerArea) {
        this.ownerArea = ownerArea;
    }

    public Byte getSharedStock() {
        return sharedStock == null ? 0 : sharedStock;
    }

    public void setSharedStock(Byte sharedStock) {
        this.sharedStock = sharedStock;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getPrinterNumber() {
        return printerNumber;
    }

    public void setPrinterNumber(String printerNumber) {
        this.printerNumber = printerNumber;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "SupplyChainWarehouse [id=" + id + ", name=" + name + ", handler=" + handler + ", userMobile="
                + userMobile + ", userName=" + userName + ", status=" + status + ", type=" + type + ", longitude="
                + longitude + ", latitude=" + latitude + ", address=" + address + ", img=" + img + ", scopeType="
                + scopeType + ", pid=" + pid + ", createTime=" + createTime + ", deliveryScope=" + deliveryScope
                + ", deliveryPrice=" + deliveryPrice + "]";
    }

    public Double getDeliveryScope() {
        return deliveryScope;
    }

    public void setDeliveryScope(Double deliveryScope) {
        this.deliveryScope = deliveryScope;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

}
