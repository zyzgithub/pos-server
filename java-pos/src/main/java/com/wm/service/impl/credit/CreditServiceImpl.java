package com.wm.service.impl.credit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.credit.CreditEntity;
import com.wm.service.credit.CreditServiceI;
import com.wm.util.StringUtil;

@Service("creditService")
@Transactional
public class CreditServiceImpl extends CommonServiceImpl implements CreditServiceI {

	@Override
	public List<CreditEntity> getListByCondition(Integer userId,Integer detailId,String action,Boolean justToday) {
		String hql="from CreditEntity as c where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (userId!=null && userId>0) {
			hql += " AND c.wuser.id = ? ";
			params.add(userId);
		}
		if (detailId!=null && detailId.intValue()>0) {
			hql += " AND c.detailId= ? ";
			params.add(detailId);
		}
		if (!StringUtil.isEmpty(action)) {
			hql += " AND c.action= ? ";
			params.add(action);
		}
		if (justToday!=null && justToday) {
			Calendar calendar=Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			Integer start=(int) (calendar.getTime().getTime()/1000);
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			Integer end=(int) (calendar.getTime().getTime()/1000);
			
			hql += " AND  c.createTime BETWEEN ? AND ? ";
			params.add(start);
			params.add(end);
		}
		if (params.size()>0) {
			return this.findHql(hql, params.toArray());
		}else{
			return this.findHql(hql);
		}
	}
	
}