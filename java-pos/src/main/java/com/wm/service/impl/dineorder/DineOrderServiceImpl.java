package com.wm.service.impl.dineorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.dineorder.DineOrderEntity;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.service.dineorder.DineOrderServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.util.AliOcs;

@Service("dineOrderService")
@Transactional
public class DineOrderServiceImpl extends CommonServiceImpl implements DineOrderServiceI {
	
	private static final Logger logger = Logger.getLogger(DineOrderServiceImpl.class);
	
	@Autowired
	private OrderServiceI orderService;
	
	@Override
	public int createDineOrder(int merchantId,String params,String timeRemark,double totalOrigin) {
		//获取城市id
		MerchantEntity merchant = this.get(MerchantEntity.class,merchantId);
		int cityId = merchant.getCityId();
		
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<String, Object>();
		int menuId = 0;
		int num = 0;
		int orderid = 0;
		double price = 0.0;
		double origin = 0.0;
		double total_price = 0.0;

		paramList = JSONHelper.toList(params);

		if (params != null && paramList.size() > 0) {
			for (int i = 0; i < paramList.size(); i++) {
				m = paramList.get(i);
				menuId = Integer.valueOf(m.get("menuId").toString());
				num = Integer.valueOf(m.get("num").toString());

				// 根据菜单id获取单价
				MenuEntity menu = this.getEntity(MenuEntity.class,
						menuId);
				price = menu.getPrice();
				// 计算总金额
				origin += num * price;
			}
			
			// 金额校验
			if (origin <= 0 || origin != totalOrigin) {
				return 0;
			}

			DineOrderEntity dineOrder = new DineOrderEntity();
			dineOrder.setCityId(cityId);
			dineOrder.setCreateTime(DateUtils.getSeconds());
			dineOrder.setMerchant(merchant);
			dineOrder.setOrigin(totalOrigin);

			dineOrder.setDineType(1);
			dineOrder.setSuccessTime(DateUtils.getSeconds());
			dineOrder.setTimeRemark(timeRemark);
			
			this.save(dineOrder);
			
			String payid = RandomStringUtils.random(4, "0123456789")
					+ Long.toString(dineOrder.getCreateTime() + dineOrder.getId()).substring(2);
			
			dineOrder.setPayId(payid);
			this.saveOrUpdate(dineOrder);
			
			this.setOrderNum(dineOrder);
			
			orderid = dineOrder.getId();

			// 添加dine_order_menu关联信息
			for (int i = 0; i < paramList.size(); i++) {
				m = paramList.get(i);
				menuId = Integer.valueOf(m.get("menuId").toString());
				num = Integer.valueOf(m.get("num").toString());

				// 根据菜单id获取单价
				MenuEntity menu = this.getEntity(MenuEntity.class,
						menuId);
				price = menu.getPrice();
				// 总价
				total_price = num * price;

				String o_msql = "insert into dine_order_menu (MENU_ID, ORDER_ID, PRICE, QUANTITY, TOTAL_PRICE) values(?, ?, ?, ?, ?)";
				this.executeSql(o_msql, menuId,orderid,price,num,total_price);
			}

			return orderid;
		} else {
			return 0;
		}
	}
	
	private void setOrderNum(DineOrderEntity order){
		//判断是否已经生成排号
		if(order.getOrderNum()==null ||"".equals(order.getOrderNum())){
			logger.info("----------餐厅订单生成排号-------");
			order.setOrderNum(AliOcs.genOrderNum(order.getMerchant().getId().toString()));//生成排号
			this.saveOrUpdate(order);//保存排号
		}
	}
	

	@Override
	public List<Map<String, Object>> getDineOrderByDineType(int dineType,int merchantId,int pageNo,int row) {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();

		String sql = "SELECT d.id FROM dine_order d where d.merchant_id=" + merchantId;
		sql += " and FROM_UNIXTIME(d.success_time,'%Y%m%d')=" + DateTime.now().toString("yyyyMMdd");
		if (dineType > 0 && dineType < 5) {
			if (dineType == 4) {
				sql += " and d.dine_type in('1','2') ";
			}else{
				sql += " and d.dine_type=" + dineType;
			}
		}
		sql += "  ORDER BY dine_type DESC, d.create_time ASC ";
		list = this.findForJdbc(sql, pageNo, row);
		
		if(list.size()==0){
			return list;
		}
		
		List<Map<String, Object>> OrderList=new ArrayList<Map<String,Object>>();
		

		if(list!=null && list.size()>0){
			 String ids="";
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map=list.get(i);
				int id=Integer.parseInt(map.get("id").toString());
				
				 ids=ids+new StringBuffer().append(id+",").toString();
		
			}
			String orderId=ids.substring(0,ids.length()-1);
			sql="SELECT d.id,d.origin, FROM_UNIXTIME(d.create_time,'%Y-%m-%d %H:%i') create_time," 
					+"FROM_UNIXTIME(d.complete_time,'%Y-%m-%d %H:%i') complete_time,d.time_remark," 
					+"d.merchant_id,CONVERT(SUBSTRING(d.order_num,9), UNSIGNED) order_num,d.pay_id,d.city_id,d.dine_type,ifnull(d.success_time,0) success_time," 
					+"ifnull(d.accept_time,0) accept_time FROM dine_order d where d.id in ("+orderId+")";
				List<Map<String, Object>> dineOrderList=new ArrayList<Map<String,Object>>();
				dineOrderList=this.findForJdbc(sql);
				
			sql="SELECT CONCAT((SELECT s.value FROM system_config s WHERE s.code='menu_url'),m.image) image,d.id,m.`name`,d.quantity,d.price,d.order_id " 
						+"from dine_order_menu d JOIN menu  m ON d.menu_id=m.id WHERE d.order_id in ("+orderId+")";
					List<Map<String, Object>> menuList=new ArrayList<Map<String,Object>>();
					menuList=this.findForJdbc(sql);

				for (int i = 0; i < dineOrderList.size(); i++) {
					Map<String, Object> dineMap=dineOrderList.get(i);
					Map<String, Object> orderMap=new HashMap<String, Object>();
					//堂食订单信息
					orderMap.put("id", dineMap.get("id"));
					orderMap.put("origin", dineMap.get("origin"));
					orderMap.put("create_time", dineMap.get("create_time"));
					orderMap.put("complete_time", dineMap.get("complete_time"));
					orderMap.put("time_remark", dineMap.get("time_remark"));
					orderMap.put("merchant_id", dineMap.get("merchant_id"));
					orderMap.put("order_num", dineMap.get("order_num"));
					orderMap.put("pay_id", dineMap.get("pay_id"));
					orderMap.put("city_id", dineMap.get("city_id"));
					orderMap.put("dine_type", dineMap.get("dine_type"));
					orderMap.put("success_time", dineMap.get("success_time"));

					//订单菜品信息
					List<Map<String, Object>> menu=new ArrayList<Map<String,Object>>();
					for (int j = 0; j < menuList.size(); j++) {
						Map<String, Object> map=menuList.get(j);
						int oId=Integer.parseInt(dineMap.get("id").toString());//堂食订单ID
						int mId=Integer.parseInt(map.get("order_id").toString());//菜单表堂食订单ID
						if(oId==mId){
							menu.add(map);
						}
						
					}
					orderMap.put("menuList", menu);
					OrderList.add(orderMap);
				}
			}
		
		return OrderList;
	}

}