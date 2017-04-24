package com.wm.controller.order;

import com.wm.entity.order.ConfirmOrderEvidenceEntity;
import com.wm.service.order.OrderServiceI;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by mjorcen on 16/8/27.
 */
@Controller
@RequestMapping("ci/confirmOrderEvidenceController")
public class ConfirmOrderEvidenceController extends BaseController {

    @Autowired
    private OrderServiceI orderService;

    @RequestMapping(params = "createEvidence")
    @ResponseBody
    public AjaxJson createEvidence(Integer orderId, Integer userId, String payId, String images, String textarea,
                                   HttpServletRequest request) {
        AjaxJson ajaxJson = AjaxJson.successJson("请求成功");
        List<ConfirmOrderEvidenceEntity> entitys = orderService.findByProperty(ConfirmOrderEvidenceEntity.class, "orderId", orderId);

        ConfirmOrderEvidenceEntity entity;
        if (!CollectionUtils.isEmpty(entitys)) {
//            for (ConfirmOrderEvidenceEntity entity : entitys) {
//                if (entity.getState() == 0 || entity.getState() == 1) {
//                    ajaxJson = AjaxJson.failJson("认证信息审核中,请耐心等待");
//                    return ajaxJson;
//                }
//                if (entity.getState() == 2) {
//                    ajaxJson = AjaxJson.failJson("认证已通过,请不要重复上传");
//                    return ajaxJson;
//                }
//            }
            entity = entitys.get(0);
        } else {
            entity = new ConfirmOrderEvidenceEntity();
        }
        entity.setState(0);
        entity.setPayId(payId);
        entity.setUserId(userId);
        entity.setOrderId(orderId);
        entity.setTextarea(textarea);
        entity.setImages(images);
        entity.setCreateTime(System.currentTimeMillis() / 1000);

        if (!CollectionUtils.isEmpty(entitys)) {
            orderService.updateEntitie(entity);
        } else {
            orderService.save(entity);
        }
        return ajaxJson;
    }


    @RequestMapping(params = "getEvidence")
    @ResponseBody
    public AjaxJson getEvidence(Integer orderId,
                                HttpServletRequest request) {
        AjaxJson ajaxJson = AjaxJson.successJson("请求成功");
        List<ConfirmOrderEvidenceEntity> entitys = orderService.findByProperty(ConfirmOrderEvidenceEntity.class, "orderId", orderId);
        if (CollectionUtils.isEmpty(entitys)) {
            ajaxJson = AjaxJson.failJson("没有相关的信息");
            return ajaxJson;
        }
        ConfirmOrderEvidenceEntity entity = entitys.get(entitys.size() - 1);
        ajaxJson.setObj(entity);
        return ajaxJson;
    }

    @RequestMapping(params = "updateEvidenceState")
    @ResponseBody
    public AjaxJson updateEvidenceState(Integer id, Integer state,
                                        HttpServletRequest request) {
        AjaxJson ajaxJson = AjaxJson.successJson("请求成功");
        ConfirmOrderEvidenceEntity entity = orderService.getEntity(ConfirmOrderEvidenceEntity.class, id);
        entity.setState(state);
        orderService.updateEntitie(entity);
        return ajaxJson;
    }

}
