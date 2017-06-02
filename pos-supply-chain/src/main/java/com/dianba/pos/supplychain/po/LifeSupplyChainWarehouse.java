package com.dianba.pos.supplychain.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "life_saas_supplychain.supplychain_warehouse_properties")
@Cacheable
public class LifeSupplyChainWarehouse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "warehouse_name")
    private String warehouseName;
    @Column(name = "status")
    private Byte status;
    @Column(name = "type")
    private Byte type;
    @Column(name = "location")
    private String location;
    @Column(name = "covering_distance")
    private Integer coveringDistance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCoveringDistance() {
        return coveringDistance;
    }

    public void setCoveringDistance(Integer coveringDistance) {
        this.coveringDistance = coveringDistance;
    }
}
