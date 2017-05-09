package com.dianba.supplychain.po;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "supply_chain_warehouse_org")
public class WarehouseOrg implements Serializable{

    @EmbeddedId
    private WarehouseOrgPK warehouseOrgPK;

    public WarehouseOrgPK getWarehouseOrgPK() {
        return warehouseOrgPK;
    }

    public void setWarehouseOrgPK(WarehouseOrgPK warehouseOrgPK) {
        this.warehouseOrgPK = warehouseOrgPK;
    }
}
