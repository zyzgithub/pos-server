package com.dianba.pos.item.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.mapper.PosItemMapper;
import com.dianba.pos.item.po.*;
import com.dianba.pos.item.repository.*;
import com.dianba.pos.item.service.LifeItemTemplateManager;
import com.dianba.pos.item.service.LifeItemTypeManager;
import com.dianba.pos.item.service.LifeItemUnitManager;
import com.dianba.pos.item.service.PosItemManager;
import com.dianba.pos.item.vo.PosItemVo;
import com.dianba.pos.item.vo.PosTypeVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Service
public class DefaultPosItemManager implements PosItemManager {

    private Logger logger = LogManager.getLogger(DefaultPosItemManager.class);
    @Autowired
    private PosItemJpaRepository posItemJpaRepository;

    @Autowired
    private LifeItemTemplateManager itemTemplateManager;

    @Autowired
    private PosItemManager posItemManager;

    @Autowired
    private LifeItemUnitManager itemUnitManager;

    @Autowired
    private LifeItemTypeManager itemTypeManager;

    @Autowired
    private LifeItemTemplateJpaRepository itemTemplateJpaRepository;

    @Autowired
    private LifeItemUnitJpaRepository itemUnitJpaRepository;

    @Autowired
    private PosItemMapper posItemMapper;

    @Autowired
    private PosTypeJpaRepository posTypeJpaRepository;

    @Autowired
    private LifeItemTypeJpaRepository lifeItemTypeJpaRepository;

    @Override
    public List<PosItem> getAllByPosTypeId(Long posTypeId) {
        return posItemJpaRepository.getAllByPosTypeId(posTypeId);
    }

    @Override
    public List<PosItem> getAllByPassportIdAndItemTypeId(Long passportId, Long itemTypeId) {
        return posItemJpaRepository.getAllByPassportIdAndItemTypeId(passportId, itemTypeId);
    }

    @Override
    public List<PosItem> getAllByPassportId(Long passportId) {
        return posItemJpaRepository.getAllByPassportId(passportId);
    }


    @Override
    public PosItem getPosItemByPassportIdAndItemTemplateId(Long passportId, Long itemId) {
        return posItemJpaRepository.getPosItemByPassportIdAndItemTemplateId(passportId, itemId);
    }

    @Override
    public PosItemVo getItemByBarcode(String barcode, String passportId) {

        logger.info("==================根据barcod查询是否有商品模板信息=========================");
        JSONObject jsonObject = new JSONObject();
        LifeItemTemplate itemTemplate = itemTemplateManager.getItemTemplateByBarcode(barcode);
        PosItemVo posItemVo = null;

        if (itemTemplate != null) { //商品模板有此商品信息
            Long userId = Long.parseLong(passportId);

            posItemVo = new PosItemVo();
            PosItem posItem = posItemManager.getPosItemByPassportIdAndItemTemplateId(userId, itemTemplate.getId());
            if (posItem == null) {
                // posItemVo.setId(posItem.getId());
                //    posItemVo.setPosTypeId(posItem.getItemTypeId());
                // ItemType itemType=itemTypeManager.getItemTypeById(posItem.getItemTypeId());
                //posItemVo.setPosTypeName(itemType.getTitle());
                posItemVo.setItemTemplateId(itemTemplate.getId());
                posItemVo.setItemName(itemTemplate.getName());
                posItemVo.setStockPrice(itemTemplate.getCostPrice());
                posItemVo.setSalesPrice(itemTemplate.getDefaultPrice());
                //posItemVo.setBuyCount(posItem.getBuyCount());
                //posItemVo.setCreateDate(posItem.getCreateTime());
                posItemVo.setBarcode(itemTemplate.getBarcode());
                // posItemVo.setIsDelete(posItem.getIsDelete());
                // posItemVo.setIsShelve(posItem.getIsShelve());
                posItemVo.setItemImg(itemTemplate.getImageUrl());
                // posItemVo.setRepertory(posItem.getRepertory());
//                posItemVo.setWarningRepertory(posItem.getWarningRepertory());
                // posItemVo.setShelfLife(posItem.getShelfLife());
                LifeItemUnit itemUnit = itemUnitManager.getItemUnitById(itemTemplate.getUnitId());
                posItemVo.setItemUnitId(itemUnit.getId());
                posItemVo.setItemUnitName(itemUnit.getTitle());
            } else {
                posItemVo = convertToVo(posItem);
            }

        }
        return posItemVo;
    }

