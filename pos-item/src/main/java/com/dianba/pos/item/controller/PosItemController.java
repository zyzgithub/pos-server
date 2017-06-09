package com.dianba.pos.item.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.config.MenuUrlConstant;
import com.dianba.pos.item.po.*;
import com.dianba.pos.item.repository.PosItemJpaRepository;
import com.dianba.pos.item.service.*;
import com.dianba.pos.item.vo.PosItemVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private LifeItemTypeManager itemTypeManager;
    @Autowired
    private LifeItemUnitManager itemUnitManager;
    @Autowired
    private PosItemJpaRepository posItemJpaRepository;
    @Autowired
    private LifeItemTemplateManager itemTemplateManager;
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

        return posItemManager.getItemByPassportId(passportId, itemTypeId);

    }

    /**
     * 获取商品规格，以及商家商品分类
     *
     * @return 通行证id
     */
    @ResponseBody
    @RequestMapping(value = "getItemUnitAndType")
    public BasicResult getItemUnitAndType(String passportId) {

        return posItemManager.getItemUnitAndType(passportId);
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
            PosItem posItem = (PosItem) map.get("info");
            PosItemVo posItemVo1 = posItemManager.convertToVo(posItem);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(posItemVo1);
            return BasicResult.createSuccessResult(msg, jsonObject);
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
            JSONObject jsonObject = null;
            List<PosItemVo> posItemVos=new ArrayList<>();
            PosItemVo posItemVo = posItemManager.getItemByBarcode(barcode, passportId);
            if (posItemVo != null) {
                posItemVos.add(posItemVo);
            }
            JSONArray jsonArray=(JSONArray) JSONArray.toJSON(posItemVos);
            return BasicResult.createSuccessResultWithDatas("获取信息成功!", jsonArray);
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
        if (posItem != null && posItem.getPassportId().equals(passportId)) {
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
            PosItem posItem = (PosItem) map.get("info");
            PosItemVo posItemVo1 = posItemManager.convertToVo(posItem);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(posItemVo1);
            return BasicResult.createSuccessResult(msg, jsonObject);
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
        } else if (posItem.getPassportId() .equals(posItemVo.getPassportId())) {
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

       return posItemManager.getListBySearchText(searchText,passportId);

    }


}
