package com.dianba.pos.menu.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.po.ItemTemplate;
import com.dianba.pos.item.po.ItemType;
import com.dianba.pos.item.po.ItemUnit;
import com.dianba.pos.item.repository.ItemUnitJpaRepository;
import com.dianba.pos.item.service.ItemTemplateManager;
import com.dianba.pos.item.service.ItemTypeManager;
import com.dianba.pos.item.service.ItemUnitManager;
import com.dianba.pos.menu.config.MenuUrlConstant;
import com.dianba.pos.menu.po.PosItem;
import com.dianba.pos.menu.po.PosType;
import com.dianba.pos.menu.repository.PosItemJpaRepository;
import com.dianba.pos.menu.service.PosItemManager;
import com.dianba.pos.menu.service.PosTypeManager;
import com.dianba.pos.menu.vo.PosItemVo;
import com.dianba.pos.menu.vo.PosTypeVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Controller
@RequestMapping(MenuUrlConstant.POS_ITEM_URL)
public class PosItemController {

    private static Logger logger = LogManager.getLogger(PosItemController.class);

    @Autowired
    private ItemTypeManager itemTypeManager;
    @Autowired
    private ItemUnitManager itemUnitManager;
    @Autowired
    private ItemUnitJpaRepository itemUnitJpaRepository;
    @Autowired
    private PosItemJpaRepository posItemJpaRepository;
    @Autowired
    private ItemTemplateManager itemTemplateManager;
    @Autowired
    private PosTypeManager posTypeManager;
    @Autowired
    private PosItemManager posItemManager;

    /**
     * 获取商品分类以及商品信息并显示各类商品数量
     *
     * @param passportId
     * @return
     */
    @ResponseBody
    @RequestMapping("getItemByPassportId")
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

