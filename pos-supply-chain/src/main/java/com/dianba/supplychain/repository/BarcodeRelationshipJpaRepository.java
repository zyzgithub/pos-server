package com.dianba.supplychain.repository;

import com.dianba.supplychain.po.BarcodeRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeRelationshipJpaRepository
        extends JpaRepository<BarcodeRelationship, Integer> {

    List<BarcodeRelationship> findByTargetBarcode(List<String> targetBarcodes);
}
