package com.dianba.pos.item.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.po.LifeItemTemplate;
import com.dianba.pos.item.po.LifeItemType;
import com.dianba.pos.item.po.LifeItemUnit;
import com.dianba.pos.item.repository.LifeItemTemplateJpaRepository;
import com.dianba.pos.item.repository.PosItemJpaRepository;
import com.dianba.pos.item.service.LifeItemTemplateManager;
import com.dianba.pos.item.service.LifeItemTypeManager;
import com.dianba.pos.item.service.LifeItemUnitManager;
import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.service.PosItemManager;
import com.dianba.pos.item.vo.PosItemVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private Logger logger= LogManager.getLogger(DefaultPosItemManager.class);
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

            posItemVo=new PosItemVo();
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
                posItemVo= convertToVo(posItem);
            }

        }
        return posItemVo;
    }


    @Override
    public Map<String, Object> itemStorage(PosItemVo posItemVo) {

        Map<String, Object> map = new HashMap<>();
        if(StringUtil.isEmpty(posItemVo.getBarcode())){

            map.put("result", "false");
            map.put("msg", "商品入库码不能为空!");
        }else {

            if(posItemVo.getId()==null){

                //查询barcode是否有此模板，没有就新增，有就关联
                LifeItemTemplate itemTemplate = itemTemplateManager.getItemTemplateByBarcode(posItemVo.getBarcode());

                if (itemTemplate == null) { //新增模板信息
                    //判断商品模板名字是否重复
                    PosItem posItem = new PosItem();
                    itemTemplate = itemTemplateManager.getItemTemplateByName(posItemVo.getItemName());
                    if (itemTemplate != null) {
                        map.put("result", "false");
                        map.put("msg", "商品名字重复了~😬~");
                    } else if (posItemVo.getStockPrice() == 0L) {
                        map.put("result", "false");
                        map.put("msg", "商品进货价不能为空~😬~");

                    } else if (posItemVo.getSalesPrice() == 0L) {
                        map.put("result", "false");
                        map.put("msg", "商品零售价不能为空~😬~");
                    } else if (posItemVo.getRepertory().equals(0)) {
                        map.put("result", "false");
                        map.put("msg", "商品库存不能为空~😬~");
                    } else if (posItemVo.getSalesPrice() < posItemVo.getStockPrice()) {
                        map.put("result", "false");
                        map.put("msg", "零售价格小于进货价哦~😬~");
                    }
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
                    itemTemplate.setName(posItemVo.getItemName());
                    //添加模板信息
                    itemTemplateJpaRepository.save(itemTemplate);

                    posItem.setBuyCount(0);
                    posItem.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));


                    posItem.setStockPrice((long) (posItemVo.getStockPrice() * 100));
                    posItem.setSalesPrice((long) (posItemVo.getSalesPrice() * 100));
                    posItem.setRepertory(posItemVo.getRepertory());
                    //预警库存默认20
                    posItem.setWarningRepertory(20);
                    posItem.setItemName(posItemVo.getItemName());
                    posItem.setGeneratedDate(posItemVo.getGeneratedDate());
                    posItem.setItemTemplateId(itemTemplate.getId());
                    if (StringUtil.isEmpty(posItemVo.getItemImg())) {
                        posItem.setItemImgUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
                    } else {
                        posItem.setItemImgUrl(posItemVo.getItemImg());
                    }

                    //保质期天
                    if(posItemVo.getShelfLife()!=null) {
                        posItem.setShelfLife(posItemVo.getShelfLife());
                    }
                    //商家id，以后收银员账号查询关联商家
                    posItem.setPassportId(posItemVo.getPassportId());
                    posItem.setPosTypeId(posItemVo.getPosTypeId());
                    posItem.setItemTypeId(posItemVo.getItemTypeId());
                    posItem.setBarcode(posItemVo.getBarcode());
                    //添加商家商品信息
                    posItemJpaRepository.save(posItem);
                    map.put("result", "true");
                    map.put("msg", "商品入库成功!");
                    map.put("info",posItem);

                } else { //关联模板信息如果商家也入库了此商品的话就可以进行商品的一个编辑

                    //查询商家是否有入库此模板信息
                    PosItem posItem = posItemManager.getPosItemByPassportIdAndItemTemplateId(posItemVo.getPassportId()
                            , itemTemplate.getId());
                    if (posItem == null) { //商家没有关系此模板信息
                        posItem = new PosItem();
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
                        posItem.setBuyCount(0);
                        posItem.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        posItem.setStockPrice((long) (posItemVo.getStockPrice() * 100));
                        posItem.setSalesPrice((long) (posItemVo.getSalesPrice() * 100));
                        posItem.setRepertory(posItemVo.getRepertory());
                        //预警库存默认20
                        posItem.setWarningRepertory(20);
                        posItem.setItemName(posItemVo.getItemName());
                        posItem.setItemTemplateId(itemTemplate.getId());
                        if (StringUtil.isEmpty(posItemVo.getItemImg())) {
                            posItem.setItemImgUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
                        } else {
                            posItem.setItemImgUrl(posItemVo.getItemImg());
                        }
                        //保质期天
                        if(posItemVo.getShelfLife()!=null) {
                            posItem.setShelfLife(posItemVo.getShelfLife());
                        }
                        //商家id，以后收银员账号查询关联商家
                        posItem.setPassportId(posItemVo.getPassportId());
                        posItem.setPosTypeId(posItemVo.getPosTypeId());
                        posItem.setItemTypeId(posItemVo.getItemTypeId());
                        posItem.setBarcode(posItemVo.getBarcode());
                        posItem.setGeneratedDate(posItemVo.getGeneratedDate());
                        //添加商家商品信息
                        posItemJpaRepository.save(posItem);

                        map.put("result", "true");
                        map.put("msg", "商品入库成功!");
                        map.put("info",posItem);
                    } else {
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
                        posItem.setBuyCount(0);
                        posItem.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        posItem.setStockPrice((long)(posItemVo.getStockPrice() * 100));
                        posItem.setSalesPrice((long) (posItemVo.getSalesPrice() * 100));
                        //库存为添加
                        Integer rep = posItem.getRepertory() + posItemVo.getRepertory();
                        posItem.setRepertory(rep);
                        //预警库存默认20
                        posItem.setWarningRepertory(20);
                        posItem.setItemName(posItemVo.getItemName());
                        posItem.setItemTemplateId(itemTemplate.getId());
                        if (StringUtil.isEmpty(posItemVo.getItemImg())) {
                            posItem.setItemImgUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
                        } else {
                            posItem.setItemImgUrl(posItemVo.getItemImg());
                        }
                        //保质期天
                        if(posItemVo.getShelfLife()!=null) {
                            posItem.setShelfLife(posItemVo.getShelfLife());
                        }
                        //商家id，以后收银员账号查询关联商家
                        posItem.setPassportId(posItemVo.getPassportId());
                        posItem.setPosTypeId(posItemVo.getPosTypeId());
                        posItem.setItemTypeId(posItemVo.getItemTypeId());
                        posItem.setBarcode(posItemVo.getBarcode());
                        posItem.setGeneratedDate(posItemVo.getGeneratedDate());
                        //添加商家商品信息
                        posItemJpaRepository.save(posItem);
                        map.put("result", "true");
                        map.put("msg", "商品入库成功!");
                        map.put("info",posItem);

                    }

                }
            }else {

                map= editPosItem(posItemVo);
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

        PosItem posItem = posItemManager.getPosItemById(posItemVo.getId());
        if (posItem == null) {
            map.put("result", "false");
            map.put("msg", "查询信息为空!");
        } else {
            posItem.setStockPrice((long) posItemVo.getStockPrice() * 100);
            posItem.setSalesPrice((long) posItemVo.getSalesPrice() * 100);
            if(posItemVo.getRepertory()!=null){
                //库存为添加
                Integer rep = posItemVo.getRepertory();
                posItem.setRepertory(rep);
            }
            //预警库存默认20
            //posItem.setWarningRepertory(20);
            if(StringUtil.isEmpty(posItemVo.getItemName())){
                posItem.setItemName(posItemVo.getItemName());
            }

            if (StringUtil.isEmpty(posItemVo.getItemImg())) {
                posItem.setItemImgUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
            } else {
                posItem.setItemImgUrl(posItemVo.getItemImg());
            }
            //保质期天
            if(posItemVo.getShelfLife()!=null){
                posItem.setShelfLife(posItemVo.getShelfLife());
            }
            if(StringUtil.isEmpty(posItemVo.getIsShelve())){
                //编辑上下架信息
                posItem.setIsShelve(posItemVo.getIsShelve());
            }
            //添加商家商品信息
            posItemJpaRepository.save(posItem);
            map.put("result", "true");
            map.put("msg", "商品编辑成功!!");


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
        posItemVo.setItemName(itemTemplate.getName());
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
        posItemVo.setItemImg(itemTemplate.getImageUrl());
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
    public List<PosItemVo> convertToVos(List<PosItem> posItems){
        List<PosItemVo> posItemVos = new ArrayList<>();
        for (PosItem posItem : posItems) {

            LifeItemTemplate itemTemplate = itemTemplateManager
                    .getItemTemplateById(posItem.getItemTemplateId());
            PosItemVo posItemVo = new PosItemVo();
            posItemVo.setId(posItem.getId());
            posItemVo.setPosTypeId(posItem.getItemTypeId());
            LifeItemType itemType = itemTypeManager.getItemTypeById(posItem.getItemTypeId());
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
            posItemVo.setItemTypeId(posItem.getItemTypeId());
            posItemVo.setBuyCount(posItem.getBuyCount());
            posItemVo.setCreateDate(posItem.getCreateTime());
            posItemVo.setBarcode(itemTemplate.getBarcode());
            posItemVo.setIsDelete(posItem.getIsDelete());
            posItemVo.setIsShelve(posItem.getIsShelve());
            posItemVo.setItemImg(itemTemplate.getImageUrl());
            posItemVo.setRepertory(posItem.getRepertory());
            posItemVo.setWarningRepertory(posItem.getWarningRepertory());
            posItemVo.setShelfLife(posItem.getShelfLife());
            LifeItemUnit itemUnit = itemUnitManager.getItemUnitById(itemTemplate.getUnitId());
            posItemVo.setItemUnitId(itemUnit.getId());
            posItemVo.setItemUnitName(itemUnit.getTitle());
            posItemVo.setGeneratedDate(posItem.getGeneratedDate());
            posItemVos.add(posItemVo);
        }

        return posItemVos;

    }
}
