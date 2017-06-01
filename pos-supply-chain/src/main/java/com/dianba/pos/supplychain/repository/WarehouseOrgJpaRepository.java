package com.dianba.pos.supplychain.repository;

import com.dianba.pos.supplychain.po.WarehouseOrg;
import com.dianba.pos.supplychain.po.WarehouseOrgPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseOrgJpaRepository extends JpaRepository<WarehouseOrg, WarehouseOrgPK> {

    @Query("select w from WarehouseOrg w where w.warehouseOrgPK.orgId=:orgId")
    List<WarehouseOrg> findByOrgId(@Param("orgId") Integer orgId);
}
