package com.wm.service.impl.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wm.service.category.ShareFunctionService;

@Service("shareFunctionService")
@Transactional
public class ShareFunctionServiceImpl  extends CommonServiceImpl implements ShareFunctionService {

	@Override
	public JSONObject loadAdministrativeAddress() {
		String sql = "select id, org_name from 0085_org where level = 2";
		List<Map<String, Object>> provinces = findForJdbc(sql);

		JSONObject provinceObject = new JSONObject();
		sql = "select id, org_name, pid from 0085_org where level = 3";
		List<Map<String, Object>> citys = findForJdbc(sql);

		Map<Long, JSONArray> cityMap = new HashMap<Long, JSONArray>();
		for (Map<String, Object> city : citys) {
			long id = Long.parseLong(city.get("id").toString());
			String orgName = city.get("org_name").toString();
			long pid = Long.parseLong(city.get("pid").toString());

			JSONArray cityArray = cityMap.get(pid);
			if (cityArray == null) {
				cityArray = new JSONArray();
				cityMap.put(pid, cityArray);
			}
			JSONObject cityObject = new JSONObject();
			cityObject.put("value", id);
			cityObject.put("text", orgName);

			cityArray.add(cityObject);
		}

		JSONArray provinceArray = new JSONArray();
		for (Map<String, Object> province : provinces) {
			long id = Long.parseLong(province.get("id").toString());
			String orgName = province.get("org_name").toString();

			JSONArray cityArray = cityMap.get(id);
			if (cityArray == null || cityArray.isEmpty()) {
				continue;
			}

			JSONObject p = new JSONObject();
			p.put("value", id);
			p.put("text", orgName);
			p.put("children", cityArray);

			provinceArray.add(p);
		}
		provinceObject.put("provinces", provinceArray);
		return provinceObject;
	}
}