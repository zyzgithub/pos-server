package com.dianba.supplychain.service.impl;

import com.dianba.supplychain.po.Warehouse;
import com.dianba.supplychain.repository.WarehouseJpaRepository;
import com.dianba.supplychain.service.WarehouseOrgManager;
import com.dianba.supplychain.util.LocationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DefaultWarehouseOrgManager implements WarehouseOrgManager {

    private Logger logger = LogManager.getLogger(DefaultWarehouseOrgManager.class);

    @Autowired
    private WarehouseJpaRepository warehouseJpaRepository;

    public int getNearbyWarehouse(double latitude, double longitude) {
        logger.info("Get Nearby warehouse parameter -- latitude " + latitude + " longitude " + longitude);
        // 先拿到所有子仓的位置信息 包括配送距离
        List<Warehouse> supplyChainWarehouses = warehouseJpaRepository.findAll();
        int nearbyWarehouseId = 0;
        double lastDistance = 0.0;
        for (Warehouse supplyChainWarehouse : supplyChainWarehouses) {
            BigDecimal warehouseLatitude = supplyChainWarehouse.getLatitude();
            BigDecimal warehouseLongitude = supplyChainWarehouse.getLongitude();

            // 如果仓库的位置信息为空, 则跳过
            if (warehouseLatitude == null || warehouseLongitude == null) {
                continue;
            }
            if (supplyChainWarehouse.getScopeType() != 1) {
                continue;
            }
            if (supplyChainWarehouse.getId() == 183) {
                // 排除有家子仓
                continue;
            }
            // 计算商家到仓库的距离
            double distance = LocationUtil.distanceSimplify(warehouseLatitude.doubleValue()
                    , warehouseLongitude.doubleValue(), latitude, longitude);
            // 获取仓库的配送距离(数据库中以千米为单位，而此处以米为单位)
            double deliverRange = supplyChainWarehouse.getDeliveryScope() * 1000;

            if (distance > deliverRange) {
                continue;
            }
            if ((lastDistance == 0) || (distance < lastDistance)) {
                lastDistance = distance;
                nearbyWarehouseId = supplyChainWarehouse.getId();
            }
        }
        return nearbyWarehouseId;
    }
}
