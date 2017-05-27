package com.dianba.pos.menu.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.po.ItemTemplate;
import com.dianba.pos.item.po.ItemType;
import com.dianba.pos.item.po.ItemUnit;
import com.dianba.pos.menu.config.MenuUrlConstant;
import com.dianba.pos.menu.po.PosItem;
import com.dianba.pos.menu.po.PosType;
import com.dianba.pos.menu.vo.PosItemVo;
import com.dianba.pos.menu.vo.PosTypeVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Controller
@RequestMapping(MenuUrlConstant.POS_ITEM_URL)
public class PosItemController extends BaseController {


    /**
     * 获取商品分类以及商品信息并显示各类商品数量
     *
     * @param passportId
     * @return
     */
    @ResponseBody
    @RequestMapping("getItemByPassportId")
    public BasicResult getItemByPassportId(Long passportId, Long itemTypeId) {

        List<PosItem> posItems = posItemManager.getAllByPassportIdAndItemTypeId(passportId, itemTypeId);

        return BasicResult.createSuccessResultWithDatas("获取商家商品信息成功!", posItems);
    }

    /**
     * 获取商品规格，以及商家商品分类
     *
     * @return 通行证id
     */
    @ResponseBody
    @RequestMapping(value = "getItemUnitAndType")
    public BasicResult getItemUnitAndType(Long passportId) {

        //规格
        List<ItemUnit> itemUnits = itemUnitJpaRepository.findAll();
        //商品分类
        List<PosType> posTypes = posTypeManager.getAllByPassportId(passportId);

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

    /**
     * 新增商家商品分类
     *
     * @param passportId
     * @param title
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addPosType")
    public AjaxJson addPosType(Long passportId, String title) {

        AjaxJson ajaxJson = new AjaxJson();

        ItemType itemType = new ItemType();

        itemType.setTitle(title);
        itemType.setAscriptionType(1);
        itemTypeJpaRepository.save(itemType);

        PosType posType = new PosType();
        posType.setPassportId(passportId);
        posType.setItemTypeId(itemType.getId());

        posTypeJpaRepository.save(posType);
        return ajaxJson;

    }


    /***
     * 商品入库
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "itemStorage")
    public AjaxJson itemStorage() {

        AjaxJson ajaxJson = new AjaxJson();

        return ajaxJson;

    }

    /**
     * 根据商品code码获取商家入库信息
     *
     * @param barcode
     * @return
     */
    @ResponseBody
    @RequestMapping("getPosItemInfoByBarcode")
    public AjaxJson getPosItemInfoByBarcode(String barcode, String type, String passportId) {

        AjaxJson ajaxJson = new AjaxJson();

        if (StringUtil.isEmpty(barcode) || StringUtil.isEmpty(type)) {
            ajaxJson.setSuccess(false);
            ajaxJson.setStateCode(AjaxJson.STATE_CODE_FAIL);
            ajaxJson.setMsg("参数输入有误!");
        } else {

            ItemTemplate itemTemplate = itemTemplateManager.getItemTemplateByBarcode(barcode);

            if (itemTemplate != null) { //商品模板有此商品信息
                Long userId = Long.parseLong(passportId);
                PosItemVo posItemVo = new PosItemVo();
                PosItem posItem = posItemManager.getPosItemByPassportIdAndItemTemplateId(userId, itemTemplate.getId());
                if (type.equals("1")) { //商品入库模板搜索
                    if (posItem == null) {

                        posItemVo.setId(itemTemplate.getId());
                        posItemVo.setItemName(itemTemplate.getName());
                        posItemVo.setBarcode(itemTemplate.getBarcode());
                        posItemVo.setStockPrice(itemTemplate.getCostPrice());
                        posItemVo.setSalesPrice(itemTemplate.getDefaultPrice());
                    } else {
                        //商家有此商品返回商家商品信息
                        posItemVo.setId(itemTemplate.getId());
                        posItemVo.setItemName(posItem.getItemName());
                        posItemVo.setBarcode(itemTemplate.getBarcode());
                        posItemVo.setStockPrice(posItem.getStockPrice());
                        posItemVo.setSalesPrice(posItem.getSalesPrice());
                    }

                }

                ajaxJson.setObj(posItemVo);
            }
        }


        return ajaxJson;

    }


    /**
     * 添加商家商品
     */
    @ResponseBody
    @RequestMapping("addPosItem")
    public BasicResult addPosItem(ItemTemplate itemTemplate) {

        return BasicResult.createSuccessResult();
    }

}
