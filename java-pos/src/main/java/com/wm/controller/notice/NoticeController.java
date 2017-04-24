package com.wm.controller.notice;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.notice.NoticeEntity;
import com.wm.service.notice.NoticeServiceI;

@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	
	@Autowired
	private NoticeServiceI noticeService;

	@RequestMapping(params = "push")
	@ResponseBody
	public AjaxJson push(String sql, @RequestParam Integer noticeId){
		AjaxJson ajaxJson = new AjaxJson();
		try {
			NoticeEntity noticeEntity = noticeService.getNotice(noticeId);
			
			if(noticeEntity == null){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("无法根据参数noticeId=" + noticeId + "获取到对应的消息");
				ajaxJson.setStateCode("02");
				return ajaxJson;
			}
			
			//如果后台传来查询的sql 
			if(StringUtils.isNotBlank(sql)){
				List< Integer> userIds = noticeService.getUserIds(sql);
				noticeService.push(noticeEntity, userIds);
			}
			else {
				noticeService.push(noticeEntity);
			}
			
			ajaxJson.setSuccess(true);
			ajaxJson.setState("00");
			ajaxJson.setMsg("发送成功！");
			
		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setState("01");
			ajaxJson.setMsg("发送失败！");
		}
		return ajaxJson;
	}
}
