package com.wm.service.impl.topic;

import java.io.File;
import java.util.Map;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.dao.impl.CommonDao;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.FileUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.VO.UploadFileVO;
import com.base.util.FileUtil;
import com.wm.entity.topic.TopicEntity;
import com.wm.service.topic.TopicServiceI;

@Service("topicService")
@Transactional
public class TopicServiceImpl extends CommonServiceImpl implements TopicServiceI {

	private static final String PHOTO_PRE_PATH = "image/topic";
	
	@Autowired
	private SystemService systemService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Integer createPost(int userId, int teamId,String title,String content) {
		TopicEntity topic = new TopicEntity();
		topic.setUserId(userId);
		topic.setTeamId(teamId);
		topic.setTitle(title);
		topic.setText(content);
		topic.setNumApproval(0);
		topic.setNumComment(0);
		topic.setNumScan(0);
		topic.setIsApprovalbyself("N");
		topic.setCreateTime(DateUtils.getSeconds());
		this.save(topic);
		
		
		String sql = "select LAST_INSERT_ID() lastInsertId";
		Map lastInsertIdMap = this.findOneForJdbc(sql);
		int topicId = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
		
		return topicId;
	}

	@Override
	public String uploadTopicPhoto(int topicId, UploadFileVO file,
			String rootPath) {
		// 文件上传路径
		String path = rootPath + CommonDao.uploadPath + File.separator
				+ PHOTO_PRE_PATH + File.separator + topicId;
		// 随机文件名
		String tempFileName = System.currentTimeMillis() + "."
				+ FileUtils.getExtend(file.getName());
		// 全路径
		String fullPathFileName = CommonDao.uploadPath + File.separator
				+ PHOTO_PRE_PATH + File.separator + topicId + File.separator
				+ tempFileName;
		// 保存文件
		FileUtil.write(path, file.decodePicBase64(), tempFileName, false);

		TopicEntity topic = systemService.get(TopicEntity.class, topicId);
		if (StringUtil.isNotEmpty(topic.getImage())) {
			// 删除原头像文件
			FileUtil.delFile(rootPath + topic.getImage());
		}
		// 保存新头像URL
		topic.setImage(fullPathFileName.replaceAll("\\\\", "/"));
		this.saveOrUpdate(topic);
		
		return fullPathFileName.replaceAll("\\\\", "/");
	}
	
}