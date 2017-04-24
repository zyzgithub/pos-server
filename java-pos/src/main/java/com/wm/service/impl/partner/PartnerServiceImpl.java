package com.wm.service.impl.partner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenuVo;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.partner.PartnerEntity;
import com.wm.entity.partner.PartnerVo;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.MenutypeServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.partner.PartnerServiceI;
import com.wm.util.PageList;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("partnerService")
@Transactional
public class PartnerServiceImpl extends CommonServiceImpl implements PartnerServiceI {
	
	@Autowired
	private MerchantServiceI merchantServic;
	@Autowired
	private MenutypeServiceI menutypeService;
	@Autowired
	private MenuServiceI menuService;
	

	@Override
	public PartnerVo findMerchantMenuList(String openid,Integer page, Integer rows) {
		PartnerVo  partnerVo  = new PartnerVo();
		List<PartnerEntity> list= this.findByProperty(PartnerEntity.class, "openid", openid);
		if(list!=null && !list.isEmpty()){
			PartnerEntity partner= list.get(0);
			if(partner!=null){
				partnerVo.setOpenid(openid);
				partnerVo.setName(partner.getName());
				if(partner.getMerchantId()!=null){
				MerchantEntity merchant=merchantServic.get(MerchantEntity.class, partner.getMerchantId());
					if(merchant!=null){
						partnerVo.setTitle(merchant.getTitle());
						List<MenutypeEntity> menutypeList = menutypeService.findByProperty(MenutypeEntity.class, "merchantId", partner.getMerchantId());
						if(menutypeList!=null && !menutypeList.isEmpty()){
							partnerVo.setMenutypeList(menutypeList);
						}
						MenuVo menu = new MenuVo();
						menu.setMerchantId(partner.getMerchantId());
						PageList pageList = menuService.findByEntityList(menu,page,rows);						
						if(pageList!=null){
							partnerVo.setPageList(pageList);
						}
					}
				}
			}
		}
		return partnerVo;
	}
	
}