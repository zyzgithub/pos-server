package com.courier_mana.statistics.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.statistics.service.CouriersProductService;

@Service
public class CouriersProductServiceImpl extends CommonServiceImpl implements CouriersProductService {
	private final static Logger logger = LoggerFactory.getLogger(CouriersProductServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Override
	public List<Map<String, Object>> productRank(SearchVo vo, Integer courierId, Integer page, Integer rowsPerPage){
		logger.info("Invoke method: productRank, params: courierId - {}, vo.timeType - {}, vo.orgId - {}, vo.beginTime - {}, vo.endTime - {}, page - {}, rowsPerPage - {}",
															courierId, vo.getTimeType(), vo.getOrgId(), vo.getBeginTime(), vo.getEndTime(), page, rowsPerPage);
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
		 * 根据搜索条件(时间)调整SQL条件
		 */
		String period = "";	//查询时间
		{
			String timeType = vo.getTimeType();
			if(timeType == null || timeType.equals("day")){
				period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE()) ";			//按天搜索的条件
			}else if(timeType.equals("week")){
				period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE())-6*86400 ";	//近7天搜索的条件
			}else if(timeType.equals("month")){
				period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE())-29*86400 ";	//近30天搜索的条件
			}else if(timeType.equals("other") && !vo.getBeginTime().equals(0) && !vo.getBeginTime().equals(0)){//SearchVo中beginTime, endTime会自动初始化为0
				period = " AND o.create_time >= " + this.getStartTimeOfTheDayInSecond(vo.getBeginTime()) + " AND o.create_time < " + this.getEndTimeOfTheDayInSecond(vo.getEndTime()) + " ";
			}
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * ");
		sql.append(" 	FROM( ");
		sql.append(" 	SELECT sourceList.* ");
		sql.append(" 		,case when @prevrank = sourceList.quantity then @currank when @prevrank := sourceList.quantity then @currank := @currank + 1 when not @prevrank := sourceList.quantity then @currank := @currank + 1 end as rank ");
		sql.append(" 	FROM( ");
		sql.append(" 		SELECT m.id, m.`name`, SUM(om.quantity) quantity ");
		sql.append(" 			,(SELECT SUM(odm.quantity) FROM `order` od, order_menu odm WHERE od.id = odm.order_id AND odm.menu_id = m.id AND o.pay_state='pay' AND o.pay_id is not null AND o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal'))totalQuantity ");
		sql.append(" 		FROM `order` o, order_menu om, menu m, 0085_merchant_org mo ");
		sql.append(" 		WHERE o.pay_state='pay' AND o.pay_id is not null AND o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 			AND o.id = om.order_id AND m.id = om.menu_id AND o.merchant_id = mo.merchant_id ");
		sql.append(period);
		/**
		 * 根据搜索条件(地区)调整SQL条件
		 */
		if(vo.getOrgId() != null){
			List<Integer> orgIds = this.courierOrgService.getManageOrgIds(courierId, vo.getOrgId());
			sql.append(" 			AND mo.org_id IN ( ");
			sql.append(StringUtils.join(orgIds, ","));
			sql.append(" 			) ");
		}
		sql.append(" 		GROUP BY m.id ");
		sql.append(" 		ORDER BY quantity DESC, m.id ");
		sql.append(" 	)sourceList,(select @currank :=0, @prevrank := null) var ");
		sql.append(" )rankList ");
		
		logger.debug("Inside method: productRank, SQL: {}", sql.toString());
		
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
	 * 整理vo中的秒数(vo所存的时间以秒为单位)
	 * 使它变成当天的最后一秒(第二天的0秒)
	 * 直接使用秒数查询比较快
	 */
	private Integer getEndTimeOfTheDayInSecond(Integer endTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(endTime*1000l);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return (int)(calendar.getTimeInMillis()/1000);
	}
	
	/**(OvO)
	 * 整理vo中的秒数(vo所存的时间以秒为单位)
	 * 使它变成当天的0秒
	 * 直接使用秒数查询比较快
	 */
	private Integer getStartTimeOfTheDayInSecond(Integer startTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime*1000l);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return (int)(calendar.getTimeInMillis()/1000);
	}
}
