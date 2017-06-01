package com.dianba.pos.supplychain.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class WarehouseOrgPK implements Serializable {

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "org_id")
    private Integer orgId;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
