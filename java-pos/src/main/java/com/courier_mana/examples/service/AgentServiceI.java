package com.courier_mana.examples.service;

import java.util.List;

/**(OvO)
 * 合作商信息获取Service
 * @author hyj
 */
public interface AgentServiceI {
	/**(OvO)
	 * 获取合作商所属的快递员ID
	 * @param agentUserId	合作商用户ID
	 * @return
	 */
	public abstract List<Integer> getAgentCourierIds(Integer agentUserId);
}
