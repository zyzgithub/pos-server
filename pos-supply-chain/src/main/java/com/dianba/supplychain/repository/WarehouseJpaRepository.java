package com.dianba.supplychain.repository;

import com.dianba.supplychain.po.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<Warehouse, Integer> {

}
