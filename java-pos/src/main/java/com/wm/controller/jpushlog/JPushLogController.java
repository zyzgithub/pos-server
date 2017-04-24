package com.wm.controller.jpushlog;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.wm.entity.jpush.JPushLogEntity;
import com.wm.service.jpush.JPushLogServiceI;

@Controller
@RequestMapping("ci/jpushLogController")
public class JPushLogController extends BasicController{
	
	@Autowired
	private JPushLogServiceI jPushLogService;
	
	/**
	 * 根据日志id更新日志状态
	 * @param jpushLogId
	 * @return
	 */
	@RequestMapping(params = "changeIsFeedBack")
	@ResponseBody
	public AjaxJson changeIsfeedBack(@RequestParam Integer jpushLogId){
		AjaxJson json = new AjaxJson();
		try {
			JPushLogEntity jPushLogEntity = jPushLogService.get(JPushLogEntity.class, jpushLogId);
			if(jPushLogEntity == null){
				json.setSuccess(false);
				json.setMsg("没有找到对应的推送日志");
				json.setStateCode("02");
				return json;
			}
			jPushLogService.changeIsFeedBack(jpushLogId, jPushLogEntity);
			json.setSuccess(true);
			json.setMsg("更新推送日志反馈状态成功");
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(true);
			json.setMsg("更新推送日志反馈状态失败");
			json.setStateCode("01");
		}
		return json;
	}

}
