package com.wm.service.impl.deduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courier_mana.common.Constants;
import com.wm.entity.deduct.DeductLogEntity;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.service.deduct.DeductServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.user.WUserServiceI;

@Service("deductService")
@Transactional
public class DeductServiceImpl extends CommonServiceImpl implements DeductServiceI {
	
	@Autowired
	private OrgServiceI orgService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private DeductLogServiceI deductLogService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	@Value("${disableDeduct}")
	private String disableDeduct;
	
	private static final String getSupplyOrderMoney = "SELECT sum(o.origin) from `order` o where o.courier_id=? and state='confirm' "
			+ " and o.from_type='supplychain' and FROM_UNIXTIME(o.complete_time, '%Y-%m-%d')=? ";
	
	
	public void saveAndUpdateMoney(DeductLogEntity deductLog) throws Exception {
		// 上线初期由于业务需要，可暂停快递员提成入账户余额
		if(StringUtils.isBlank(disableDeduct) || "false".equals(disableDeduct)){
			flowService.deductFlowCreate(deductLog);
		}
		//写提成日志表
		deductLogService.save(deductLog);
	}

	public List<Map<String, Object>> getCourierDeductRule(Integer courierId, Integer userId, Integer courierType) {
		String sql = "select cd.id,cd.deduct,cd.sub_deduct,cd.rewards,cd.subsidy,cd.mng_deduct,cgr.total_order ";
		sql += " from 0085_courier_deduct cd left join 0085_courier_grade_rule cgr on cd.grade_rule_id=cgr.id ";
		if(Constants.COURIER.equals(courierType) ||
				Constants.COURIER_TYPE_SUPPLY_NORMAL_FLAT.equals(courierType) || Constants.COURIER_TYPE_SUPPLY_DRIVER_FLAT.equals(courierType)){
			sql += " left join 0085_courier_position cp on cp.position_id=cd.position_id ";
			sql += " where cd.user_id = ? and cd.type = ? ";
			sql += " and cp.courier_id=?  order by cgr.total_order desc ";
			return this.findForJdbc(sql, new Object[]{userId, courierType, courierId});
		} else if(Constants.COURIER_COOPERATE_BUSINESS.equals(courierType)){
			sql += " where cd.user_id = ? and cd.type = ? ";
			sql += " order by cgr.total_order desc ";
			return this.findForJdbc(sql, new Object[]{userId, courierType});
		} else if (Constants.COURIER_TYPE_SUPPLY_NORMAL.equals(courierType) || Constants.COURIER_TYPE_SUPPLY_DRIVER.equals(courierType)){
			// 供应链订单提成规则
			sql += " left join 0085_courier_position cp on cp.position_id=cd.position_id ";
			sql += " where cd.user_id = ? and cd.type = ? ";
			sql += " and cp.courier_id=? order by cgr.total_order desc ";
			return this.findForJdbc(sql, new Object[]{userId, courierType, courierId});
		} else {
			sql += " where cd.user_id = ? and cd.type = ? ";
			return this.findForJdbc(sql, new Object[]{userId, courierType});
		}
	}
	
	public Long getQuantityByOrder(Integer orderId, boolean countDeduct) {
		String sql = "select count(m.id) total from menu m ";
		sql += " left join order_menu om on om.menu_id=m.id ";
		sql += " left join `order` o on o.id=om.order_id ";
		sql += " left join merchant mer on o.merchant_id=mer.id ";
		sql += " left join 0085_merchant_info mi on mi.merchant_id=mer.id ";
		sql += " where o.id=? ";
		if(countDeduct){
			sql += " and om.price>= case when mi.courier_min_deduct_money is not null then mi.courier_min_deduct_money else 0 end  ";
		}
		return getCountForJdbcParam(sql, new Object[]{orderId});
	}
	
	@Override
	public Double getCourierDeduct(Integer courierId, Integer totalOrder, Integer userId) {
		Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
		List<Map<String, Object>> list = this.getCourierDeductRule(courierId,userId, courierType);
		for(Map<String, Object> map : list){
			if(totalOrder >= Double.parseDouble(map.get("total_order").toString())){
				return Double.parseDouble(map.get("deduct").toString());
			}
		}
		return 0d;
	}
	
	/**
	 * 获取在在黑名单中的快递员ID列表
	 * @return
	 */
	public List<Integer> getCourierInBlacklist(){
		List<Integer> courierIds = new ArrayList<Integer>();
		String sql = "select courier_id from 0085_courier_deduct_blacklist" ;
		List<Map<String, Object>> list = this.findForJdbc(sql);
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map: list){
				courierIds.add(Integer.parseInt(map.get("courier_id").toString()));
			}
		}
		return courierIds;
	}

	@Override
	public Double getSupplyOrderMoney(Integer courierId, String deductDate) {
		Double money = findOneForJdbc(getSupplyOrderMoney, Double.class, courierId, deductDate);
		return money == null ? 0.0 : money;
	}

}
