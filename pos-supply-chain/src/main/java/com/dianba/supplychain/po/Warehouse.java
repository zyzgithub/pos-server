package com.dianba.supplychain.po;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "supply_chain_warehouse")
@Cacheable
public class Warehouse implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer handler;
    private String userMobile;
    private String userName;
    private Byte status;
    private Byte type;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String address;
    private String img;
    private String printerNumber;
    private Byte scopeType;
    private Integer pid;
    private String ownerArea;
    private Byte sharedStock;
    private Timestamp createTime;
    private Double deliveryScope;
    private Double deliveryPrice;

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
