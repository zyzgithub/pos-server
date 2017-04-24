package com.wm.service.impl.pos;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.pos.PosOrderModifyMoneyDetail;
import com.wm.exception.ApplicationRuntimeException;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pos.PosOrderModifyMoneyDetailService;







import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;import java.util.HashMap;
import java.util.Map;

/**
 * Created by mjorcen on 16/8/15.
 */
@Service
public class PosOrderModifyMoneyDetailServiceImpl extends CommonServiceImpl implements PosOrderModifyMoneyDetailService {
	private static final Logger logger = LoggerFactory.getLogger(PosOrderModifyMoneyDetailServiceImpl.class);
    @Autowired
    private OrderServiceI orderService;

    @Override
    public Map<String, Object> modify(PosOrderModifyMoneyDetail detail, String remark) {
        if (detail == null) {
            throw new ApplicationRuntimeException(01, "参数不能为空");
        }
        if (detail.getModifyType() == null) {
            throw new ApplicationRuntimeException(02, "修改类型不能为空");
        }
        if (detail.getModifyType() == 2 && detail.getModifyDiscount() == null) {
            throw new ApplicationRuntimeException(03, "折扣不能为空");
        }
        if (detail.getOrderId() == null) {
            throw new ApplicationRuntimeException(04, "订单 ID 不能为空");
        }

        OrderEntity orderEntity = orderService.getEntity(OrderEntity.class, detail.getOrderId());

        if (orderEntity == null) {
            throw new ApplicationRuntimeException(05, "订单不存在");
        }

        PosOrderModifyMoneyDetail dbDetail = super.commonDao.findUniqueByProperty(PosOrderModifyMoneyDetail.class, "orderId", detail.getOrderId());
        Integer detailId;

        if (dbDetail != null) {
            orderEntity.setOrigin(dbDetail.getRealPrice().doubleValue());
            detailId = dbDetail.getId();
            detail.setId(detailId);
        }

        detail.setRealPrice(BigDecimal.valueOf(orderEntity.getOrigin()));
        detail.setMerchantId(orderEntity.getMerchant().getId());
        /*
        折扣类型
        1 : 免单
        2 : 折扣
        3 : 抹零
        4 : 修改
         */
        BigDecimal modifyMoney;
        Double totalPrice = 0.00;
        switch (detail.getModifyType()) {
            case 1: {
                modifyMoney = BigDecimal.valueOf(orderEntity.getOrigin()).multiply(BigDecimal.valueOf(-1));
                totalPrice = 0D;
                detail.setModifyMoney(modifyMoney);

            }
            break;
            case 2: {

                modifyMoney = BigDecimal.valueOf(orderEntity.getOrigin() / 100 *(100 - detail.getModifyDiscount()));
                totalPrice = formatDouble(orderEntity.getOrigin() - modifyMoney.doubleValue());
                detail.setModifyMoney(modifyMoney.multiply(BigDecimal.valueOf(-1)));
            }
            break;
            case 3: {
                modifyMoney = BigDecimal.valueOf(orderEntity.getOrigin() - orderEntity.getOrigin().intValue());
                totalPrice = formatDouble(orderEntity.getOrigin() - modifyMoney.doubleValue());
                detail.setModifyMoney(modifyMoney.multiply(BigDecimal.valueOf(-1)));

            }
            break;
            case 4: {
                modifyMoney = BigDecimal.valueOf(orderEntity.getOrigin() - detail.getModifyMoney().doubleValue());
                totalPrice = formatDouble(orderEntity.getOrigin() - modifyMoney.doubleValue());
                detail.setModifyMoney(modifyMoney.multiply(BigDecimal.valueOf(-1)));
            }
            break;
        }
        
        orderEntity.setOrigin(totalPrice);

        detail.setCreateTime(System.currentTimeMillis() / 1000);
        if (dbDetail == null) {
            this.commonDao.save(detail);

        } else {
            try {
                BeanUtils.copyProperties(dbDetail,detail);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            this.commonDao.updateEntitie(dbDetail);
        }

        if (StringUtils.isNotBlank(remark)) {
            orderEntity.setRemark(remark);
        }
        this.orderService.updateEntitie(orderEntity);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("totalPrice", totalPrice);
        return resultMap;
    }


    private double formatDouble(double d) {
        return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


	@Override
	public Map<String, Object> getOrderModifyDetail(Integer orderId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select po.modify_money money, ");
			query.append(" case po.modify_type when 1 then '免单'");
			query.append(" 	when 2 then '打折'");
			query.append(" 	when 3 then '抹零'");
			query.append(" 	when 4 then '修改'");
			query.append(" 	else '其它' end modifyType");
			query.append(" from pos_order_modify_money_detail po ");
			query.append(" where po.order_id = ? ");
			resultMap =  findOneForJdbc(query.toString(), orderId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取订单：{}修改详情失败", orderId);
		}
		return resultMap;
	}
}
