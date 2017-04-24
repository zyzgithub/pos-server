package com.wm.service.impl.team;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.impl.CommonDao;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.FileUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.VO.UploadFileVO;
import com.base.util.FileUtil;
import com.wm.entity.team.TeamEntity;
import com.wm.service.team.TeamTopicServiceI;

@Service("teamTopicService")
@Transactional
public class TeamTopicServiceImpl extends CommonServiceImpl implements TeamTopicServiceI {

	private static final String PHOTO_PRE_PATH = "image/team";
	
	@Override
	public List<Map<String, Object>> getTeamTopicList(String userId, String start, String num) {
				
		String sql = "SELECT t.id,t.team_name,t.num_member,t.introduction,t.topic,CONCAT((SELECT s.value " +
				" FROM system_config s WHERE s.code='user_url'),t.image) image FROM team_member t_m LEFT JOIN " +
				" team t ON t.id=t_m.team_id WHERE t_m.user_id=? ORDER BY t.topic LIMIT ?,?";
		
		String sql2 = "SELECT tp.id as topicId,tp.team_id,tp.title,tp.text,CONCAT((SELECT s.value FROM system_config s " +
				" WHERE s.code='user_url'),tp.image) image, FROM_UNIXTIME(tp.create_time,'%Y-%m-%d %H:%i') create_time," +
				" tp.num_approval,tp.num_comment,tp.num_scan,tp.is_approvalBySelf,u.nickname FROM team t LEFT JOIN topic tp " +
				" ON t.id=tp.team_id LEFT JOIN user u ON tp.user_id=u.id WHERE t.id=? ORDER BY tp.num_approval LIMIT 0,3";
		
		List<Map<String, Object>> teamList = this.findForJdbc(sql, new Object[]{userId, start, num});
		List<Map<String, Object>> topics = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> teamTopics = new ArrayList<Map<String, Object>>();
		Map<String,Object> m = new HashMap<String, Object>();	
		
		for(int i=0;i<teamList.size();i++){
			m = teamList.get(i);
			String teamId = m.get("id").toString();
			topics = this.findForJdbc(sql2, new Object[]{teamId});
			for(int j=0;j<topics.size();j++){
				if(teamId.equals(topics.get(j).get("team_id").toString())){
					m.put("topics", topics);				
				}
			}
			teamTopics.add(m);
		}

		return teamTopics;
	}

	@Override
	public int createTeam(String userId, String teamName, String topic, String introduction, String label) {
		String sql = "insert into team(user_id,num_member,team_name,topic,introduction,label) VALUES(?,1,?,?,?,?)";
		this.executeSql(sql, userId,teamName,topic,introduction,label);
		sql = "select LAST_INSERT_ID() lastInsertId";
		Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
		int teamId = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
		return teamId;
	}

	@Override
	public String uploadTeamPhoto(int teamId, UploadFileVO file, String rootPath) {
		// 文件上传路径
		String path = rootPath + CommonDao.uploadPath + File.separator
				+ PHOTO_PRE_PATH + File.separator + teamId;
		// 随机文件名
		String tempFileName = System.currentTimeMillis() + "."
				+ FileUtils.getExtend(file.getName());
		// 全路径
		String fullPathFileName = CommonDao.uploadPath + File.separator
				+ PHOTO_PRE_PATH + File.separator + teamId + File.separator
				+ tempFileName;
		// 保存文件
		FileUtil.write(path, file.decodePicBase64(), tempFileName, false);

		TeamEntity team = this.get(TeamEntity.class, teamId);
		if (StringUtil.isNotEmpty(team.getImage())) {
			// 删除原头像文件
			FileUtil.delFile(rootPath + team.getImage());
		}
		// 保存新头像URL
		team.setImage(fullPathFileName.replaceAll("\\\\", "/"));
		this.saveOrUpdate(team);
		
		return fullPathFileName.replaceAll("\\\\", "/");
	}


}