    public Map<String, Object> itemStorageVerification(PosItemVo posItemVo) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(posItemVo.getBarcode())) {
            map.put("result", "false");
            map.put("msg", "商品入库码不能为空!");
        } else if (posItemVo.getStockPrice() == 0L) {
            map.put("result", "false");
            map.put("msg", "商品进货价不能为空");
        } else if (posItemVo.getSalesPrice() == 0L) {
            map.put("result", "false");
            map.put("msg", "商品零售价不能为空");
        } else if (posItemVo.getRepertory().equals(0)) {
            map.put("result", "false");
            map.put("msg", "商品库存不能为空");
        } else if (posItemVo.getSalesPrice() < posItemVo.getStockPrice()) {
            map.put("result", "false");
            map.put("msg", "零售价格小于进货价哦");
        } else if (posItemVo.getPassportId() == null) {
            map.put("result", "false");
            map.put("msg", "passportId 参数不能为空!");
        } else if (posItemVo.getItemTypeId() == null) {

            map.put("result", "false");
            map.put("msg", "itemTypeId 参数不能为空!");
        } else if (posItemVo.getPosTypeId() == null) {

            map.put("result", "false");
            map.put("msg", "posTypeId 参数不能为空!");
        } else if (posItemVo.getItemUnitId() == null) {
            map.put("result", "false");
            map.put("msg", "itemUnitId 参数不能为空!");
        } else if (StringUtil.isEmpty(posItemVo.getItemName())) {
            map.put("result", "false");
            map.put("msg", "itemName 参数不能为空!");
        } else {
            map.put("result", "true");
        }


        return map;
    }

    public PosItem convertToClass(PosItemVo posItemVo, LifeItemTemplate lifeItemTemplate) {

        PosItem posItem = new PosItem();
        posItem.setBuyCount(0);
        //预警库存默认20
        if (posItemVo.getWarningRepertory() != null) {
            posItem.setWarningRepertory(posItemVo.getWarningRepertory());
        } else {
            posItem.setWarningRepertory(20);
        }

        posItem.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
        if (StringUtil.isEmpty(posItemVo.getIsDelete())) {
            posItem.setIsDelete("N");
        } else {
            posItem.setIsDelete(posItemVo.getIsDelete());
        }
        if (StringUtil.isEmpty(posItemVo.getIsShelve())) {
            posItem.setIsShelve("Y");
        } else {
            posItem.setIsShelve(posItemVo.getIsShelve());
        }
        if (posItemVo.getRepertory() != null) {
            posItem.setRepertory(posItemVo.getRepertory());
        }
        if (!StringUtil.isEmpty(posItemVo.getItemName())) {
            posItem.setItemName(posItemVo.getItemName());
        }
        if (posItemVo.getGeneratedDate() != null) {
            posItem.setGeneratedDate(posItemVo.getGeneratedDate());
        }
        if (lifeItemTemplate.getId() != null) {
            posItem.setItemTemplateId(lifeItemTemplate.getId());
        }
        if (!StringUtil.isEmpty(posItemVo.getBarcode())) {
            posItem.setBarcode(posItemVo.getBarcode());
        }
        if (posItemVo.getStockPrice() != 0.0) {
            posItem.setStockPrice((long) (posItemVo.getStockPrice() * 100));
        }
        if (posItemVo.getSalesPrice() != 0.0) {
            posItem.setSalesPrice((long) (posItemVo.getSalesPrice() * 100));
        }

        if (StringUtil.isEmpty(posItemVo.getItemImg())) {
            posItem.setItemImgUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
        } else {
            posItem.setItemImgUrl(posItemVo.getItemImg());
        }

        //保质期天
        if (posItemVo.getShelfLife() != null) {
            posItem.setShelfLife(posItemVo.getShelfLife());
        } //商家id，以后收银员账号查询关联商家
        if (posItemVo.getPassportId() != null) {
            posItem.setPassportId(posItemVo.getPassportId());
        }
        if (posItemVo.getPosTypeId() != null) {
            posItem.setPosTypeId(posItemVo.getPosTypeId());
        }
        if (posItemVo.getItemTypeId() != null) {
            posItem.setItemTypeId(posItemVo.getItemTypeId());
        }
        return posItem;
    }

    @Override
    public Map<String, Object> itemStorage(PosItemVo posItemVo) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(posItemVo.getBarcode())) {
            map.put("result", "false");
            map.put("msg", "barcode 参数不能为空!");
        } else if (posItemVo.getPassportId() == null) {
            map.put("result", "false");
            map.put("msg", "passportId 参数不能为空!");
        } else {

            //查询barcode是否有此模板，没有就新增，有就关联
            LifeItemTemplate itemTemplate = itemTemplateManager.getItemTemplateByBarcode(posItemVo.getBarcode());
            if (itemTemplate == null) {

                //-------------模板为空，参数必须要效验。。---------------------
                map = itemStorageVerification(posItemVo);
                String result = map.get("result").toString();
                if (result.equals("true")) {
                    //新增模板信息
                    //判断商品模板名字是否重复
                    PosItem posItem = new PosItem();
                    //新增模板并关联
                    //pos商品模板
                    itemTemplate = new LifeItemTemplate();
                    itemTemplate.setAscriptionType(1);
                    if (StringUtil.isEmpty(posItemVo.getItemImg())) {
                        itemTemplate.setImageUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
                    } else {
                        itemTemplate.setImageUrl(posItemVo.getItemImg());
                    }
                    itemTemplate.setBarcode(posItemVo.getBarcode());
                    itemTemplate.setCostPrice((long) posItemVo.getStockPrice() * 100);
                    itemTemplate.setDefaultPrice((long) posItemVo.getSalesPrice() * 100);
                    itemTemplate.setUnitId(posItemVo.getItemUnitId());
                    itemTemplate.setTypeId(posItemVo.getItemTypeId());
                    itemTemplate.setName(posItemVo.getItemName());
                    //添加模板信息
                    itemTemplateJpaRepository.save(itemTemplate);
                    //set 实体类
                    posItem = convertToClass(posItemVo, itemTemplate);
                    //添加商家商品信息
                    posItemJpaRepository.save(posItem);
                    map.put("result", "true");
                    map.put("msg", "商品入库成功!");
                    map.put("info", posItem);

                }

            } else {
                //关联模板信息如果商家也入库了此商品的话就可以进行商品的一个编辑
                //查询商家是否有入库此模板信息
                PosItem posItem = posItemManager.getPosItemByPassportIdAndItemTemplateId(posItemVo.getPassportId()
                        , itemTemplate.getId());
                if (posItem == null) { //商家没有关系此模板信息
                    //set 实体类
                    posItem = convertToClass(posItemVo, itemTemplate);
                    //添加商家商品信息
                    posItemJpaRepository.save(posItem);

                    map.put("result", "true");
                    map.put("msg", "商品入库成功!");
                    map.put("info", posItem);
                } else {
                    //商品入库为新增数量。
                    if (posItemVo.getRepertory() != null) {
                        int count = posItem.getRepertory() + posItemVo.getRepertory();
                        posItemVo.setRepertory(count);
                    }
                    map = editPosItem(posItemVo);
                }
            }

        }


        return map;
    }

    @Override
    public PosItem getPosItemById(Long id) {
        return posItemJpaRepository.getPosItemById(id);
    }

    @Override
    public List<PosItem> findAllBySearchTextPassportId(String searchText, Long passportId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        List<PosItem> posItems = null;
        if (pattern.matcher(searchText).matches() == true) {
            posItems = posItemJpaRepository
                    .findAllByBarcodeLikeAndPassportId("%" + searchText + "%", passportId);
        } else {
            posItems = posItemJpaRepository
                    .findAllByItemNameLikeAndPassportId("%" + searchText + "%", passportId);
        }
        return posItems;
    }

    @Override
    public Map<String, Object> editPosItem(PosItemVo posItemVo) {

        Map<String, Object> map = new HashMap<>();
        //查询商家是否有此商品信息

        PosItem posItem = posItemJpaRepository.getPosItemByPassportIdAndBarcode(posItemVo.getPassportId()
                , posItemVo.getBarcode());
        if (posItem == null) {
            map.put("result", "false");
            map.put("msg", "查询商家商品为空!");
        } else {
            if (!StringUtil.isEmpty(posItemVo.getIsDelete())) {
                posItem.setIsDelete(posItemVo.getIsDelete());
            }
            if (!StringUtil.isEmpty(posItemVo.getIsShelve())) {
                posItem.setIsShelve(posItemVo.getIsShelve());
            }
            if (posItemVo.getRepertory() != null) {
                posItem.setRepertory(posItemVo.getRepertory());
            }
            if (!StringUtil.isEmpty(posItemVo.getItemName())) {
                posItem.setItemName(posItemVo.getItemName());
            }
            if (posItemVo.getGeneratedDate() != null) {
                posItem.setGeneratedDate(posItemVo.getGeneratedDate());
            }
            if (posItemVo.getItemTemplateId() != null) {
                posItem.setItemTemplateId(posItemVo.getItemTemplateId());
            }
            if (!StringUtil.isEmpty(posItemVo.getBarcode())) {
                posItem.setBarcode(posItemVo.getBarcode());
            }
            if (posItemVo.getStockPrice() != 0.0) {
                posItem.setStockPrice((long) (posItemVo.getStockPrice() * 100));
            }
            if (posItemVo.getSalesPrice() != 0.0) {
                posItem.setSalesPrice((long) (posItemVo.getSalesPrice() * 100));
            }

            if (!StringUtil.isEmpty(posItemVo.getItemImg())) {
                posItem.setItemImgUrl(posItemVo.getItemImg());
            }
            if (posItemVo.getWarningRepertory() != null) {
                posItem.setWarningRepertory(posItemVo.getWarningRepertory());
            }
            //保质期天
            if (posItemVo.getShelfLife() != null) {
                posItem.setShelfLife(posItemVo.getShelfLife());
            } //商家id，以后收银员账号查询关联商家
            if (posItemVo.getPassportId() != null) {
                posItem.setPassportId(posItemVo.getPassportId());
            }
            if (posItemVo.getPosTypeId() != null) {
                posItem.setPosTypeId(posItemVo.getPosTypeId());
            }
            if (posItemVo.getItemTypeId() != null) {
                posItem.setItemTypeId(posItemVo.getItemTypeId());
            }
            //添加商家商品信息
            posItemJpaRepository.save(posItem);
            map.put("result", "true");
            map.put("msg", "商品编辑成功!!");
            map.put("info", posItem);

        }
        return map;
    }

    @Override
    public PosItemVo convertToVo(PosItem posItem) {
        LifeItemTemplate itemTemplate = itemTemplateManager.getItemTemplateById(posItem.getItemTemplateId());
        PosItemVo posItemVo = new PosItemVo();
        posItemVo.setId(posItem.getId());
        posItemVo.setPosTypeId(posItem.getItemTypeId());
        LifeItemType itemType = itemTypeManager.getItemTypeById(posItem.getItemTypeId());
        posItemVo.setPosTypeName(itemType.getTitle());
        posItemVo.setItemTemplateId(itemTemplate.getId());
        posItemVo.setItemName(posItem.getItemName());
        BigDecimal sMoney = new BigDecimal(posItem.getStockPrice());
        BigDecimal saMoney = new BigDecimal(posItem.getSalesPrice());
        BigDecimal a = new BigDecimal(100);
        Double sPrice = sMoney.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
        Double saPrice = saMoney.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
        posItemVo.setStockPrice(sPrice);
        posItemVo.setSalesPrice(saPrice);
        posItemVo.setItemTypeId(posItem.getItemTypeId());
        posItemVo.setBuyCount(posItem.getBuyCount());
        posItemVo.setCreateDate(posItem.getCreateTime());
        posItemVo.setBarcode(itemTemplate.getBarcode());
        posItemVo.setIsDelete(posItem.getIsDelete());
        posItemVo.setIsShelve(posItem.getIsShelve());
        posItemVo.setItemImg(posItem.getItemImgUrl());
        posItemVo.setRepertory(posItem.getRepertory());
        posItemVo.setWarningRepertory(posItem.getWarningRepertory());
        posItemVo.setShelfLife(posItem.getShelfLife());
        posItemVo.setPassportId(posItem.getPassportId());
        LifeItemUnit itemUnit = itemUnitManager.getItemUnitById(itemTemplate.getUnitId());
        posItemVo.setItemUnitId(itemUnit.getId());
        posItemVo.setItemUnitName(itemUnit.getTitle());
        posItemVo.setGeneratedDate(posItem.getGeneratedDate());
        return posItemVo;
    }

    @Override
    public List<PosItemVo> convertToVos(List<PosItem> posItems) {
        List<PosItemVo> posItemVos = new ArrayList<>();
        //商品模板ID
        List<Long> itemTemplateIdList = new ArrayList<>();
        for (PosItem posItem : posItems) {
            itemTemplateIdList.add(posItem.getItemTemplateId());
        }
        List<LifeItemTemplate> itemTemplateList = itemTemplateJpaRepository.findAll(itemTemplateIdList);
        List<Long> itemUnitIdList = new ArrayList<>();
        for (LifeItemTemplate itemTemplate : itemTemplateList) {
            itemUnitIdList.add(itemTemplate.getUnitId());
        }
        List<LifeItemUnit> itemUnits = itemUnitJpaRepository.findAll(itemUnitIdList);
        Map<Long, LifeItemUnit> itemUnitMap = new HashMap<>();
        for (LifeItemTemplate itemTemplate : itemTemplateList) {
            for (LifeItemUnit itemUnit : itemUnits) {
                if (itemTemplate.getUnitId().longValue() == itemUnit.getId()) {
                    itemUnitMap.put(itemTemplate.getId(), itemUnit);
                }
            }
        }
        //获取所有商品分类id
        List<Long> itemTypeIdList=new ArrayList<>();
        for(PosItem posItem : posItems){
            itemTypeIdList.add(posItem.getItemTypeId());
        }

        //获取所有商品分类
        List<LifeItemType> itemTypes=lifeItemTypeJpaRepository.findAll(itemTypeIdList);
        Map<Long, LifeItemType> itemTypeMap = new HashMap<>();
        for (PosItem posItem : posItems) {
            for (LifeItemType itemType : itemTypes) {
                if (posItem.getItemTypeId().longValue() == itemType.getId()) {
                    itemTypeMap.put(itemType.getId(), itemType);
                }
            }
        }
        for (PosItem posItem : posItems) {
            PosItemVo posItemVo = new PosItemVo();
            posItemVo.setId(posItem.getId());
            posItemVo.setPosTypeId(posItem.getItemTypeId());
            LifeItemType itemType = itemTypeMap.get(posItem.getItemTypeId());
            posItemVo.setPosTypeName(itemType.getTitle());
            posItemVo.setItemTemplateId(posItem.getItemTemplateId());
            posItemVo.setItemName(posItem.getItemName());
            BigDecimal sMoney = new BigDecimal(posItem.getStockPrice());
            BigDecimal saMoney = new BigDecimal(posItem.getSalesPrice());
            BigDecimal a = new BigDecimal(100);
            Double sPrice = sMoney.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
            Double saPrice = saMoney.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
            posItemVo.setStockPrice(sPrice);
            posItemVo.setSalesPrice(saPrice);
            posItemVo.setItemTypeId(posItem.getItemTypeId());
            posItemVo.setBuyCount(posItem.getBuyCount());
            posItemVo.setCreateDate(posItem.getCreateTime());
            posItemVo.setBarcode(posItem.getBarcode());
            posItemVo.setIsDelete(posItem.getIsDelete());
            posItemVo.setIsShelve(posItem.getIsShelve());
            posItemVo.setItemImg(posItem.getItemImgUrl());
            posItemVo.setRepertory(posItem.getRepertory());
            posItemVo.setWarningRepertory(posItem.getWarningRepertory());
            posItemVo.setShelfLife(posItem.getShelfLife());
            posItemVo.setPassportId(posItem.getPassportId());
            LifeItemUnit itemUnit = itemUnitMap.get(posItem.getItemTemplateId());
            String title = "";
            Long unitId=null;
            if (itemUnit != null) {
                title = itemUnit.getTitle();
                unitId=itemUnit.getId();
            }

            posItemVo.setItemUnitId(unitId);
            posItemVo.setItemUnitName(title);
            posItemVo.setGeneratedDate(posItem.getGeneratedDate());
            posItemVos.add(posItemVo);
        }

        return posItemVos;

    }

    @Transactional
    public void offsetItemRepertory(Map<Long, Integer> itemIdMaps) {
        if (itemIdMaps == null || itemIdMaps.size() == 0) {
            throw new PosNullPointerException("所需变更的商品库存为空！");
        }
        List<PosItem> posItems = posItemJpaRepository.findAll(itemIdMaps.keySet());
        for (Long key : itemIdMaps.keySet()) {
            for (PosItem posItem : posItems) {
                if (key.longValue() == posItem.getId().longValue()) {
                    posItem.setRepertory(posItem.getRepertory() - itemIdMaps.get(key));
                    break;
                }
            }
        }
        posItemJpaRepository.save(posItems);
    }

    @Override
    public BasicResult getItemUnitAndType(String passportId) {
        if (StringUtil.isEmpty(passportId)) {

            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        } else {

            logger.info("获取所有商品单位规格。。");
            //规格
            List<LifeItemUnit> itemUnits = itemUnitJpaRepository.findAll();
            //商品分类
            logger.info("获取所有商品分类");
            List<PosType> posTypes = posTypeJpaRepository.getAllByPassportId(Long.parseLong(passportId));
            List<Long> itemTypeIds = new ArrayList<>();
            for (PosType posType : posTypes) {
                itemTypeIds.add(posType.getItemTypeId());
            }
            List<Map<String, Object>> itemTypeCountMapList = posItemMapper
                    .getCountByItemType(Long.parseLong(passportId), itemTypeIds);
            Map<Long, Integer> itemTypeCountMap = new HashMap<>();
            for (Map<String, Object> map : itemTypeCountMapList) {
                itemTypeCountMap.put(Long.parseLong(map.get("item_type_id").toString())
                        , Integer.parseInt(map.get("count").toString()));
            }
            List<PosTypeVo> posTypeVos = new ArrayList<>();
            if (posTypes.size() > 0) {
                for (PosType posType : posTypes) {
                    PosTypeVo posTypeVo = new PosTypeVo();
                    posTypeVo.setId(posType.getId());
                    posTypeVo.setTitle(posType.getItemTypeTitle());
                    posTypeVo.setItemTypeId(posType.getItemTypeId());
                    Integer count = itemTypeCountMap.get(posType.getItemTypeId());
                    if (count == null) {
                        count = 0;
                    }
                    posTypeVo.setTypeCount(count);
                    posTypeVos.add(posTypeVo);
                }
            }
            JSONObject jo = new JSONObject();
            jo.put("itemUnitList", itemUnits);
            jo.put("itemTypes", posTypeVos);
            BasicResult basicResult = BasicResult.createSuccessResult();
            basicResult.setResponse(jo);
            return basicResult;
        }
    }

    @Override
    public BasicResult getItemByPassportId(String passportId, String itemTypeId) {
        if (StringUtil.isEmpty(passportId)) {

            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        } else {
            List<PosItem> posItems = null;
            if (StringUtil.isEmpty(itemTypeId)) {
                posItems = posItemManager.getAllByPassportId(Long.parseLong(passportId));
            } else {
                posItems = posItemManager.getAllByPassportIdAndItemTypeId(Long.parseLong(passportId)
                        , Long.parseLong(itemTypeId));

            }

            if (posItems.size() == 0) {

                return BasicResult.createFailResult("没有商品信息!");

            } else {
                List<PosItemVo> posItemVos = posItemManager.convertToVos(posItems);
                return BasicResult.createSuccessResultWithDatas("获取商家商品信息成功!", posItemVos);
            }


        }
    }

    @Override
    public BasicResult getListBySearchText(String searchText, Long passportId) {
        List<PosItemVo> posItemVos = posItemMapper.getListBySearchText(searchText, passportId);
        return BasicResult.createSuccessResultWithDatas("搜索成功!", posItemVos);
    }
}
