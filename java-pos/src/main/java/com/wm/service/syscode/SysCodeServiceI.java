package com.wm.service.syscode;

import java.util.List;
import java.util.Map;

public interface SysCodeServiceI {
	
	/**
	 * 根据code类型获取code_name
	 * @return
	 */
	public List<Map<String, Object>> getSysCodeName(String code);
	
	/**
	 * 根据code和code_value获取code_name
	 * @param code
	 * @param codeValue
	 * @return
	 */
	public String findSysCodeNameByCodeAndValue(String code, int codeValue);

}
