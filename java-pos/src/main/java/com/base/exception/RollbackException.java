package com.base.exception;

import org.jeecgframework.core.common.model.json.AjaxJson;

@SuppressWarnings("serial")
public class RollbackException extends RuntimeException{
	private AjaxJson ajaxJson;

	public AjaxJson getAjaxJson() {
		return ajaxJson;
	}

	public void setAjaxJson(AjaxJson j) {
		this.ajaxJson = j;
	}
	
}
