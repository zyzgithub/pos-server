package com.dianba.pos.supplychain.repository;

import com.dianba.pos.supplychain.po.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyChainGoodsJpaRepository extends JpaRepository<Goods, Integer> {

    List<Goods> findByBarcodeIn(List<String> barcodes);
}
