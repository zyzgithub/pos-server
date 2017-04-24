package com.wm.entity.partner;

import java.util.List;

import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.util.PageList;

/**
 * 
* @ClassName: PartnerVo 
* @Description: TODO
* @author 黄聪
* @date 2015年9月30日 上午10:54:27 
*
 */
public class PartnerVo {

	/**第三方标识ID*/
	private java.lang.String openid;
	/**合作方名称*/
	private java.lang.String name;
	/**商家名称*/
	private java.lang.String title;
	/**菜品分类*/
	private List<MenutypeEntity> menutypeList;
	/**菜品列表*/
	private PageList pageList;
	

	public java.lang.String getOpenid() {
		return openid;
	}

	public void setOpenid(java.lang.String openid) {
		this.openid = openid;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	public List<MenutypeEntity> getMenutypeList() {
		return menutypeList;
	}

	public void setMenutypeList(List<MenutypeEntity> menutypeList) {
		this.menutypeList = menutypeList;
	}

	public PageList getPageList() {
		return pageList;
	}

	public void setPageList(PageList pageList) {
		this.pageList = pageList;
	}
	
}
