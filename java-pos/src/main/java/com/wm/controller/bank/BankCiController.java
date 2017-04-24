package com.wm.controller.bank;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.bank.BankServiceI;

/**
 * @Title: Controller
 * @Description: bank
 * @author wuyong
 * @date 2015-01-07 09:45:18
 * @version V1.0
 *
 */
@Controller
@RequestMapping("ci/bankController")
public class BankCiController extends BaseController {

	@Autowired
	private BankServiceI bankService;
	@Autowired
	private SystemService systemService;

	/**
	 * 查询银行列表
	 * @return
	 */
	@RequestMapping(params = "queryBankList")
	@ResponseBody
	public AjaxJson queryBankList() {
		AjaxJson j = new AjaxJson();
		String sql = "SELECT id, name, logo_url FROM `bank` ORDER BY sort ";
		j.setMsg("查询成功");
		j.setSuccess(true);
		j.setObj(bankService.findForJdbc(sql));
		return j;
	}

}
