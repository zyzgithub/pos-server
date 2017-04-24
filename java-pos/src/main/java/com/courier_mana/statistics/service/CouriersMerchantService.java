package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 统计-店铺统计接口
 * @author hyj
 *
 */
public interface CouriersMerchantService {
	/**(OvO)
	 * 获取各类型店铺的数量
	 * @param vo		搜索条件VO(时间, 地区)
	 * @param courierId	快递员ID(必选)
	 * @return	返回店铺数量信息, 包含
	 * 			fastFoodMerchant	快餐店铺
	 * 			drinkMerchant		饮品店铺
	 * 			communityMerchant	社区店铺
	 * 			otherMerchant		其他店铺
	 * 			totalMerchant		总店铺数量
	 */
	public abstract Map<String, Object> merchantsCount(SearchVo vo, Integer courierId);
	
	/**(OvO)
	 * 店铺排行
	 * @param vo			搜索条件VO(时间, 地区ID)
	 * @param courierId		快递员ID(必选)
	 * @param page			页数(可选, 默认为1)
	 * @param rowsPerPage	每页显示的记录数(可选, 默认为10)
	 * @param merchantType	店铺类型(a: 所有类型; f: 快餐店铺; d: 饮品店铺; c: 社区店铺; o: 其他店铺)
	 * @return	返回店铺排行信息, 包含
	 * 			rank			排名
	 * 			merchantId		店铺ID(估计没用)
	 * 			merchantName	店铺名
	 * 			todayOrigin		今日销售额
	 * 			preOrigin		上一个时段的销售额
	 * 			totalOrigin		累计销售额
	 * 			detail			销售额细节(最近三天销售记录)
	 */
	public abstract List<Map<String, Object>> merchantsRank(SearchVo vo, Integer courierId, Integer page, Integer rowsPerPage, String merchantType);
}
