package com.dianba.pos.item.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.po.LifeItemType;
import com.dianba.pos.item.repository.LifeItemTypeJpaRepository;
import com.dianba.pos.item.repository.PosTypeJpaRepository;
import com.dianba.pos.item.service.LifeItemTypeManager;
import com.dianba.pos.item.config.MenuUrlConstant;
import com.dianba.pos.item.po.PosType;
import com.dianba.pos.item.service.PosTypeManager;
import com.dianba.pos.item.vo.PosTypeVo;
import com.dianba.pos.passport.mapper.PassportMapper;
import com.dianba.pos.passport.po.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangyong on 2017/5/31.
 */
@Controller
@RequestMapping(MenuUrlConstant.POS_TYPE_URL)
public class PosTypeController {

    @Autowired
    private LifeItemTypeManager itemTypeManager;
    @Autowired
    private LifeItemTypeJpaRepository itemTypeJpaRepository;
    @Autowired
    private PosTypeJpaRepository posTypeJpaRepository;
    @Autowired
    private PosTypeManager posTypeManager;

    @Autowired
    private PassportMapper passportMapper;
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

        if (StringUtil.isEmpty(passportId) || StringUtil.isEmpty(title)) {
            return BasicResult.createFailResult("参数输入有误，或者参数值为空");
        } else {

            Passport passport=passportMapper.getPassportInfoByCashierId(Long.parseLong(passportId));
            PosType posType = posTypeManager.getPosTypeByPassportIdAndItemTypeTitle(passport.getId(), title);
            if (posType != null) {

                return BasicResult.createFailResult("商家分类名称重复了。");

            } else {
                LifeItemType itemType = new LifeItemType();
                itemType.setTitle(title);
                itemType.setIcon("http://no1.0085.com");
                itemType.setImage("http://no1.0085.com");
                itemType.setParentId(0L);
                itemType.setSort(0);
                itemType.setStatus(0);
                itemType.setTop(0);
                itemType.setAscriptionType(1);
                itemTypeJpaRepository.save(itemType);

                posType = new PosType();

                posType.setPassportId(passport.getId());
                posType.setItemTypeId(itemType.getId());
                posType.setItemTypeTitle(title);
                posType.setIsDelete("N");
                posType.setSort(0);
                posTypeJpaRepository.save(posType);
                PosTypeVo posTypeVo=new PosTypeVo();
                posTypeVo.setId(posType.getId());
                posTypeVo.setItemTypeId(posType.getItemTypeId());
                posTypeVo.setTitle(posType.getItemTypeTitle());

                posTypeVo.setTypeCount(0);
                JSONObject jsonObject=(JSONObject)JSONObject.toJSON(posTypeVo);
                return BasicResult.createSuccessResult("添加商家分类成功!",jsonObject);

            }

        }


    }

    /**
     * 删除商家分类
     *
     * @param passportId
     * @param posTypeId
     * @return
     */
    @ResponseBody
    @RequestMapping("deletePosType")
    public BasicResult deletePosType(Long passportId, Long posTypeId) {

        Passport passport=passportMapper.getPassportInfoByCashierId(passportId);
        return posTypeManager.deletePosType(passport.getId(),posTypeId);
    }

    /**
     * 编辑商家分类
     *
     * @param passportId
     * @param posTypeId
     * @param title
     * @return
     */
    @ResponseBody
    @RequestMapping("editPosType")
    public BasicResult editPosType(Long passportId, Long posTypeId, String title) {

        PosType posType = posTypeManager.getPosTypeById(posTypeId);
        Passport passport=passportMapper.getPassportInfoByCashierId(passportId);
        if (posType != null && posType.getPassportId().equals(passport.getId())) {
            LifeItemType itemType = itemTypeManager.getItemTypeById(posType.getItemTypeId());
            itemType.setTitle(title);
            itemTypeJpaRepository.save(itemType);
            posType.setItemTypeTitle(title);
            posTypeJpaRepository.save(posType);
            JSONObject jsonObject=(JSONObject)JSONObject.toJSON(posType);
            return BasicResult.createSuccessResult("编辑商家分类成功!",jsonObject);
        } else {
            return BasicResult.createFailResult("编辑商家分类异常!");
        }
    }
}
