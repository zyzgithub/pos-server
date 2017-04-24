package com.courier_mana.statistics.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.AgentServiceI;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.statistics.service.CourierStatisticService;
import com.team.wechat.util.DateUtil;

/**(OvO)
 * 统计-快递员统计模块实现类
 * @author hyj
 *
 */
@Service
public class CourierStatisticServiceImpl extends CommonServiceImpl implements CourierStatisticService {
	private static final Logger logger = LoggerFactory.getLogger(CourierStatisticServiceImpl.class);

	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@Autowired
	private AgentServiceI agentService;
	
	@Override
	public List<Map<String, Object>> realTimeStatistic(Integer courierId, Integer orgId, Integer page, Integer rowsPerPage,String interval) {
		logger.info("Invoke method: realTimeStatistic, params: courierId - {}, orgId - {}, page - {}, rowsPerPage - {}", courierId, orgId, page, rowsPerPage);
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		/**
		 * 初始化分页参数
		 */
		page = this.initPageVar(page);
		rowsPerPage = this.initRowsPerPageVar(rowsPerPage);
		
		/**
		 * 判断courierId是否合作商用户ID, 控制流程
		 * 获取需要查询的快递员列表
		 */
		List<Integer> couriersId = null;
		if(this.courierMyInfoService.isAgentUser(courierId)){
			couriersId = this.agentService.getAgentCourierIds(courierId);
		}else{
			couriersId = this.getManageCouriersId(courierId, orgId);
		}
		
		String timeWhere = null;  //day日,week周,month月
		switch (interval) {
		case "day":
			timeWhere = new String("o.create_time >= UNIX_TIMESTAMP(CURDATE())");
			break;
		case "week":
			Map<String, String> week = DateUtil.getWeek();
			timeWhere = new String("o.create_time between UNIX_TIMESTAMP('"+ week.get("first") +"') and UNIX_TIMESTAMP('"+ week.get("last") +"')");
			break;
		case "month":
			Map<String, String> month = DateUtil.getMonth();
			timeWhere = new String("o.create_time between UNIX_TIMESTAMP('"+ month.get("first") +"') and UNIX_TIMESTAMP('"+ month.get("last") +"')");
			break;
		default:
			break;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.* from (SELECT u.id courierId, u.username courierName ");
		sql.append(" 	,COUNT(CASE WHEN o.state = 'accept' THEN 1 ELSE NULL END) holding ");
		sql.append(" 	,COUNT(CASE WHEN o.state = 'delivery' AND UNIX_TIMESTAMP(NOW()) - o.delivery_time > 1800 THEN 1 ELSE NULL END) timeOut ");
		sql.append(" 	,COUNT(CASE WHEN o.state = 'delivery' THEN 1 ELSE NULL END) delivering ");
		sql.append(" 	,COUNT(CASE WHEN o.state IN('delivery_done','done','confirm','evaluated')THEN 1 ELSE NULL END) finished ");
		sql.append(" FROM `user` u LEFT JOIN `order` o ON o.courier_id = u.id AND "+ timeWhere +" ");
		sql.append(" WHERE u.id IN ( ");
		sql.append(StringUtils.join(couriersId, ","));
		sql.append(" ) ");
		sql.append(" GROUP BY u.id ");
		sql.append(" ORDER BY delivering DESC, u.id ) a order by finished desc");
		
		logger.debug("Inside method: realTimeStatistic, SQL: {}", sql.toString());
		
		return findForJdbc(sql.toString(), page, rowsPerPage);
	}
	
	@Override
	public List<Map<String, Object>> realTimeRank(Integer courierId, Integer orgId, Integer page, Integer rowsPerPage) {
		logger.info("Invoke method: realTimeRank, params: courierId - {}, orgId - {}, page - {}, rowsPerPage - {}", courierId, orgId, page, rowsPerPage);
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 初始化分页参数
		 */
		page = this.initPageVar(page);
		rowsPerPage = this.initRowsPerPageVar(rowsPerPage);
		
		/**
		 * 判断courierId是否合作商用户ID, 控制流程
		 * 获取需要查询的快递员列表
		 */
		List<Integer> couriersId = null;
		if(this.courierMyInfoService.isAgentUser(courierId)){
			couriersId = this.agentService.getAgentCourierIds(courierId);
		}else{
			couriersId = this.getManageCouriersId(courierId, orgId);
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM( ");
		sql.append(" 	SELECT sourceList.*,case when @prevrank = sourceList.todayOrderCount then @currank when @prevrank := sourceList.todayOrderCount then @currank := @currank + 1 when not @prevrank := sourceList.todayOrderCount then @currank := @currank + 1 end as rank ");
		sql.append(" FROM( ");
		sql.append(" 		SELECT u.id courierId, u.username, ");
		sql.append(" 			(SELECT COUNT(1) FROM `order` o WHERE o.state IN ('delivery_done','done','confirm','evaluated') AND o.pay_time >= UNIX_TIMESTAMP(CURDATE()) AND o.courier_id = u.id) todayOrderCount, ");
		sql.append(" 			(SELECT COUNT(1) FROM `order` o WHERE o.state IN ('delivery_done','done','confirm','evaluated') AND o.courier_id = u.id) totalOrderCount ");
		sql.append(" 		FROM `user` u ");
		sql.append(" 		WHERE u.id IN ( ");
		sql.append(StringUtils.join(couriersId, ","));
		sql.append(" 		) ");
		sql.append(" 		ORDER BY todayOrderCount DESC, u.id ");
		sql.append(" 	)sourceList, (select @currank :=0, @prevrank := null) r ");
		sql.append(" )rankList ");
		
		logger.debug("Inside method: realTimeRank, SQL: {}", sql.toString());
		
		return findForJdbc(sql.toString(), page, rowsPerPage);
	}
	
	/**(OvO)
	 * 初始化page(页码)变量
	 * @param page 需要初始化的页码变量
	 * @return 如果page为空则返回1
	 */
	private Integer initPageVar(Integer page){
		if(page == null){
			return 1;
		}
		return page;
	}
	
	/**(OvO)
	 * 初始化rowsPerPage(每页显示记录数)变量
	 * @param page 需要初始化的页码变量
	 * @return 如果page为空则返回1
	 */
	private Integer initRowsPerPageVar(Integer rowsPerPage){
		if(rowsPerPage == null){
			return 10;
		}
		return rowsPerPage;
	}
	
	/**(OvO)
	 * 获取管辖的"飞行员"ID
	 * @param courierId	快递员ID
	 * @param orgId		区域ID
	 * @return	"飞行员"ID
	 */
	private List<Integer> getManageCouriersId(Integer courierId, Integer orgId){
		/**
		 * 获取管辖范围的所有orgId
		 */
		List<Integer> orgIds = null;
		if(orgId == null){
			orgIds = this.courierOrgService.getManageOrgIds(courierId);
		}else{
			orgIds = this.courierOrgService.getManageOrgIds(courierId, orgId);
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.id ");
		sql.append(" FROM 0085_courier_org corg, `user` u, 0085_courier_position cp ");
		sql.append(" WHERE u.id = corg.courier_id AND u.is_delete = 0 ");
		sql.append(" 	AND cp.courier_id = u.id AND cp.position_id = 2 ");
		sql.append(" 	AND corg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		
		return findListbySql(sql.toString());
	}
	
	/**
	 * 获取快递员实时排行前三
	 */
	@Override
	public List<Map<String, Object>> firstThreeRank(Integer courierId,Integer orgId,String interval) {
		logger.info("Invoke method: realTimeRank, params: courierId - {}, orgId - {}, page - {}, rowsPerPage - {}", courierId, orgId);
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null)
			throw new IllegalArgumentException("courierId不能为空");
		
		Boolean flag = courierMyInfoService.isAgentUser(courierId);
		
		List<Integer> couriersId = null;
		
		//获取需要查询的快递员列表 
		if(flag){
			couriersId = this.courierOrgService.getPartnerUserId(courierId);
		}else{
			couriersId = this.getManageCouriersId(courierId, orgId);
		}
		
		String timeWhere = null;  //day日,week周,month月
		switch (interval) {
		case "day":
			timeWhere = new String("o.pay_time >= UNIX_TIMESTAMP(CURDATE())");
			break;
		case "week":
			Map<String, String> week = DateUtil.getWeek();
			timeWhere = new String("o.pay_time between UNIX_TIMESTAMP('"+ week.get("first") +"') and UNIX_TIMESTAMP('"+ week.get("last") +"')");
			break;
		case "month":
			Map<String, String> month = DateUtil.getMonth();
			timeWhere = new String("o.pay_time between UNIX_TIMESTAMP('"+ month.get("first") +"') and UNIX_TIMESTAMP('"+ month.get("last") +"')");
			break;
		default:
			break;
		}
		
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM( ");
		sql.append(" 	SELECT sourceList.*,case when @prevrank = sourceList.todayOrderCount then @currank when @prevrank := sourceList.todayOrderCount then @currank := @currank + 1 when not @prevrank := sourceList.todayOrderCount then @currank := @currank + 1 end as rank ");
		sql.append(" FROM( ");
		sql.append(" 		SELECT u.id courierId, u.username,u.photoUrl, ");
		sql.append(" 		(SELECT COUNT(1) FROM `order` o WHERE o.state IN ('delivery_done','done','confirm','evaluated') AND "+ timeWhere +" AND o.courier_id = u.id) todayOrderCount ");
		sql.append(" ,a.reward,a.order_deduct orderDeduct ");
		sql.append(" 		FROM `user` u INNER JOIN 0085_courier_deduct_log a ON a.courier_id = u.id");
		sql.append(" 		WHERE u.id IN ( ");
		sql.append(StringUtils.join(couriersId, ","));
		sql.append(" 		) ");
		sql.append(" group by u.id ORDER BY todayOrderCount DESC, u.id ");
		sql.append(" 	)sourceList, (select @currank :=0, @prevrank := null) r ");
		sql.append(" )rankList  LIMIT 3");
		
		logger.debug("Inside method: realTimeRank, SQL: {}", sql.toString());
		
		return findForJdbc(sql.toString());
	}

    /**
     * 快递员订单信息
     * @param courierId     快递员ID(必选)
     * @param page          page：页数(可选, 默认为1)
     * @param rowsPerPage   rowsPerPage：每页显示的记录数(可选, 默认为10)
     * @return  返回快递员订单信息
	 */
    public List<Map<String, Object>> getCourierOrderInfos(Integer courierId, Integer page, Integer rowsPerPage){
        List<Map<String, Object>> list = null;
        StringBuilder query = new StringBuilder();
        query.append("  select u.id courierId, u.username courierName , u.mobile as courierMobile,o.state,o.realname,o.delivery_time as sendTime,o.order_num as orderNum,o.mobile,o.id as orderId,o.pay_id as payId,o.remark,m.title as merchantTitle ,m.id as merchantId,o.address,o.order_type as orderType,o.from_type as orderFrom,o.user_id as userId ");
        query.append("  FROM `user` u  ");
        query.append("  LEFT JOIN `order` o ON o.courier_id = u.id AND o.create_time >= UNIX_TIMESTAMP(CURDATE())  ");
        query.append("  LEFT JOIN merchant m ON o.merchant_id=m.id ");
        query.append("  WHERE u.id = "+courierId);
        list = findForJdbc(query.toString(), page, rowsPerPage); 

        // 快递员信息查询和上部分分离
        if (list != null) {
            for (Map<String, Object> map : list) { 
                String orderType = (String)map.get("orderType");
                //保证orderType 只返回4中状态
                if(!"mobile".equals(orderType)){
                    map.put("orderType", "normal");
                }
                // 判断是否 为私厨，众包，电话订单 在 orderType 只允许一种存在，优先级为 私厨>众包>电话订单
                
                Object orderFromTempl = map.get("orderFrom");
                Object merchantIdTempl = map.get("merchantId");
                
                if(orderFromTempl == null || merchantIdTempl == null)
                	continue;
                String crowdSourse = orderFromTempl.toString();
                String merchantId = merchantIdTempl.toString();
                Map<String, Object> privateOrder = this.getPrivateOrder(merchantId);
                Integer type = null;
                if (privateOrder != null) {
                    type = (Integer) map.get("private");
                }
                if ("crowdsourcing".equals(crowdSourse)) {
                    Map<String, Object> merchantDetail = this.getMerchantDetail(merchantId);
                    map.put("orderType", "crowd");
                    map.put("merchantUsername",
                            (String) merchantDetail.get("merchantUsername"));
                    map.put("merchantMobile",
                            (String) merchantDetail.get("merchantMobile"));
                    map.put("merchantAddress",
                            (String) merchantDetail.get("merchantAddress"));
                }
                if (type != null && type == 2) {
                    map.put("orderType", "private");
                }
            }
        }
        return list;
    }
        
    public Map<String, Object> getPrivateOrder(String merchantId) {
        StringBuilder query = new StringBuilder();
        query.append("select  info.merchant_source as private from 0085_merchant_info info where info.merchant_id = "+merchantId);
        Map<String, Object> privateOrder = findOneForJdbc(query.toString());
        return privateOrder;
    } 
    
    public Map<String, Object> getMerchantDetail(String merchantId) {
        StringBuilder query = new StringBuilder();
        query.append("select m.address as merchantAddress ,u.username as merchantUsername ,u.mobile as merchantMobile from merchant m ,`user` u where m.user_id = u.id and m.id ="+merchantId);
        Map<String, Object> merchantDetail = findOneForJdbc(query.toString());
        return merchantDetail;
    }
}
