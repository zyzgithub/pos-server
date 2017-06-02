package com.dianba.pos.supplychain.service.impl;

import com.dianba.pos.supplychain.po.LifeSupplyChainWarehouse;
import com.dianba.pos.supplychain.repository.LifeSupplyChainWarehouseJpaRepository;
import com.dianba.pos.supplychain.service.LifeSupplyChainWarehouseManager;
import com.dianba.pos.supplychain.util.LocationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DefaultLifeSupplyChainWarehouseManager implements LifeSupplyChainWarehouseManager {

    private Logger logger = LogManager.getLogger(DefaultLifeSupplyChainWarehouseManager.class);

    @Autowired
    private LifeSupplyChainWarehouseJpaRepository warehouseJpaRepository;

    public Long getNearbyWarehouse(double latitude, double longitude) {
        logger.info("Get Nearby warehouse parameter -- latitude " + latitude + " longitude " + longitude);
        // 先拿到所有子仓的位置信息 包括配送距离
        List<LifeSupplyChainWarehouse> supplyChainWarehouses = warehouseJpaRepository.findAll();
        Long nearbyWarehouseId = 0L;
        double lastDistance = 0.0;
        for (LifeSupplyChainWarehouse supplyChainWarehouse : supplyChainWarehouses) {
            // 如果仓库的位置信息为空, 则跳过
            if (StringUtils.isEmpty(supplyChainWarehouse.getLocation())) {
                continue;
            }
            String[] location = supplyChainWarehouse.getLocation().split(",");
            BigDecimal warehouseLatitude = BigDecimal.valueOf(Double.parseDouble(location[0]));
            BigDecimal warehouseLongitude = BigDecimal.valueOf(Double.parseDouble(location[1]));
            // 计算商家到仓库的距离
            double distance = LocationUtil.distanceSimplify(warehouseLatitude.doubleValue()
                    , warehouseLongitude.doubleValue(), latitude, longitude);
            // 获取仓库的配送距离(数据库中以千米为单位，而此处以米为单位)
            double deliverRange = supplyChainWarehouse.getCoveringDistance() * 1000;

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