            List<PosItemVo> posItemVos = new ArrayList<>();
            for (PosItem posItem : posItems) {

                ItemTemplate itemTemplate = itemTemplateManager.getItemTemplateById(posItem.getItemTemplateId());
                PosItemVo posItemVo = new PosItemVo();
                posItemVo.setId(posItem.getId());
                posItemVo.setPosTypeId(posItem.getItemTypeId());
                ItemType itemType = itemTypeManager.getItemTypeById(posItem.getItemTypeId());
                posItemVo.setPosTypeName(itemType.getTitle());
                posItemVo.setItemTemplateId(itemTemplate.getId());
                posItemVo.setItemName(itemTemplate.getName());
                BigDecimal sMoney = new BigDecimal(posItem.getStockPrice());
                BigDecimal saMoney = new BigDecimal(posItem.getSalesPrice());
                BigDecimal a = new BigDecimal(100);
                Double sPrice = sMoney.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
                Double saPrice = saMoney.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
                posItemVo.setStockPrice(sPrice);
                posItemVo.setSalesPrice(saPrice);
                posItemVo.setBuyCount(posItem.getBuyCount());
                posItemVo.setCreateDate(posItem.getCreateTime());
                posItemVo.setBarcode(itemTemplate.getBarcode());
                posItemVo.setIsDelete(posItem.getIsDelete());
                posItemVo.setIsShelve(posItem.getIsShelve());
                posItemVo.setItemImg(itemTemplate.getImageUrl());
                posItemVo.setRepertory(posItem.getRepertory());
                posItemVo.setWarningRepertory(posItem.getWarningRepertory());
                posItemVo.setShelfLife(posItem.getShelfLife());
                ItemUnit itemUnit = itemUnitManager.getItemUnitById(itemTemplate.getUnitId());
                posItemVo.setItemUnitId(itemUnit.getId());
                posItemVo.setItemUnitName(itemUnit.getTitle());
                posItemVos.add(posItemVo);
            }
            return BasicResult.createSuccessResultWithDatas("获取商家商品信息成功!", posItemVos);
        }

    }

    /**
     * 获取商品规格，以及商家商品分类
     *
     * @return 通行证id
     */
    @ResponseBody
    @RequestMapping(value = "getItemUnitAndType")
    public BasicResult getItemUnitAndType(String passportId) {

        if (StringUtil.isEmpty(passportId)) {

            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        } else {

            //规格
            List<ItemUnit> itemUnits = itemUnitJpaRepository.findAll();
            //商品分类
            List<PosType> posTypes = posTypeManager.getAllByPassportId(Long.parseLong(passportId));

            List<PosTypeVo> posTypeVos = new ArrayList<>();
            for (PosType posType : posTypes) {
                ItemType itemType = itemTypeManager.getItemTypeById(posType.getItemTypeId());
                PosTypeVo posTypeVo = new PosTypeVo();
                posTypeVo.setId(posType.getId());
                posTypeVo.setTitle(itemType.getTitle());
                List<PosItem> posItems = posItemManager.getAllByPosTypeId(posType.getId());
                posTypeVo.setTypeCount(posItems.size());
                posTypeVos.add(posTypeVo);
            }

            JSONObject jo = new JSONObject();
            jo.put("itemUnitList", itemUnits);
            jo.put("itemTypes", posTypeVos);


            return BasicResult.createSuccessResultWithDatas("请求成功!", jo);

        }

    }


    /***
     * 商品入库
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "itemStorage")
    public BasicResult itemStorage(PosItemVo posItemVo) {

        Map<String, Object> map = posItemManager.itemStorage(posItemVo);

        String result = map.get("result").toString();

        String msg = map.get("msg").toString();

        if (result.equals("false")) {

            return BasicResult.createFailResult(msg);
        } else {
            return BasicResult.createSuccessResult(msg);
        }

    }

    /**
     * 根据商品code码获取商家入库信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("getPosItemInfoByBarcode")
    public BasicResult getPosItemInfoBySearch(String barcode, String passportId) {

        if (StringUtil.isEmpty(barcode) || StringUtil.isEmpty(passportId)) {
            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        } else {
            JSONObject jsonObject = new JSONObject();
            PosItemVo posItemVo = posItemManager.getItemByBarcode(barcode, passportId);
            if (posItemVo != null) {
                jsonObject.put("itemInfo", posItemVo);
            }
            return BasicResult.createSuccessResult("获取信息成功!", jsonObject);
        }

    }


    /**
     * 删除商家商品信息
     *
     * @param posItemId
     * @param passportId
     * @return
     */
    @ResponseBody
    @RequestMapping("deletePosItem")
    public BasicResult deletePosItem(Long posItemId, Long passportId) {

        PosItem posItem = posItemManager.getPosItemById(posItemId);
        if (posItem != null && posItem.getPassportId() == passportId) {
            //删除商品
            posItemJpaRepository.delete(posItem);

            return BasicResult.createSuccessResult("删除商家商品成功!");

        } else {
            return BasicResult.createFailResult("没有此商家商品信息!");

        }

    }

    @ResponseBody
    @RequestMapping("editPosItem")
    public BasicResult editPosItem(PosItemVo posItemVo) {

        posItemManager.editPosItem(posItemVo);

        Map<String, Object> map = posItemManager.editPosItem(posItemVo);

        String result = map.get("result").toString();

        String msg = map.get("msg").toString();

        if (result.equals("false")) {

            return BasicResult.createFailResult(msg);
        } else {
            return BasicResult.createSuccessResult(msg);
        }


    }

    /**
     * 商品上下架
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("itemIsShelve")
    public BasicResult itemIsShelve(PosItemVo posItemVo) {

        logger.info("====================商品上下架============================");
        PosItem posItem = posItemManager.getPosItemById(posItemVo.getId());
        logger.info("商家id:" + posItemVo.getPassportId());
        logger.info("商家商品id:" + posItemVo.getId());
        if (posItem == null) {

            logger.info("=======================没有此商品信息=================================");
            return BasicResult.createFailResult("数据出现异常,请联系管理员!");
        } else if (posItem.getPassportId() == posItemVo.getPassportId()) {
            logger.info("商品isShelve:" + posItemVo.getIsShelve());
            posItem.setIsShelve(posItemVo.getIsShelve());
            if (!StringUtil.isEmpty(posItemVo.getItemName())) {
                posItem.setItemName(posItemVo.getItemName());
            }
            if (posItemVo.getStockPrice() != 0.0) {
                posItem.setStockPrice((long) posItemVo.getStockPrice() * 100);
            }
            if (posItemVo.getSalesPrice() != 0.0) {
                posItem.setSalesPrice((long) posItemVo.getSalesPrice() * 100);

            }
            if (posItemVo.getRepertory() != null) {

                posItem.setRepertory(posItemVo.getRepertory());
            }

            posItemJpaRepository.save(posItem);
            return BasicResult.createSuccessResult();
        } else {
            return BasicResult.createFailResult("数据出现异常,请联系管理员!");

        }
    }


    @ResponseBody
    @RequestMapping("getListBySearchText")
    public BasicResult getListBySearchText(String searchText, Long passportId) {

        List<PosItem> posItems = posItemManager.findAllBySearchTextPassportId(searchText, passportId);

        return BasicResult.createSuccessResultWithDatas("搜索成功!", posItems);

    }


}
