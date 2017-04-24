package com.wm.service.impl.courierdevelop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.courier.dto.CourierDevMerchantPhase;
import com.wm.controller.courier.dto.DevMerchantPhase;
import com.wm.controller.courierdevelop.dto.MerchantDevRecDTO;
import com.wm.entity.merchantdev.MerchantDevDtlEntity;
import com.wm.entity.merchantdev.MerchantDevPhaseEntity;
import com.wm.entity.merchantdev.MerchantDevelopmentEntity;
import com.wm.service.courierdevlop.CourierMerchantDepPhaseServiceI;

@Service("courierMerchantDepPhaseService")
@Transactional
public class CourierMerchantDepPhaseServiceImpl extends CommonServiceImpl
		implements CourierMerchantDepPhaseServiceI {

	private static final Logger logger = LoggerFactory
			.getLogger(CourierMerchantDepPhaseServiceImpl.class);

	private int totalStates = 7;

	/**
	 * 实体类是否是阶段
	 * 
	 * @param entity
	 * @return
	 */
	protected boolean isPhase(MerchantDevPhaseEntity entity) {
		return entity.getParentId() == null;
	}

	/**
	 * 实体类是否是阶段phase的子任务
	 * 
	 * @param phase
	 * @param entity
	 * @return
	 */
	protected boolean isSubTaskOfPhase(DevMerchantPhase phase,
			MerchantDevPhaseEntity entity) {
		return entity.getParentId() != null
				&& entity.getParentId().equals(phase.getId());
	}

	/**
	 * 获取某一个阶段、子任务的排序号
	 * 
	 * @param id
	 * @return 当根据id找不到对应的阶段、子任务时返回空
	 */
	public Integer getOrderNo(Integer id) {
		MerchantDevPhaseEntity entity = this.get(MerchantDevPhaseEntity.class,
				id);
		if (entity != null) {
			return entity.getOrderNo();
		}
		return null;
	}

	@Override
	public List<DevMerchantPhase> getDevMerchantDefinition() {
		String hql = "select t from MerchantDevPhaseEntity t";
		List<MerchantDevPhaseEntity> entities = this.findHql(hql);

		List<DevMerchantPhase> phases = new ArrayList<DevMerchantPhase>();
		if (CollectionUtils.isNotEmpty(entities)) {
			// 找出所有的阶段
			for (MerchantDevPhaseEntity entity : entities) {
				// 是阶段
				if (isPhase(entity)) {
					DevMerchantPhase phase = new DevMerchantPhase();
					phase.setId(entity.getId());
					phase.setName(entity.getName());
					phase.setOrderNo(entity.getOrderNo());
					phase.setChecked(false);

					phases.add(phase);
				}
			}

			if (CollectionUtils.isNotEmpty(phases)) {
				// 为每个阶段配上子任务
				for (DevMerchantPhase phase : phases) {
					for (MerchantDevPhaseEntity entity : entities) {
						// 是当前阶段的子任务
						if (isSubTaskOfPhase(phase, entity)) {
							List<DevMerchantPhase> subTasks = phase
									.getSubTasks();
							if (subTasks == null) {
								subTasks = new ArrayList<DevMerchantPhase>();
								phase.setSubTasks(subTasks);
							}
							DevMerchantPhase subTask = new DevMerchantPhase();
							subTask.setId(entity.getId());
							subTask.setName(entity.getName());
							subTask.setOrderNo(entity.getOrderNo());
							subTask.setChecked(false);

							subTasks.add(subTask);
						}
					}
				}
			}
		}
		return phases;
	}

	/**
	 * 根据当前的阶段ID、子任务ID初始化阶段定义的cheched状态
	 * 
	 * @param ongoingStageId
	 * @param subTaskId
	 * @return
	 */
	private List<DevMerchantPhase> initCheckedStateOfMerchantPhaseDef(
			Integer ongoingStageId, Integer subTaskId) {
		// 获取商家录入的定义
		List<DevMerchantPhase> phases = getDevMerchantDefinition();
		// 获取阶段的排序号
		Integer stageOrderNo = this.getOrderNo(ongoingStageId);
		// 获取子任务的排序号
		Integer subTaskOrderNo = this.getOrderNo(subTaskId);

		// 无法确定排序号
		if (stageOrderNo == null || subTaskOrderNo == null) {
			logger.info("无法根据ongoingStageId:{}, subTaskId:{}获取对应的阶段、子任务", stageOrderNo, subTaskOrderNo);
			throw new RuntimeException(
					"stagetOrderNo == null || subTaskOrderNo == null");
		}

		// 从第一个阶段到商家录入最后一个阶段前一个阶段
		for (int i = 0; i < stageOrderNo.intValue() - 1; i++) {
			// 把阶段设置为完成
			phases.get(i).setChecked(true);

			// 把阶段的子任务设置为完成
			List<DevMerchantPhase> subTasks = phases.get(i).getSubTasks();
			if (CollectionUtils.isNotEmpty(subTasks)) {
				for (DevMerchantPhase subTask : subTasks) {
					subTask.setChecked(true);
				}
			}
		}

		// 最后一个阶段
		DevMerchantPhase ongoingStage = phases.get(stageOrderNo.intValue() - 1);
		// 最后一个阶段设置完成的子任务
		List<DevMerchantPhase> subTasks = ongoingStage.getSubTasks();
		for (int i = 0; i < subTaskOrderNo; i++) {
			subTasks.get(i).setChecked(true);
		}
		return phases;
	}

	/**
	 * 获取阶段定义中打钩（已完成的子任务）id连接字符串
	 * 
	 * @param phases
	 * @return
	 */
	private String getCheckSubTaskIds(List<DevMerchantPhase> phases) {
		StringBuilder checkedSubTaskIdsSb = new StringBuilder();
		for (DevMerchantPhase phase : phases) {
			List<DevMerchantPhase> subTasks = phase.getSubTasks();
			for (DevMerchantPhase subTask : subTasks) {
				if (!subTask.isChecked()) {
					break;
				}
				checkedSubTaskIdsSb.append(subTask.getId()).append("_");
			}
		}

		String checkedSubTaskIds = checkedSubTaskIdsSb.toString();
		int len = checkedSubTaskIds.length() - 1;
		if (checkedSubTaskIds.charAt(len) == '_') {
			checkedSubTaskIds = checkedSubTaskIds.substring(0, len);
		}
		return checkedSubTaskIds.toString();
	}

	@Override
	public CourierDevMerchantPhase getCourierDevMerchantPhase(Integer devId) {
		CourierDevMerchantPhase courierDevMerchantPhase = null;
		MerchantDevelopmentEntity entity = this.get(
				MerchantDevelopmentEntity.class, devId);
		if (entity != null) {
			courierDevMerchantPhase = new CourierDevMerchantPhase();
			courierDevMerchantPhase.setDevId(devId);
			courierDevMerchantPhase.setMerchantName(entity.getMerchantTitle());
			courierDevMerchantPhase.setMerchantHolder(entity.getMerchantHolder());
			courierDevMerchantPhase.setMerchantMobile(entity.getMerchantMobile());
			courierDevMerchantPhase.setRemark(entity.getRemark());
			courierDevMerchantPhase.setCourierId(entity.getCourierId());

			courierDevMerchantPhase.setCurComletePhaseId(entity.getOngoingStage());
			Integer phaseOrderNo = this.getOrderNo(entity.getOngoingStage());
			courierDevMerchantPhase.setCurComletePhaseOrderNo(phaseOrderNo);

			courierDevMerchantPhase.setCurCompleteSubTaskId(entity.getSubTaskId());
			courierDevMerchantPhase.setCurCompleteSubTaskOrderNo(getOrderNo(entity.getSubTaskId()));

			Integer ongoingStageId = entity.getOngoingStage();
			Integer subTaskId = entity.getSubTaskId();

			List<DevMerchantPhase> phases = initCheckedStateOfMerchantPhaseDef(ongoingStageId, subTaskId);
			courierDevMerchantPhase.setDevMerchantPhases(phases);
			courierDevMerchantPhase.setCheckedSubTasks(getCheckSubTaskIds(phases));

		}
		return courierDevMerchantPhase;
	}

	@Override
	public List<Map<String, Object>> getCourierDevMerchantHistory(
			Integer courierId, int page, int rows) {

		StringBuilder query = new StringBuilder();
		query.append("SELECT dev_id, merchant_title,ongoing_stage, sub_task, courier_id,create_date, done_date, state ");
		query.append("FROM 0085_merchant_development ");
		query.append(" where courier_id = ?  ");
		query.append(" order by create_date DESC ");
		List<Map<String, Object>> list = findForJdbcParam(query.toString(),page, rows, courierId);
		Map<Integer, String> stagesMap = queryDevPhaseNames();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			map.put("id", map.get("dev_id"));
			map.put("stage", stagesMap.get(Integer.parseInt(map.get("ongoing_stage").toString())));
			map.put("targetMerchant", map.get("merchant_title"));
			if (map.get("state") != null && (map.get("state").equals(1)||map.get("state").equals(true))) {
				map.put("status", "已完成");
			} else {
				map.put("status", "进行中");
			}
			// 时间处理
			String createTime = list.get(i).get("create_date").toString();
			String updateTime = list.get(i).get("done_date").toString();
			createTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S")
					.parseDateTime(createTime).toString("yyyy年MM月dd日 HH:mm:ss");
			updateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S")
					.parseDateTime(updateTime).toString("yyyy年MM月dd日 HH:mm:ss");
			map.put("createTime", createTime);
			map.put("updateTime", updateTime);
			list.set(i, map);
		}
		return list;
	}

	/**
	 * 获取录入子阶段的额外信息
	 * @param vo
	 * @return
	 */
	private Map<String, Object> getEnterSubtaskExtraInfos(MerchantDevRecDTO vo){
		if(StringUtils.isEmpty(vo.getCheckString())){
			return null;
		}
		String[] ids = vo.getCheckString().split("_");
		Arrays.sort(ids);
		
		String maxId = ids[ids.length - 1];
		Map<String, Object> maxMap = queryDevPhaseById(Integer.parseInt(maxId));
		
		// 记录最大任务的id ，所在阶段，以及在该阶段的排序
		int maxStage = Integer.parseInt(maxMap.get("parent_id").toString());
		int lastOrder = Integer.parseInt(maxMap.get("order_no").toString());
		// 判断这个阶段是还有此次最大id排序要后的id，如果有，表示该阶段还没有完成，反之该阶段完成。
		List<Map<String, Object>> li = queryStageLastList(maxStage, lastOrder);
		if (li == null || li.size() == 0) {
			maxMap.put("finished", true);
		}
		else {
			maxMap.put("finished", false);
		}
		
		maxMap.put("ids", ids);
		return maxMap;
	}
	
	//对招商录入时输入的信息进行校验
	private AjaxJson validate(MerchantDevRecDTO vo){
		AjaxJson json = new AjaxJson();
		if (vo.getCourierId() == 0) {
			json.setMsg("快递员Id不能为空");
			json.setSuccess(false);
			json.setStateCode("02");
			return json;
		}
		
		if(StringUtils.isEmpty(vo.getCheckString())){
			json.setMsg("没有勾选完成的子任务");
			json.setSuccess(false);
			json.setStateCode("02");
			return json;
		}
		
		Map<String, Object> subTaskExtraInfos = getEnterSubtaskExtraInfos(vo);
		String[] ids = (String[])subTaskExtraInfos.get("ids");
		Map<String, String> idMap = new HashMap<String, String>();
		for (int i = 0; i < ids.length; i++) {
			idMap.put(ids[i], "");
		}
		
		// 记录最大任务的id ，所在阶段，以及在该阶段的排序
		Integer maxTask = Integer.parseInt(subTaskExtraInfos.get("id").toString());

		List<Map<String, Object>> list = queryDevPhaseByStage(maxTask);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> rtmMap = list.get(i);
			String id = rtmMap.get("id").toString();
			// 如果该阶段中排序小的还没有在idMap中，那么就是跨任务了
			if (idMap.get(id) == null) {
				// 获取未完成的任务所在的阶段
				Map<String, Object> s = queryDevPhaseById(Integer.parseInt(id));
				json.setMsg("阶段" + s.get("parent_id").toString()+ "中 前面还有任务未完成 ！");
				json.setSuccess(false);
				json.setStateCode("02");
				return json;
			}
		}
		return json;
	}
	public AjaxJson updateCourierDevPhase(MerchantDevRecDTO vo) {
		AjaxJson json = new AjaxJson();
		int lastOrder = 0;
		try {
			json = validate(vo);
			if(!json.isSuccess()){
				return json;
			}
			
			Map<String, Object> subTaskExtraInfos = getEnterSubtaskExtraInfos(vo);
			Integer maxTask = Integer.parseInt(subTaskExtraInfos.get("id").toString());
			int maxStage = Integer.parseInt(subTaskExtraInfos.get("parent_id").toString());
			boolean finished = (boolean)subTaskExtraInfos.get("finished");
			
			// 保存招商录入实体
			MerchantDevelopmentEntity entity = null;
			if (vo.getId() != 0) {
				// 更新
				Map<Integer, String> stagesMap = queryDevPhaseNames();
				entity = this.get(MerchantDevelopmentEntity.class, vo.getId());
				if(entity.getState().intValue() == 1){
					json.setMsg("您已经成功完成该招商，请不要重复提交。");
					json.setSuccess(false);
					json.setStateCode("06");
					return json;
				}
				entity.setSubTaskId(maxTask);
				entity.setOngoingStage(maxStage);
				entity.setSubTaskId(maxTask);
				entity.setSubTask(lastOrder);
				entity.setRemark(vo.getRemark());
				entity.setStageName(stagesMap.get(maxStage));
				if (maxStage == totalStates && finished) {
					entity.setState(1);// 招商录入所有任务完毕
				} else {
					entity.setState(0);// 招商录入还有任务未完毕
				}
				entity.setDoneDate(DateUtils.gettimestamp());
				saveOrUpdate(entity);
				List<MerchantDevDtlEntity> dtlEntities = this.findByProperty(MerchantDevDtlEntity.class, "devId", vo.getId());
				// 更新招商录入阶段进展情况
				for (int i = 0; i < dtlEntities.size(); i++) {
					MerchantDevDtlEntity et = dtlEntities.get(i);
					if (et.getRecordStage() < maxStage) {
						et.setStatus(1);
					} else if (et.getRecordStage() == maxStage && finished) {
						et.setStatus(1);
					}
					save(et);
				}
			}
			json.setMsg("保存招商录入成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存招商录入失败");
			json.setSuccess(true);
			json.setStateCode("01");
		}
		return json;
	}

	@Override
	public AjaxJson createCourierDevPhase(MerchantDevRecDTO vo) {
		AjaxJson json = new AjaxJson();
		int lastOrder = 0;
		try{
			//先校验
			json = validate(vo);
			if(!json.isSuccess()){
				return json;
			}
			
			//获取
			Map<String, Object> subTaskExtraInfos = getEnterSubtaskExtraInfos(vo);
			Integer maxTask = Integer.parseInt(subTaskExtraInfos.get("id").toString());
			int maxStage = Integer.parseInt(subTaskExtraInfos.get("parent_id").toString());
			boolean finished = (boolean)subTaskExtraInfos.get("finished");
			
			Map<Integer, String> stagesMap = queryDevPhaseNames();
			// 新增招商录入记录实体
			MerchantDevelopmentEntity entity = new MerchantDevelopmentEntity();
			entity.setCourierId(vo.getCourierId());
			entity.setMerchantTitle(vo.getTargetMerchant());
			entity.setMerchantHolder(vo.getMerchantHolder());
			entity.setMerchantMobile(vo.getMerchantPhone());
			entity.setOngoingStage(maxStage);
			entity.setSubTaskId(maxTask);
			entity.setSubTask(lastOrder);
			entity.setCreateDate(DateUtils.gettimestamp());
			entity.setRemark(vo.getRemark());
			entity.setDoneDate(DateUtils.gettimestamp());
			entity.setStageName(stagesMap.get(maxStage));
			if (maxStage == totalStates && finished) {
				entity.setState(1);// 招商录入完毕
			} else {
				entity.setState(0);// 招商录入未完毕
			}
			save(entity);
			int devId = entity.getDevId();
			// 新增招商录入阶段进展情况实体
			Map<Integer, String> tasksMap = queryStateTaskName();
			for (int i = 1; i <= totalStates; i++) {
				MerchantDevDtlEntity dtlEntity = new MerchantDevDtlEntity();
				dtlEntity.setDevId(devId);
				if (i < maxStage) {
					dtlEntity.setStatus(1);
				} else if (i == maxStage && finished) {
					dtlEntity.setStatus(1);
				} else {
					dtlEntity.setStatus(0);
				}
				dtlEntity.setRecordStage(i);
				dtlEntity.setStateName(stagesMap.get(i));
				dtlEntity.setStageTask(tasksMap.get(i));
				save(dtlEntity);
			}
			json.setMsg("保存招商录入成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存招商录入失败");
			json.setSuccess(true);
			json.setStateCode("01");
		}
		return json;
	}

	@Override
	public Map<String, String> validate(Integer devId, List<Integer> subTaskIds) {
		Map<String, String> result = new HashMap<String, String>();
		MerchantDevelopmentEntity entity = this.get(MerchantDevelopmentEntity.class, devId);
		if (entity == null) {
			result.put("success", "false");
			result.put("errMsg", "子任务ID非法");
			result.put("errParams", StringUtils.join(subTaskIds, ","));
			return result;
		}

		List<DevMerchantPhase> devMerchantPhases = initCheckedStateOfMerchantPhaseDef(
				entity.getOngoingStage(), entity.getSubTaskId());
		result = this.validate(subTaskIds, devMerchantPhases);
		if (StringUtils.equals("false", result.get("success"))) {
			return result;
		} else {
			Integer minSubTaskId = Integer.parseInt(result.get("minSubTaskId")
					.toString());
			Integer originSubTaskId = entity.getSubTaskId();

			if (!this.isNeighbourSubTask(minSubTaskId, originSubTaskId)) {
				result.put("success", "false");
				result.put("errMsg", "子任务ID非法");
				result.put("errParams", StringUtils.join(subTaskIds, ","));
				return result;
			} else {
				result.put("success", "true");
			}
			return result;
		}
	}

	@Override
	public Integer getSubTaskMaxOrderNo(Integer phaseId) {
		return findOneForJdbc("SELECT max(p.order_no) from 0085_merchant_dev_phase p where p.parent_id=?",
				Integer.class, phaseId);
	}

	@Override
	public boolean isNeighbourSubTask(Integer subTaskId1, Integer subTaskId2) {
		MerchantDevPhaseEntity subTask1 = this.get(
				MerchantDevPhaseEntity.class, subTaskId1);
		MerchantDevPhaseEntity subTask2 = this.get(
				MerchantDevPhaseEntity.class, subTaskId2);

		if (subTask1 == null || subTask2 == null) {
			return false;
		}

		if (subTask1.getParentId().equals(subTask2.getParentId())) {
			return Math.abs(subTask1.getOrderNo() - subTask2.getOrderNo()) == 1;
		} else {
			MerchantDevPhaseEntity phase1 = this.get(
					MerchantDevPhaseEntity.class, subTask1.getParentId());
			MerchantDevPhaseEntity phase2 = this.get(
					MerchantDevPhaseEntity.class, subTask2.getParentId());

			if (phase1.getOrderNo() > phase2.getOrderNo()) {
				int maxOrderNo = this.getSubTaskMaxOrderNo(phase2.getId());
				return subTask1.getOrderNo() == 1
						&& subTask2.getOrderNo() == maxOrderNo;
			} else {
				int maxOrderNo = this.getSubTaskMaxOrderNo(phase1.getId());
				return subTask2.getOrderNo() == 1
						&& subTask1.getOrderNo() == maxOrderNo;
			}
		}
	}

	@Override
	public Map<String, String> validate(List<Integer> subTaskIds, List<DevMerchantPhase> phases) {
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p1.id, p1.`name`, p2.order_no phase_order_no, p1.order_no sub_task_order_no ");
		sql.append(" from 0085_merchant_dev_phase p1, 0085_merchant_dev_phase p2 ");
		sql.append(" where p1.parent_id = p2.id ");
		sql.append("  and p1.id in (" + StringUtils.join(subTaskIds, ",") + ")");
		sql.append(" ORDER BY p2.order_no, p1.order_no");

		List<Map<String, Object>> subTasks = this.findForJdbc(sql.toString());

		if (subTaskIds.size() != subTasks.size()) {
			result.put("success", "false");
			result.put("errMsg", "子任务ID非法");
			result.put("errParams", StringUtils.join(subTaskIds, ","));
			return result;
		}

		int i = 0;
		Integer maxSubTaskId = -1;
		Integer maxPhaseOrderNo = -1;
		Integer maxSubTaskOrderNo = -1;
		Integer minSubTaskId = -1;

		if (phases == null) {
			phases = this.getDevMerchantDefinition();
		}

		for (Map<String, Object> subTaskMap : subTasks) {
			Integer subTaskId = Integer.parseInt(subTaskMap.get("id").toString());
			Integer phaseOrderNo = Integer.parseInt(subTaskMap.get("phase_order_no").toString());
			Integer subTaskOrderNo = Integer.parseInt(subTaskMap.get("sub_task_order_no").toString());
			if (i == 0) {
				minSubTaskId = subTaskId;
			}
			if (i == subTasks.size() - 1) {
				maxSubTaskId = subTaskId;
				maxPhaseOrderNo = phaseOrderNo;
				maxSubTaskOrderNo = subTaskOrderNo;
			}

			DevMerchantPhase phase = phases.get(phaseOrderNo - 1);
			DevMerchantPhase subTask = phase.getSubTasks().get(
					subTaskOrderNo - 1);
			subTask.setChecked(true);

			i++;
		}

		DevMerchantPhase maxPhase = phases.get(maxPhaseOrderNo - 1);
		for (int j = 0; j < maxSubTaskOrderNo; j++) {
			DevMerchantPhase subTask = maxPhase.getSubTasks().get(j);
			if (!subTask.isChecked()) {
				result.put("success", "false");
				result.put("errMsg", "子任务ID非法");
				result.put("errParams", StringUtils.join(subTaskIds, ","));
				return result;
			}
		}

		for (int j = 0; j < maxPhaseOrderNo - 1; j++) {
			List<DevMerchantPhase> subTaskList = phases.get(j).getSubTasks();
			for (DevMerchantPhase subTask : subTaskList) {
				if (!subTask.isChecked()) {
					result.put("success", "false");
					result.put("errMsg", "子任务ID非法");
					result.put("errParams", StringUtils.join(subTaskIds, ","));
					return result;
				}
			}
		}

		result.put("success", "true");
		result.put("maxPhaseOrderNo", maxPhaseOrderNo.toString());
		result.put("maxSubTaskOrderNo", maxSubTaskOrderNo.toString());
		result.put("minSubTaskId", minSubTaskId.toString());
		result.put("maxSubTaskId", maxSubTaskId.toString());
		return result;
	}

	public Map<Integer, String> queryDevPhaseNames() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT id,name  FROM 0085_merchant_dev_phase ");
		List<Map<String, Object>> list = findForJdbc(query.toString());
		Map<Integer, String> stagesMap = new HashMap<Integer, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> rsMap = list.get(i);
			int stage = Integer.parseInt(rsMap.get("id").toString());
			String name = rsMap.get("name").toString();
			stagesMap.put(stage, name);
		}
		return stagesMap;
	}

	public Map<String, Object> queryDevPhaseById(int id) {
		StringBuilder queryById = new StringBuilder();
		queryById.append("SELECT id,NAME,parent_id,order_no  FROM 0085_merchant_dev_phase ");
		queryById.append("  WHERE id= ?  ");
		return findOneForJdbc(queryById.toString(), id);
	}

	public List<Map<String, Object>> queryDevPhaseByStage(Integer id) {
		StringBuilder queryById = new StringBuilder();
		queryById.append("SELECT id,NAME,parent_id,order_no  FROM 0085_merchant_dev_phase ");
		queryById.append(" where id <? and parent_id is not null ");
		return findForJdbc(queryById.toString(), id);
	}

	public Map<String, Object> queryMerchantDevById(int id) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT dev_id, merchant_title,ongoing_stage, sub_task, courier_id,create_date, done_date,sub_task_id,task_finished,state ");
		query.append(" FROM 0085_merchant_development ");
		query.append(" where dev_id = ?  ");
		return findOneForJdbc(query.toString(), id);
	}

	public List<Map<String, Object>> queryStageLastList(Integer stage, Integer order) {
		StringBuilder queryById = new StringBuilder();
		queryById.append("SELECT id,NAME,parent_id,order_no  FROM 0085_merchant_dev_phase ");
		queryById.append("  where  parent_id= ? and order_no > ? ");
		return findForJdbc(queryById.toString(), stage.toString(), order.toString());
	}

	public Map<Integer, String> queryStateTaskName() {
		StringBuilder queryById = new StringBuilder();
		queryById.append("SELECT stage,stage_tasks FROM `0085_merchat_dev_tasks` ");
		List<Map<String, Object>> list = findForJdbc(queryById.toString());
		Map<Integer, String> tasks = new HashMap<Integer, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> taskMap = list.get(i);
			int stage = Integer.parseInt(taskMap.get("stage").toString());
			String task = taskMap.get("stage_tasks").toString();
			tasks.put(stage, task);
		}
		return tasks;
	}
}
