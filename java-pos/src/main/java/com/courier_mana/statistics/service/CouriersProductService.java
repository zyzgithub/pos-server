package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 统计-菜品统计接口
 * @author hyj
 *
 */
public interface CouriersProductService {
	/**(OvO)
	 * 菜品排行
	 * @param vo			搜索条件VO(时间, 地区ID)
	 * @param courierId		快递员ID(必选)
	 * @param page			页数(可选, 默认为1)
	 * @param rowsPerPage	每页显示的记录数(可选, 默认为10)
	 * @return	返回菜品排行信息, 包含
	 * 			rank			排名
	 * 			name			菜名
	 * 			quantity		销售份额
	 * 			totalQuantity	累计销售份额
	 */
	public List<Map<String, Object>> productRank(SearchVo vo, Integer courierId, Integer page, Integer rowsPerPage);
}
