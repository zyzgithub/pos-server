package com.wm.service.team;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.base.VO.UploadFileVO;

public interface TeamTopicServiceI extends CommonService{
	/**
	 * 获取小组话题列表
	 * @param userId
	 * @param start
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> getTeamTopicList(String userId,String start,String num);
	/**
	 * 创建小组
	 * @param userId
	 * @param teamName
	 * @param topic
	 * @param introduction
	 * @param label
	 * @return
	 */
	public int createTeam(String userId,String teamName,String topic,String introduction,String label);
	
	/**
	 * 小组头像上传
	 * 
	 * @param teamId
	 *            用户id
	 * @param file
	 *            用户图片文件
	 * @param rootPath   
	 *            服务器根目录
	 */
	public String uploadTeamPhoto(int teamId, UploadFileVO file, String rootPath);
}
