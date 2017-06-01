package com.dianba.pos.menu.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.item.po.ItemType;
import com.dianba.pos.item.repository.ItemTypeJpaRepository;
import com.dianba.pos.item.service.ItemTypeManager;
import com.dianba.pos.menu.config.MenuUrlConstant;
import com.dianba.pos.menu.po.PosType;
import com.dianba.pos.menu.repository.PosTypeJpaRepository;
import com.dianba.pos.menu.service.PosTypeManager;
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
    private ItemTypeManager itemTypeManager;
    @Autowired
    private ItemTypeJpaRepository itemTypeJpaRepository;
    @Autowired
    private PosTypeJpaRepository posTypeJpaRepository;
    @Autowired
    private PosTypeManager posTypeManager;

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

            PosType posType = posTypeManager.getPosTypeByPassportIdAndItemTypeTitle(Long.parseLong(passportId), title);
            if (posType != null) {

                return BasicResult.createFailResult("商家分类名称重复了。");

            } else {
                ItemType itemType = new ItemType();
                itemType.setTitle(title);
                itemType.setAscriptionType(1);
                itemTypeJpaRepository.save(itemType);

                posType = new PosType();
                posType.setPassportId(Long.parseLong(passportId));
                posType.setItemTypeId(itemType.getId());

                posTypeJpaRepository.save(posType);
                return BasicResult.createSuccessResult("添加商家分类成功!");

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

        PosType posType = posTypeManager.getPosTypeById(posTypeId);
        if (posType != null && posType.getPassportId() == passportId) {

            posTypeJpaRepository.delete(posType);
            return BasicResult.createSuccessResult("删除商家分类成功!");
        } else {

            return BasicResult.createFailResult("删除商家分类异常!");
        }
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
        if (posType != null && posType.getPassportId() == passportId) {
            ItemType itemType = itemTypeManager.getItemTypeById(posType.getItemTypeId());
            itemType.setTitle(title);
            itemTypeJpaRepository.save(itemType);
            posType.setItemTypeTitle(title);
            posTypeJpaRepository.save(posType);
            return BasicResult.createSuccessResult("编辑商家分类成功!");
        } else {

            return BasicResult.createFailResult("删除商家分类异常!");
        }
    }
}
