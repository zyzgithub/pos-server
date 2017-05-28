package com.dianba.pos.menu.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.po.ItemTemplate;
import com.dianba.pos.item.po.ItemType;
import com.dianba.pos.item.po.ItemUnit;
import com.dianba.pos.menu.config.MenuUrlConstant;
import com.dianba.pos.menu.po.PosItem;
import com.dianba.pos.menu.po.PosType;
import com.dianba.pos.menu.vo.PosItemVo;
import com.dianba.pos.menu.vo.PosTypeVo;
import javafx.geometry.Pos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Controller
@RequestMapping(MenuUrlConstant.POS_ITEM_URL)
public class PosItemController extends BaseController {

    private static Logger logger = LogManager.getLogger(PosItemController.class);
    /**
     * 获取商品分类以及商品信息并显示各类商品数量
     *
     * @param passportId
     * @return
     */
    @ResponseBody
    @RequestMapping("getItemByPassportId")
    public BasicResult getItemByPassportId(String passportId, String itemTypeId) {

        if(StringUtil.isEmpty(passportId)){

            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        }else {
            List<PosItem> posItems = null;
            if(StringUtil.isEmpty(itemTypeId)){
                posItems=posItemManager.getAllByPassportId(Long.parseLong(passportId));
            }else {
                posItems=posItemManager.getAllByPassportIdAndItemTypeId(Long.parseLong(passportId),Long.parseLong(itemTypeId));

            }


            List<PosItemVo> posItemVos=new ArrayList<>();
            for(PosItem posItem:posItems){

                ItemTemplate itemTemplate=itemTemplateManager.getItemTemplateById(posItem.getItemTemplateId());
                PosItemVo posItemVo=new PosItemVo();
                posItemVo.setId(posItem.getId());
                posItemVo.setPosTypeId(posItem.getItemTypeId());
                ItemType itemType=itemTypeManager.getItemTypeById(posItem.getItemTypeId());
                posItemVo.setPosTypeName(itemType.getTitle());
                posItemVo.setItemTemplateId(itemTemplate.getId());
                posItemVo.setItemName(itemTemplate.getName());
                posItemVo.setStockPrice(posItem.getStockPrice());
                posItemVo.setSalesPrice(posItem.getSalesPrice());
                posItemVo.setBuyCount(posItem.getBuyCount());
                posItemVo.setCreateDate(posItem.getCreateTime());
                posItemVo.setBarcode(itemTemplate.getBarcode());
                posItemVo.setIsDelete(posItem.getIsDelete());
                posItemVo.setIsShelve(posItem.getIsShelve());
                posItemVo.setItem_img(itemTemplate.getImageUrl());
                posItemVo.setRepertory(posItem.getRepertory());
                posItemVo.setWarningRepertory(posItem.getWarningRepertory());
                posItemVo.setShelfLife(posItem.getShelfLife());
                ItemUnit itemUnit=itemUnitManager.getItemUnitById(itemTemplate.getUnitId());
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

        if(StringUtil.isEmpty(passportId)){

            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        }else {

            //规格
            List<ItemUnit> itemUnits = itemUnitJpaRepository.findAll();
            //商品分类
            List<PosType> posTypes = posTypeManager.getAllByPassportId(Long.parseLong(passportId));

            List<PosTypeVo> posTypeVos = new ArrayList<>();
            for (PosType posType : posTypes) {
                ItemType itemType = itemTypeManager.getItemTypeById(posType.getItemTypeId());
                PosTypeVo posTypeVo = new PosTypeVo();
                posTypeVo.setId(itemType.getId());
                posTypeVo.setTitle(itemType.getTitle());
                List<PosItem> posItems = posItemManager.getAllByPosTypeId(posType.getId());
                posTypeVo.setType_count(posItems.size());
                posTypeVos.add(posTypeVo);
            }

            JSONObject jo = new JSONObject();
            jo.put("itemUnitList", itemUnits);
            jo.put("itemTypes", posTypeVos);


            return BasicResult.createSuccessResultWithDatas("请求成功!", jo);

        }

    }

    /**
     * 新增商家商品分类
     *
     * @param passportId
     * @param title
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addPosType")
    public BasicResult addPosType(String passportId, String title) {

        if(StringUtil.isEmpty(passportId)||StringUtil.isEmpty(title)){

            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        }else {

            ItemType itemType = new ItemType();
            itemType.setTitle(title);
            itemType.setAscriptionType(1);
            itemTypeJpaRepository.save(itemType);

            PosType posType = new PosType();
            posType.setPassportId(Long.parseLong(passportId));
            posType.setItemTypeId(itemType.getId());

            posTypeJpaRepository.save(posType);
            return BasicResult.createSuccessResult();
        }


    }


    /***
     * 商品入库
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "itemStorage")
    public BasicResult itemStorage(PosItemVo posItemVo) {

       Map<String,Object> map=posItemManager.itemStorage(posItemVo);

       String result=map.get("result").toString();

       String msg=map.get("msg").toString();

       if(result.equals("false")){

           return BasicResult.createFailResult(msg);
       }else{
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
              JSONObject jsonObject=new JSONObject();
              PosItemVo posItemVo=posItemManager.getItemByBarcode(barcode,passportId);
              if(posItemVo!=null)
              {
                  jsonObject.put("itemInfo",posItemVo);
              }
              return BasicResult.createSuccessResult("获取信息成功!",jsonObject);
          }

    }


    /**
     * 添加商家商品
     */
    @ResponseBody
    @RequestMapping("addPosItem")
    public BasicResult addPosItem(PosItemVo posItemVo) {

       ItemTemplate itemTemplate=itemTemplateManager.getItemTemplateByName(posItemVo.getItemName());
       if(itemTemplate!=null){

           return BasicResult.createFailResult("商品名字重复了~😬~");
       }else {

           itemTemplate=new ItemTemplate();
           itemTemplate.setName(posItemVo.getItemName());
           itemTemplate.setImageUrl(posItemVo.getItem_img());
           //销售单价
           itemTemplate.setDefaultPrice(posItemVo.getSalesPrice());
           itemTemplateJpaRepository.save(itemTemplate);
           PosItem posItem=new PosItem();
           posItem.setItemName(posItemVo.getItemName());

       }
        return BasicResult.createSuccessResult();
    }

    @ResponseBody
    @RequestMapping("editPosItem")
    public BasicResult editPosItem(PosItemVo posItemVo){



        return BasicResult.createSuccessResult();

    }

    /**
     * 商品上下架
     * @return
     */
    @ResponseBody
    @RequestMapping("itemIsShelve")
    public BasicResult itemIsShelve(PosItemVo posItemVo){

        PosItem posItem=posItemManager.getPosItemById(posItemVo.getId());

        if(posItem==null){
            return BasicResult.createFailResult("数据出现异常,请联系管理员!");
        }else if(posItem.getPassportId()==posItemVo.getPassportId()){
            posItem.setItemName(posItemVo.getItemName());
            posItem.setStockPrice(posItemVo.getStockPrice());
            posItem.setSalesPrice(posItemVo.getSalesPrice());
            posItemJpaRepository.save(posItem);
            return BasicResult.createSuccessResult();
        }else {
            return BasicResult.createFailResult("数据出现异常,请联系管理员!");
        }
    }

    @ResponseBody
    @RequestMapping("getListBySearchText")
    public BasicResult getListBySearchText(String searchText,Long passportId){

        List<PosItem> posItems=posItemManager.findAllBySearchTextPassportId(searchText,passportId);

        return BasicResult.createSuccessResultWithDatas("搜索成功!",posItems);

    }

}
