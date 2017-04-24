package com.wm.service.impl.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.AppTypeConstants;
import com.courier_mana.common.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpush.JPush;
import com.wm.entity.jpush.JPushLogEntity;
import com.wm.entity.notice.NoticeEntity;
import com.wm.entity.notice.NoticeUserEntity;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.courierorg.CourierOrgServiceI;
import com.wm.service.notice.NoticeServiceI;

@Service("noticeService")
public class NoticeServiceImpl extends CommonServiceImpl implements NoticeServiceI {
	private static final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);
	private final static int LIMIT = 300;
	
	@Autowired
	private CourierOrgServiceI courierOrgService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	 protected static Gson _gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	@Override
	public List<Integer> getUserIds(String sql) {
		List<Integer> userIds = new ArrayList<Integer>();
		try {
			List<Map<String, Object>> idsMap = this.findForJdbc(sql);
			if(CollectionUtils.isNotEmpty(idsMap)){
				for(Map<String, Object> idMap: idsMap){
					userIds.add(Integer.parseInt(idMap.get("id").toString()));
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return userIds;
	}

	@Override
	public NoticeEntity getNotice(Integer noticeId) {
		if(noticeId == null){
			return null;
		}
		return this.getEntity(NoticeEntity.class, noticeId);
	}

	
	@Override
	public void push(NoticeEntity noticeEntity, List<Integer> userIds){
		//分页发送消息
		pushByPage(noticeEntity, userIds);
	}
	
	@Override
	public void push(Integer noticeId){
		NoticeEntity noticeEntity = this.getNotice(noticeId);
		if(noticeEntity != null){
			this.push(noticeEntity);
		}
	}
	@Override
	public void push(NoticeEntity noticeEntity){
		try {
			//快递员消息
			if(noticeEntity.getNoticeObject().intValue() == 3){
				List<Integer> courierIds = new ArrayList<Integer>();
				
				//推个平台快递员
				if(noticeEntity.getCourierType().equals(Constants.COURIER)){
				//获取组织架构下的所有快递员ID
				courierIds = courierOrgService.queryCouriersByParentOrgId(noticeEntity.getOrgId());
				}
				
//				//推给合作商快递员
//				if(noticeEntity.getCourierType().equals(Constants.COURIER_COOPERATE_BUSINESS)){
//					
//				}
				
				//推给众包快递员
				if(noticeEntity.getCourierType().equals(Constants.COURIER_CROWDSOURING)){
					courierIds = courierInfoService.getCouriersByCourierType(noticeEntity.getCourierType()); 
				}
				
				//分页发送消息
				pushByPage(noticeEntity, courierIds);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页推送消息
	 * @param noticeEntity
	 * @param userIds
	 */
	private void pushByPage(NoticeEntity noticeEntity, List<Integer> userIds){
		int totalPage = userIds.size() % LIMIT == 0?userIds.size()/LIMIT:userIds.size()/LIMIT+1;
		
		for(int pageNo = 1; pageNo <= totalPage; pageNo++){
			List<Integer> partCourierIds = this.getParts(userIds, pageNo);
			String[] targets = new String[partCourierIds.size()];
			for (int i = 0; i < targets.length; i++) {
				targets[i] = partCourierIds.get(i).toString();
			}
			
			String result = JPush.push(targets, noticeEntity.getTitle(), 
					noticeEntity.getContent(), "", buildExtras(noticeEntity));
			logger.info("极光推送消息，消息ID:{}, 标题:{}: 标题:{}, 推送结果:", new Object[]{noticeEntity.getId(), 
					noticeEntity.getTitle(), StringUtils.join(partCourierIds, ","), result});
			afterPush(noticeEntity, partCourierIds, result);
		}
	}
	
	@Override
	public void afterPush(NoticeEntity noticeEntity, List<Integer> userIds, String result){

		if(CollectionUtils.isNotEmpty(userIds)){
			//保存用户消息
			List<NoticeUserEntity> noticeUserEntities = new ArrayList<NoticeUserEntity>();
			for(Integer userId: userIds){
				NoticeUserEntity entity = new NoticeUserEntity();
				entity.setNoticeId(noticeEntity.getId());
				entity.setUserId(userId);
				entity.setReadStatus(0);
				
				noticeUserEntities.add(entity);
			}
			this.batchSave(noticeUserEntities);
			
			//保存推送日志
			Map<String, String> pushMap = new HashMap<String, String>();
			pushMap.put("noticeId", noticeEntity.getId().toString());
			JPushLogEntity jpushLog = new JPushLogEntity(noticeEntity.getTitle(), noticeEntity.getContent(), "", StringUtils.join(userIds, ","), pushMap.toString());
			jpushLog.setResultTime(DateUtils.getSeconds());
			jpushLog.setResult(result);
			this.save(jpushLog);
		}
	}
	
	private Map<String, String> buildExtras(NoticeEntity noticeEntity){
		Map<String, String> extras = new HashMap<String, String>();
		if(noticeEntity.getNoticeObject().intValue() == 3){
			extras.put("appType", AppTypeConstants.APP_TYPE_COURIER);
		}
		extras.put("noticeId", noticeEntity.getId().toString());
		return extras;
	}
	
	private List<Integer> getParts(List<Integer> courierIds, int pageNo){
		List<Integer> targets = new ArrayList<Integer>();
		int start = (pageNo-1) * LIMIT;
		int end = pageNo*LIMIT > courierIds.size()?courierIds.size(): pageNo*LIMIT;
		
		for (int i = start; i < end; i++) {
			targets.add(courierIds.get(i));
		}
		return targets;
	}

	/**
	 * 推送审核商家申请结果消息
	 */
	@Override
	public void pushAudit(NoticeEntity noticeEntity, Integer userId) {
		try {
			//快递员消息
			if(noticeEntity.getNoticeObject().intValue() == 3){
				String[] targets = new String[]{userId.toString()};
				String result = JPush.push(targets, noticeEntity.getTitle(), 
						noticeEntity.getContent(), "", buildExtras(noticeEntity));
				logger.info("极光推送消息，消息ID:{}, 标题:{}: 标题:{}, 推送结果:", new Object[]{noticeEntity.getId(), 
						noticeEntity.getTitle(), userId, result});
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
