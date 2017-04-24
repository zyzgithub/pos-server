package com.wm.service.topic;

import org.jeecgframework.core.common.service.CommonService;

import com.base.VO.UploadFileVO;

public interface TopicServiceI extends CommonService{

	/**
	 * 发帖
	 * @param userId 用户id
	 * @param teamId 小组为id
	 * @return
	 */
	public Integer createPost(int userId,int teamId,String title,String content);
	
	/**
	 * 发帖图片上传
	 * 
	 * @param teamId
	 *            用户id
	 * @param file
	 *            用户图片文件
	 * @param rootPath   
	 *            服务器根目录
	 */
	public String uploadTopicPhoto(int topicId, UploadFileVO file, String rootPath);
}
