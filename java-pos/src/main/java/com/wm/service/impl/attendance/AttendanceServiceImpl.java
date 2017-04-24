package com.wm.service.impl.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.courier_mana.common.Constants;
import com.wm.controller.networkDevelop.NetworkDevelopController;
import com.wm.controller.takeout.vo.AttendanceVo;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.util.Dialect;
import com.wm.util.DistanceUtil;
import com.wm.util.PageList;

@Service("attendanceService")
@Transactional
public class AttendanceServiceImpl extends CommonServiceImpl implements AttendanceServiceI {

	private static final Logger logger = Logger.getLogger(AttendanceServiceImpl.class);
	
	@Autowired
	private OrgServiceI orgService;
	@Autowired
	private SystemconfigServiceI systemconfigService;
	
	@Override
	public List<AttendanceVo> queryByUserId(int userId, String startTime, String endTime, int page, int rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(
				"select ca.id, ca.user_id as userId, ca.longitude as longitude, ca.latitude as latitude, ")
				.append(" ca.type as type, ca.address as address, from_unixtime(ca.create_time , '%Y-%m-%d %H:%i:%s') as createTime,ca.device_info as deviceInfo, ")
				.append(" u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
				.append(" FROM ")
				.append(" 0085_courier_attendance ca, ")
				.append(" user as u ").append(" WHERE ")
				.append(" ca.user_id = ?  and DATE(FROM_UNIXTIME(ca.create_time)) >= ? and DATE(FROM_UNIXTIME(ca.create_time)) <= ? and ca.user_id = u.id")
				.append(" order by ca.id desc ");
		List<AttendanceVo> orvList = Lists.newArrayList();
		orvList = this.findObjForJdbc(sbsql.toString(), page, rows,
				AttendanceVo.class, userId,startTime,endTime);
		return orvList;
	}
	
	public boolean isOnDuty(Integer userId){
		String sql = "select type from 0085_courier_attendance where user_id=? "
				+ "and from_unixtime(create_time, '%y-%m-%d')=curdate() order by id desc limit 0,1";
		List<Map<String, Object>> atts = this.findForJdbc(sql, new Object[]{userId});
		if(atts != null && atts.size() > 0){
			Map<String, Object> map = atts.get(0);
			return "0".equals(map.get("type").toString()) ? true : false;
		}
		return false;
	}
	
	@Override
	public PageList findByEntityList(AttendanceVo attendance,Integer page,Integer rows){
		StringBuffer sql = new StringBuffer();
		sql.append(" select ca.id,ca.user_id as userId,ca.longitude,ca.latitude,ca.type,ca.address,ca.create_time as createTime,ca.device_info as deviceInfo,"
				+ "u.username as userName, u.photoUrl as headIcon, u.mobile as mobile from 0085_courier_attendance ca, user as u where 1=1 and ca.user_id = u.id");
		if (attendance.getId()!=0) {
			sql.append(" and ca.id = ");
			sql.append(attendance.getId());
		}
		if (attendance.getUserId()!=0) {
			sql.append(" and ca.user_id = ");
			sql.append(attendance.getUserId());
		}
		if (StringUtil.isNotEmpty(attendance.getLongitude())) {
			sql.append(" and ca.longitude = ");
			sql.append(attendance.getLongitude());
		}
		if (StringUtil.isNotEmpty(attendance.getLatitude())) {
			sql.append(" and ca.latitude = ");
			sql.append(attendance.getLatitude());
		}
		
		if (StringUtil.isNotEmpty(attendance.getType())) {
			sql.append(" and ca.type = ");
			sql.append("'");
			sql.append(attendance.getType());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(attendance.getAddress())) {
			sql.append(" and ca.address = ");
			sql.append("'");
			sql.append(attendance.getAddress());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(attendance.getDeviceInfo())) {
			sql.append(" and ca.device_info = ");
			sql.append("'");
			sql.append(attendance.getDeviceInfo());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(attendance.getStartTime())) {
			sql.append(" and DATE(FROM_UNIXTIME(ca.create_time)) >= ");
			sql.append("'");
			sql.append(attendance.getStartTime());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(attendance.getEndTime())) {
			sql.append(" and DATE(FROM_UNIXTIME(ca.create_time)) <= ");
			sql.append("'");
			sql.append(attendance.getEndTime());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(attendance.getUserName())) {
			sql.append(" and u.username = ");
			sql.append("'");
			sql.append(attendance.getUserName());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(attendance.getMobile())) {
			sql.append(" and u.mobile = ");
			sql.append("'");
			sql.append(attendance.getMobile());
			sql.append("'");
		}
		List<AttendanceVo> list = this.findObjForJdbc(sql.toString(), page,
				rows, AttendanceVo.class);
		Dialect dialect = new Dialect();
		String countSql = dialect.getCountString(sql.toString());
		List<Map<String, Object>> countList = this.findForJdbc(countSql);
		Map<String, Object> map = countList.get(0);
		String count = map.get("count").toString();
		PageList pageList = new PageList(rows, page, Integer.parseInt(count), list);
		return pageList;
	}
	
	
	@Override
	public List<Map<String, Object>> getAttendceRecords(Integer courierId, String calDate, Integer type){
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.user_id courierId,  DATE(FROM_UNIXTIME(a.create_time))  `date`,  FROM_UNIXTIME(a.create_time) punchTime,  a.type, a.address");
		sql.append(" from 0085_courier_attendance a ");
		sql.append(" where a.user_id = ? " );
		sql.append(" and DATE(FROM_UNIXTIME(a.create_time)) = ?");
		if(type != null){
			sql.append(" and type = ?" );
		}
		sql.append(" order by a.create_time");
		
		if(type != null){
			return this.findForJdbc(sql.toString(), courierId, calDate, type);
		}
		else {
			return this.findForJdbc(sql.toString(), courierId, calDate);
		}
	}
	
	
	@Override
	public List<Map<String, Object>> getAttendceRecords(Integer courierId, String startDate, String endDate, Integer type){
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.user_id courierId,  DATE(FROM_UNIXTIME(a.create_time))  `date`,  FROM_UNIXTIME(a.create_time) punchTime,  a.type, a.address");
		sql.append(" from 0085_courier_attendance a ");
		sql.append(" where a.user_id = ? " );
		sql.append(" and DATE(FROM_UNIXTIME(a.create_time)) >= ?");
		sql.append(" and DATE(FROM_UNIXTIME(a.create_time)) < ?");
		if(type != null){
			sql.append(" and type = ?" );
		}
		sql.append(" order by a.create_time");
		
		if(type != null){
			return this.findForJdbc(sql.toString(), courierId, startDate, endDate, type);
		}
		else {
			return this.findForJdbc(sql.toString(), courierId, startDate, endDate);
		}
	}
	
	
	/**
	 * 从某一个快递员的打卡记录中找出首次打卡上班或下班的记录
	 * @param courierId
	 * @param calDate
	 * @param punchRecords
	 * @return
	 */
	private Map<String, Object> getFirstPunchRecord(Integer courierId, String calDate, 
			List<Map<String, Object>> punchRecords) {
			if(CollectionUtils.isNotEmpty(punchRecords)){
				
				for(Map<String, Object> record: punchRecords){
					String attendenceDay = record.get("date").toString();
					
					if(attendenceDay.compareTo(calDate) > 0){
						break;
					}
					
					if(StringUtils.equals(calDate, attendenceDay)){
						return record;
					}
				}
			}
			return null;
	}
	
	@Override
	public Map<String, Object> getFirstPunchOndutyRecord(Integer courierId, String calDate, 
			List<Map<String, Object>> ondutyRecords){
		return getFirstPunchRecord(courierId, calDate, ondutyRecords);
	}
	
	@Override
	public Map<String, Object> getFirstPunchOffdutyRecord(Integer courierId, String calDate, List<Map<String, Object>> offdutyRecords){
		return getFirstPunchRecord(courierId, calDate, offdutyRecords);
	}
	
	@Override
	public Map<String, Object> getFirstPunchOndutyRecord(Integer courierId, String calDate){
		List<Map<String, Object>> punchOndutyRecords = this.getAttendceRecords(courierId, calDate, Constants.ONDUTY);
		if(CollectionUtils.isNotEmpty(punchOndutyRecords)){
			return punchOndutyRecords.get(0);
		}
		else {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> getFirstPunchOffdutyRecord(Integer courierId, String calDate){
		List<Map<String, Object>> punchOffdutyRecords = this.getAttendceRecords(courierId, calDate, Constants.OFFDUTY);
		if(CollectionUtils.isNotEmpty(punchOffdutyRecords)){
			return punchOffdutyRecords.get(0);
		}
		else {
			return null;
		}
	}
	
	
	
	@Override
	public double calPerDayWorkhours(Integer courierId, String calDate){
		List<Map<String, Object>> ondutyRecords = this.getAttendceRecords(courierId, calDate, Constants.ONDUTY);
		List<Map<String, Object>> offdutyRecords = this.getAttendceRecords(courierId, calDate, Constants.OFFDUTY);
		
		
		Map<String, Object> firstPunchOnduty = null;
		Map<String, Object> firstPunchOffduty = null;
		
		
		if(CollectionUtils.isNotEmpty(ondutyRecords)){
			firstPunchOnduty = ondutyRecords.get(0);
		}
		
		if(CollectionUtils.isNotEmpty(offdutyRecords)){
			firstPunchOffduty = offdutyRecords.get(0);
		}
		
		return this.calPerDayWorkhours(courierId, calDate, firstPunchOnduty, firstPunchOffduty);
	}
	
	
	@Override
	public double calPerDayWorkhours(Integer courierId, String calDate, Map<String, Object> firstPunchOnduty, Map<String, Object> firstPunchOffduty){
		if(firstPunchOnduty == null){
			return 0.0;
		}
		
		if(firstPunchOnduty != null && firstPunchOffduty == null){
			firstPunchOffduty = new HashMap<String, Object>();
			firstPunchOffduty.put("courierId", courierId);
			firstPunchOffduty.put("date", calDate);
			firstPunchOffduty.put("punchTime", calDate + " 23:59:59.0");
			firstPunchOffduty.put("type", 1);
		}
		return calWorkhours(firstPunchOnduty, firstPunchOffduty);
	}
	
	/**
	 * 计算两个打卡记录之间的工作时间（单位：小时）
	 * @param firstPunchOnduty
	 * @param firstPunchOffduty
	 * @return
	 */
	private double calWorkhours(Map<String, Object> firstPunchOnduty, Map<String, Object> firstPunchOffduty){
		DateTime ondutyTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(firstPunchOnduty.get("punchTime").toString());
		DateTime offdutyDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(firstPunchOffduty.get("punchTime").toString());
		
		int minutes = Minutes.minutesBetween(ondutyTime, offdutyDateTime).getMinutes();
		
		return minutes / 60.0;
	}
	
	@Override
	public int calAttendanceDays(Integer courierId, String startDate, String endDate){
		return 0;
	}
	
	@Override
	public Map<String, Object> getFirstAttendanceTime(Integer courierId, String time) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT ond.onduttyTime, offd.offduttyTime from");
		query.append(" (SELECT FROM_UNIXTIME(create_time) onduttyTime, user_id ");
		query.append(" from 0085_courier_attendance ");
		query.append(" where  DATE(FROM_UNIXTIME(create_time))= ? and type = 0 and user_id = ?");
		query.append(" ORDER BY onduttyTime LIMIT 0, 1 ) ond");
		query.append(" LEFT JOIN ");
		query.append(" (SELECT FROM_UNIXTIME(create_time) offduttyTime, user_id ");
		query.append(" from 0085_courier_attendance ");
		query.append(" where  DATE(FROM_UNIXTIME(create_time))= ? and type = 1 and user_id = ? ");
		query.append(" ORDER BY offduttyTime LIMIT 0, 1) offd");
		query.append(" ON ond.user_id = offd.user_id ");
		return findOneForJdbc(query.toString(), time, courierId, time, courierId);
	}
	
	@Override
	public List<Integer> getPunchCourierIds(String date){
		List<Integer> courierIds = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT a.user_id from 0085_courier_attendance a");
		sql.append(" where date(from_unixtime(a.create_time))=?");
		sql.append(" ORDER BY user_id");
		List<Map<String, Object>> list = this.findForJdbc(sql.toString(), date);
		
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map:list){
				courierIds.add(Integer.parseInt(map.get("user_id").toString()));
			}
		}
		return courierIds;
	}

	@Override
	public boolean isInTheRange(int courierId, double lon1, double lat1) {
		double distances = Double.MAX_VALUE;
		//默认1000米
		double correctDistance = 1000.00;
		try {
			correctDistance = Double.parseDouble(systemconfigService.getValByCode("courier_clock_correct_distance"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> lonAndLatList = orgService.getOrgLongitudeAndLatitude(courierId);
		if(CollectionUtils.isEmpty(lonAndLatList)){
			logger.warn("未找到快递员" + courierId + "的考勤地址");
			return false;
		}
		for(Map<String, Object> lonAndLatMap : lonAndLatList){
			distances = DistanceUtil.getShortDistance(lon1, lat1, Double.valueOf(lonAndLatMap.get("longitude").toString()), Double.valueOf(lonAndLatMap.get("latitude").toString()));
			logger.info("快递员： " + courierId + "距离打卡地点" + distances + "米");
			//在打卡范围内， 才可以打卡
			if(distances<correctDistance){
				return true;
			}
		}
		logger.warn("快递员" + courierId + "不在打卡范围内");
		return false;
	}

}