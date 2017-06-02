package com.dianba.pos.supplychain.repository;

import com.dianba.pos.supplychain.po.LifeSupplyChainItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeSupplyChainItemsJpaRepository extends JpaRepository<LifeSupplyChainItems, Integer> {

    List<LifeSupplyChainItems> findByBatchesCodeIn(List<String> batchesCodes);

    List<LifeSupplyChainItems> findByWarehouseIdAndItemTemplateIdInAndStatusAndRestrictionQuantity(
            Long warehouseId, List<Long> itemTemplateIds, Byte status, Integer restrictionQuantity);
}
