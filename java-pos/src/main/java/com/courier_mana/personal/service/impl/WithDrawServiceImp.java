package com.courier_mana.personal.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.courier_mana.personal.service.WithDrawService;

/**
 * @author zhaoyue
 *
 */
@Service
public class WithDrawServiceImp extends CommonServiceImpl implements
		WithDrawService {

	private static Logger logger = LoggerFactory
			.getLogger(WithDrawServiceImp.class);

	@Override
	public List<Map<String, Object>> getCourierCardId(Integer userId) {

		if (userId == null) {
			throw new IllegalArgumentException("userId=null");
		}
		StringBuilder queryCardSql = new StringBuilder();
		queryCardSql
				.append(" select b.card_no as cardNo,a.name as bankName,u.money from bank as a ,bank_card as b,`user` u");
		queryCardSql.append(" where a.id=b.bank_id and b.user_id=? and u.id=b.user_id ");
		logger.info("queryCardSql{}", queryCardSql);
		StringBuilder querySelect = new StringBuilder();
		querySelect.append("  select  count(user_id) as applyCount from withdrawals where  DATE(FROM_UNIXTIME(submit_time))=DATE(now()) and user_id= ?");
		Map<String,Object> applyCount = findOneForJdbc(querySelect.toString(), userId);
		List<Map<String,Object>> list = findForJdbc(queryCardSql.toString(), userId);
		if(CollectionUtils.isNotEmpty(list)){
			for(int i=0;i<list.size();i++){
				((Map)list.get(i)).put("applyCount", applyCount);
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getCourierStatistics(Integer courierId,
			Integer page, Integer rows) {
		List<Map<String, Object>> list = null;
		if (courierId == null && page == null && rows == null) {
			throw new IllegalArgumentException("courierId,rows,page中有参数为空");

		} else {
			// 起始行数
			Integer start = (page - 1) * rows;
			// 截止行数
			Integer end =  rows;
			String sql = "select count(*) as count from "
					+ "(select DISTINCT cd.courier_id,cd.deduct_type from 0085_courier_deduct_log cd "
					+ "where cd.courier_id=? and cd.deduct_type<3)a";
			logger.info("sql:{}", sql.toString());
			Long count = (Long) findForJdbc(sql, courierId).get(0).get("count");
			if (count != null) {
				if (count == 1) {
					list = this.getDeductType1(courierId, start, end);
				} else if (count == 2) {
					list = this.getDeductType2(courierId, start, end);
				} else if (count == 3) {
					list = this.getDeductType3(courierId, start, end);
				}
			} else {
				throw new IllegalArgumentException("count 为空！");
			}
			//加上外包订单提成
			String time=null;
			String time1=null;
			String query = "select DATE(FROM_UNIXTIME(cx.account_date)) as time,cx.order_deduct as orderDeduct from 0085_courier_deduct_log cx where cx.deduct_type=3 and cx.courier_id=? ";
			List<Map<String,Object>> other = findForJdbc(query, courierId);
			if(other!=null&&other.size()!=0){
				if(list!=null){
					for(Map<String,Object> map:list){
						logger.info("date:{}",((Date)map.get("time")).toString());
						for(Map<String,Object> ma:other){
							logger.info("date1:{}",((Date)ma.get("time")).toString());
							if(((Date)map.get("time")).toString().equals(((Date)ma.get("time")).toString())){
								logger.info("date2:{}",((Date)ma.get("time")).toString());
								Double  orderDeduct= (Double)map.get("orderDeduct")+(Double)ma.get("orderDeduct") ;
							map.put("orderDeduct",orderDeduct );
							}
						}
					}
				}
				
			}
			
			
		}
		return list;
		// StringBuilder queryCourierStatist = new StringBuilder();
		// queryCourierStatist.append(" select a.courier_id, a.date1, a.deduct_type, count(a.deduct)");
		// queryCourierStatist.append(" (  ");
		// queryCourierStatist.append(" SELECT courier_id, date(from_unixtime(account_date)) date1 , deduct_type, deduct ");
		// queryCourierStatist.append(" from 0085_courier_deduct_log cdl");
		// queryCourierStatist.append(" where cdl.courier_id =?");
		// queryCourierStatist.append(" ) a");
		// queryCourierStatist.append( " where date1 >= '?' and date1 < '?'");
		// queryCourierStatist.append(
		// " group by a.courier_id, a.date1, a.deduct_type");
		// logger.info("sql{}",queryCourierStatist);
		/*
		 * 这里的分页是按照天数来的，一天可能有两条记录 所以page 和 rows 需要转换
		 */
		// Long now = System.currentTimeMillis();
		// Long end= now-(page-1)*rows*24*60*60*1000;
		// Long start = now-page*rows*24*60*60*1000;
		// SimpleDateFormat simformat = new SimpleDateFormat("yyyy-MM-dd");
		//
		// String pagefrom = simformat.format(start);
		// String pageEnd = simformat.format(end);
		// logger.info("pagefrom{} pageEnd{}",pagefrom,pageEnd);
		// List<Map<String,Object>> list = null;
		// list=findForJdbc(queryCourierStatist.toString(),pagefrom,pageEnd);

		// return findForJdbc(queryCourierStatist.toString(),pagefrom,pageEnd);
	}

	/**
	 * 当存在deduct_type=0 的时候查询提成
	 * 
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getDeductType1(Integer courierId,
			Integer start, Integer end) {

		StringBuilder query = new StringBuilder();
		query.append("select");
		query.append("  u.money,DATE(FROM_UNIXTIME(cd.account_date)) as time,cd.order_deduct as orderDeduct,cd.reward,cd.total_deduct as totalDeduct,0 as otherDeduct from ");
		query.append("  0085_courier_deduct_log cd ,");
		query.append("  `user` u");
		query.append("  where cd.deduct_type=0  and cd.courier_id= ? and ");
		query.append("   u.id=cd.courier_id order by DATE(FROM_UNIXTIME(cd.account_date))");
		query.append("  limit ?,?");
		return findForJdbc(query.toString(), courierId, start, end);
	}

	/**
	 * 当存在deduct_type=1的时候查询提成
	 * 
	 * @param courierId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> getDeductType2(Integer courierId,
			Integer start, Integer end) {

		StringBuilder query = new StringBuilder();
		query.append("  select");
		query.append("  u.money,DATE(FROM_UNIXTIME(cd.account_date)) as time,cd.order_deduct as orderDeduct,cd.reward,cd.total_deduct as totalDeduct,a.order_deduct as otherDeduct");
		query.append("  from  `user` u, ");
		query.append("  0085_courier_deduct_log cd ,");
		query.append("  (select cx.courier_id,cx.account_date, cx.order_deduct ");
		query.append("  from 0085_courier_deduct_log cx where cx.deduct_type=1 )a ");
		query.append("  where");
		query.append("  cd.deduct_type=0 and cd.courier_id =a.courier_id and cd.courier_id=?");
		query.append("  and DATE(FROM_UNIXTIME(cd.account_date))=DATE(FROM_UNIXTIME(a.account_date))");
		query.append("  and u.id=cd.courier_id order by DATE(FROM_UNIXTIME(cd.account_date))");
		query.append("  limit ?,?");
		logger.info("query:{}", query.toString());

		return findForJdbc(query.toString(), courierId, start, end);

	}

	/**
	 * 当存在deduct_type=2的时候查询提成
	 * 
	 * @param courierId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> getDeductType3(Integer courierId,
			Integer start, Integer end) {
		StringBuilder query = new StringBuilder();
		query.append("  select");
		query.append("  u.money,DATE(FROM_UNIXTIME(cd.account_date)) as time,cd.order_deduct as orderDeduct,cd.reward,cd.total_deduct as totalDeduct,(a.order_deduct+b.order_deduct) as otherDeduct");
		query.append("  from ");
		query.append("  0085_courier_deduct_log cd ,");
		query.append("  (select cx.courier_id,cx.account_date, cx.order_deduct  from ");
		query.append("  0085_courier_deduct_log cx where cx.deduct_type=1 )a ,");
		query.append("  (select cx.courier_id,cx.account_date, cx.order_deduct  from ");
		query.append("  0085_courier_deduct_log cx where cx.deduct_type=2 )b,");
		query.append("  `user` u");
		query.append("  where cd.deduct_type=0 and cd.courier_id =a.courier_id ");
		query.append("  and cd.courier_id=b.courier_id");
		query.append("  and cd.courier_id= ? and ");
		query.append("  DATE(FROM_UNIXTIME(cd.account_date))=DATE(FROM_UNIXTIME(a.account_date)) ");
		query.append("  and DATE(FROM_UNIXTIME(cd.account_date))=DATE(FROM_UNIXTIME(b.account_date)) ");
		query.append("  and u.id=cd.courier_id order by DATE(FROM_UNIXTIME(cd.account_date))");
		query.append("  limit ? , ? ");
		logger.info("query:{}", query.toString());
		return findForJdbc(query.toString(), courierId, start, end);
	}

	/*
	 * 插入申请提现记录
	 * (non-Javadoc)
	 * @see com.courier_mana.personal.service.WithDrawService#applyFor(java.lang.Integer, java.lang.Long, java.lang.Double, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> applyFor(Integer courierId,
			Long bankCardId, Double getMoney, String password) {
		List<Map<String, Object>> list = null;
		if (courierId == null && bankCardId == null && getMoney == null) {
			throw new IllegalArgumentException("applyFor:参数错误");

		}
		StringBuilder query = new StringBuilder();
		query.append("  select u.money,u.user_type as userType from `user` u where u.id=? and u.password= ?");
		list = findForJdbc(query.toString(), courierId, password);
		if (list.size()==0) {
			throw new IllegalArgumentException("密码错误！");
		} else {
			// 取款之前
			Double beforeMoney = (Double) list.get(0).get("money");
			// 取款之后
			Double afterMoney = beforeMoney - getMoney;

			if (afterMoney < 0) {
				throw new IllegalArgumentException("您的余额不足！");
			} else {

				StringBuilder queryInsert = new StringBuilder();
				queryInsert
						.append("  INSERT into withdrawals (user_id,submit_time,money,bankcard_id,user_type,before_money,after_money) values(?,?,?,?,?,?,?) ");
				//现在时间
				Long now = System.currentTimeMillis()/1000;
				String userType = (String) list.get(0).get("userType");
				int listInsert = executeSql(queryInsert.toString(), courierId,
						now, getMoney,bankCardId , userType, beforeMoney,
						afterMoney);
				if (listInsert == 0) {
					throw new IllegalArgumentException("插入失败！");
				}
				//查询当天已经的插入次数
				Map<String, Object> map = new HashMap<String, Object>();
			
				map.put("insertResult", listInsert);
				list.add(map);

			}
		}
		return list;
	}
	

	public List<Map<String,Object>> getWithRecord(Integer courierId,Integer page,Integer rows){
		if(page==null){
			page = 1;
			rows = 5;
		}
		
		if(courierId==null){
			throw new IllegalArgumentException("参数错误");
			
		}
		StringBuilder query = new StringBuilder();
		query.append("   select");
		query.append(" complete_time as completeTime,state,money,submit_time as submitTime,cancel_time as cancelTime from withdrawals where user_id=?");
		return this.findForJdbcParam(query.toString(), page, rows, courierId);
		
	}
}
