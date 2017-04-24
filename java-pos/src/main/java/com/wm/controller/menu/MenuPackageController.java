package com.wm.controller.menu;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.menu.MenuPackVo;
import com.wm.service.menu.MenuPackageServiceI;

@Controller
@RequestMapping("ci/menupackage")
public class MenuPackageController extends BaseController {
	@Autowired
	private MenuPackageServiceI packageService;

	/**
	 * 菜品列表查询
	 * 
	 * @author chengyinghao
	 * @param tpmPackage
	 * @return
	 */
	@RequestMapping(params = "dishesList")
	@ResponseBody
	public AjaxJson dishesList(Integer merchantId,Integer typeId,Integer merchantSource,@RequestParam(defaultValue = "0")Integer start,
			@RequestParam(defaultValue="10")Integer num) {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		j.setStateCode("01");
		
		if(merchantId == null){
			j.setMsg("商家ID不能为空!");
			return j;
		}
		
		if(merchantSource != 2){
			if(typeId == null){
				j.setMsg("菜单类别id不能为空!");
				return j;
			}	
		}
		
		if(typeId == null){
			typeId = 0;
		}
		
		List<Map<String,Object>> tpmAccountInfo = packageService.getTpmPackageMainList(merchantId,typeId,merchantSource,start,num);
		j.setObj(tpmAccountInfo);
		j.setMsg("操作成功!");
		j.setSuccess(true);
		j.setStateCode("00");
		
		return j;
	}

	/**
	 * 删除菜品
	 * 
	 * @author chengyinghao
	 * @param tpmPackage
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(params = "deleteDishesList")
	@ResponseBody
	public AjaxJson deleteDishesList(Integer merchantId , Integer id) {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		j.setStateCode("01");
		
		if(merchantId == null){
			j.setMsg("商家ID不能为空!");
			
			return j;
		}
		
		if(id == null){
			j.setMsg("菜品ID不能为空!");
			
			return j;
		}
		
		Integer delmenu = packageService.deleteTpmPackageList(merchantId,id);
		if(delmenu == 0){
			j.setMsg("操作失败!");
			j.setSuccess(false);
			j.setStateCode("01");
		}else{
			j.setMsg("操作成功!");
			j.setSuccess(true);
			j.setStateCode("00");
		}
		
		return j;
	}

	/**
	 * 上下架菜品
	 * 
	 * @param tpmPackage
	 * @param bindingResult
	 * @author bin
	 * @return
	 */
	@RequestMapping(params = "updateStatus")
	@ResponseBody
	public AjaxJson updateStatus(Integer merchantId , Integer id ,String display,String menuGroupId) {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		j.setStateCode("01");
		
		if(merchantId == null){
			j.setMsg("商家ID不能为空!");
			return j;
		}
		
		if(id == null){
			j.setMsg("菜品ID不能为空!");
			return j;
		}
		
		if(display == null || display.equals("")){
			j.setMsg("上/下架状态不能为空!");
			return j;
		}
		
		if (display.equals("N")) {
			Map<String,Object> stockNum = packageService.selectTpmPackStock(merchantId,id);
			if (Integer.parseInt(stockNum.get("today_repertory").toString()) == 0) {
				j.setMsg("该菜品的库存为零，请先在“编辑”中添加库存!");
				return j;	
			}
		}
		
		Integer updismenu = packageService.updateTpmPackageByStatus(merchantId,id,display);
		if(updismenu == 0){
			j.setMsg("操作失败!");
			j.setSuccess(false);
			j.setStateCode("01");
		}else{
			j.setMsg("操作成功!");
			j.setSuccess(true);
			j.setStateCode("00");
		}
		
		return j;
	}

