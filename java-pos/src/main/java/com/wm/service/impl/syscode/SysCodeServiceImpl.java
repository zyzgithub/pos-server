package com.wm.service.impl.syscode;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.syscode.SysCodeServiceI;
import com.wm.util.AliOcs;

@Service
@Transactional
public class SysCodeServiceImpl extends CommonServiceImpl implements
		SysCodeServiceI {
	
	private final static int EXPIRED_TIME = 60*60*24;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getSysCodeName(String code) {
		
		String key = "syscode_search_" + code;
		Object obj = AliOcs.getObject(key);
		
		//如果缓存中已经有了
		if(obj != null){
			return (List<Map<String, Object>>)obj;
		}
		//缓存中没有
		else{
			StringBuilder query = new StringBuilder();
			query.append(" select code_name codeName, code_value codeValue");
			query.append(" from sys_code");
			query.append(" where code = ?");
			query.append(" order by id");		
			List<Map<String, Object>> list = findForJdbc(query.toString(), code);
			AliOcs.set(key, list, EXPIRED_TIME);
			return list;
		}
	}

	@Override
	public String findSysCodeNameByCodeAndValue(String code, int codeValue) {
		StringBuilder query = new StringBuilder();
		query.append(" select code_name codeName");
		query.append(" from sys_code");
		query.append(" where code = ? and code_value = ? and flag = 1");
		String codeName = findOneForJdbc(query.toString(), String.class, code, codeValue);
		return codeName;
	}

}
