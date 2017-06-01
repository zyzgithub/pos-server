package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.LifeBarcodeRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeBarcodeRelationshipJpaRepository
        extends JpaRepository<LifeBarcodeRelationship, Integer> {

    List<LifeBarcodeRelationship> findByTargetBarcodeIn(List<String> targetBarcodes);
}