	/**
	 * 添加菜品/套餐
	 * 
	 * @author chengyinghao
	 * @param tpmMainPackage
	 * @return
	 */
	@RequestMapping(params = "addishes")
	@ResponseBody
	public AjaxJson addishes(MenuPackVo menuVo) {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		j.setStateCode("01");
		if(menuVo.getName() == null || menuVo.getName() == ""){
			j.setMsg("菜品名称不能为空!");
			return j;
		}else if(menuVo.getName().length() >15){
			j.setMsg("菜品名称最多15个字!");
			return j;
		}
		
		if(menuVo.getMerchantSource() == null){
			j.setMsg("商家来源不能为空!");
			return j;
		}
		
		if(menuVo.getPrice() == null){
			j.setMsg("菜品单价不能为空!");
			return j;
		}else if(menuVo.getPrice() == 0){
			j.setMsg("菜品单价不能为0!");
			return j;
		}else if(!String.valueOf(menuVo.getPrice()).matches("^(0|([1-9][0-9]{0,7}))(.[0-9]{1,2})?$")){
			j.setMsg("菜品单价格式不合法!");
			return j;
		}
		
		if(menuVo.getImage() == null || menuVo.getImage() == ""){
			j.setMsg("请选择菜品图片!");
			return j;
		}
		
		if(menuVo.getImage().length() > 100){
			j.setMsg("图片链接长度超长了!");
			return j;
		}
		
		if(menuVo.getTodayRepertory() == null){
			menuVo.setTodayRepertory(1000);
		}else{
			if(menuVo.getTodayRepertory()>1000 || menuVo.getTodayRepertory() <0){
				j.setMsg("库存量应大于等于0且小于等于1000!");
				return j;
			}
		}
		
		if(menuVo.getIntro() != null && menuVo.getIntro() != ""){
			if(menuVo.getIntro().length() > 100){
				j.setMsg("菜品描述限100字内!");
				return j;
			}
		}
		
		Map<String,Object> nameMap = packageService.getPackName(menuVo.getName(),menuVo.getMerchantId());
		if(Integer.parseInt(nameMap.get("cont").toString()) != 0 ){
			j.setMsg("菜品名重复!");
			return j;
		}
		
		Integer addmenu = packageService.createTpmPackage(menuVo);
		if(addmenu == 0){
			j.setMsg("操作失败!");
			j.setSuccess(false);
			j.setStateCode("01");
		}else{
			j.setMsg("操作成功!");
			j.setSuccess(true);
			j.setStateCode("00");
		}
		
		return j;
	}

	/**
	 * 菜品编辑查询
	 * 
	 * @author chengyinghao
	 * @param packageId
	 */
	@RequestMapping(params = "selectDishpo")
	@ResponseBody
	public AjaxJson selectDishpo(Integer merchantId ,Integer id) {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		j.setStateCode("01");
		
		if(merchantId == null){
			j.setMsg("商家ID不能为空!");
			
			return j;
		}
		
		if(id == null){
			j.setMsg("菜品ID不能为空!");
			
			return j;
		}
		
		Map<String,Object> list = packageService.selectTpmPackagepo(merchantId,id);
		j.setObj(list);
		j.setMsg("操作成功!");
		j.setSuccess(true);
		j.setStateCode("00");
		
		return j;
	}

	/**
	 * 编辑菜品
	 * 
	 * @author chengyinghao
	 * @param tpmMainPackage
	 * @return
	 */
	@RequestMapping(params = "updateDishes")
	@ResponseBody
	public AjaxJson updateDishes(MenuPackVo menuVo) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		j.setStateCode("01");
		
		if(menuVo.getName() == null || menuVo.getName() == ""){
			j.setMsg("菜品名称不能为空!");
			return j;
		}else if(menuVo.getName().length() >15){
			j.setMsg("菜品名称最多15个字!");
			return j;
		}
		
		if(menuVo.getPrice() == null){
			j.setMsg("菜品单价不能为空!");
			return j;
		}else if(menuVo.getPrice() == 0){
			j.setMsg("菜品单价不能为0!");
			return j;
		}else if(!String.valueOf(menuVo.getPrice()).matches("^(0|([1-9][0-9]{0,7}))(.[0-9]{1,2})?$")){
			j.setMsg("菜品单价格式不合法!");
			return j;
		}
		
		if(menuVo.getImage() == null || menuVo.getImage() == ""){
			j.setMsg("请选择菜品图片!");
			return j;
		}
		
		if(menuVo.getImage().length() > 100){
			j.setMsg("图片链接长度超长了!");
			return j;
		}
		
		Map<String,Object> nameMap = packageService.getPackNameIo(menuVo.getName(),menuVo.getMerchantId(),menuVo.getId());
		if(Integer.parseInt(nameMap.get("cont").toString()) != 0 ){
			j.setMsg("菜品名重复!");
			return j;
		}

		Integer updPack= packageService.updateTpmPackage(menuVo);
		if(updPack == null){
			j.setMsg("操作失败!");
			j.setSuccess(false);
			j.setStateCode("01");
		}else{
			j.setMsg("操作成功!");
			j.setSuccess(true);
			j.setStateCode("00");
		}
		
		return j;
	}
	
	/**
	 * 菜品排序
	 * @param merchantId
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(params = "sortList")
	@ResponseBody
	public AjaxJson sortList (Integer merchantId,String menuIds) throws JSONException {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		j.setStateCode("01");
		if(merchantId == null){
			j.setMsg("商家ID不能为空!");
			
			return j;
		}
		if(menuIds == null || menuIds == ""){
			j.setMsg("排序信息不能为空!");
			
			return j;
		}
		
		j = packageService.sortList(merchantId,menuIds);
		
		return j;
	}
}