package com.dianba.pos.supplychain.repository;

import com.dianba.pos.supplychain.po.LifeSupplyChainWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeSupplyChainWarehouseJpaRepository extends JpaRepository<LifeSupplyChainWarehouse, Long> {

}
