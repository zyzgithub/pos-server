package ${bussiPackage}.service.impl.${entityPackage};

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import ${bussiPackage}.service.${entityPackage}.${entityName}ServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import ${bussiPackage}.entity.${entityPackage}.${entityName}Entity;
<#list subTab as sub>
import ${bussiPackage}.entity.${sub.entityPackage}.${sub.entityName}Entity;
</#list>
@Service("${entityName?uncap_first}Service")
@Transactional
public class ${entityName}ServiceImpl extends CommonServiceImpl implements ${entityName}ServiceI {

	@Override
	public void addMain(${entityName}Entity ${entityName?uncap_first},
	        <#list subTab as sub>List<${sub.entityName}Entity> ${sub.entityName?uncap_first}List<#if sub_has_next>,</#if></#list>){
			//保存主信息
			this.save(${entityName?uncap_first});
		
			<#list subTab as sub>
			/**保存-${sub.ftlDescription}*/
			for(${sub.entityName}Entity ${sub.entityName?uncap_first}:${sub.entityName?uncap_first}List){
				<#list sub.foreignKeys as key>
				//外键设置
				<#if key?lower_case?index_of("${jeecg_table_id}")!=-1>
				${sub.entityName?uncap_first}.set${key}(${entityName?uncap_first}.get${jeecg_table_id?cap_first}());
				<#else>
				${sub.entityName?uncap_first}.set${key}(${entityName?uncap_first}.get${key}());
				</#if>
				</#list>
				this.save(${sub.entityName?uncap_first});
			}
			</#list>
	}

	@Override
	public void updateMain(${entityName}Entity ${entityName?uncap_first},
	        <#list subTab as sub>List<${sub.entityName}Entity> ${sub.entityName?uncap_first}List<#if sub_has_next>,</#if></#list>) {
		//保存订单主信息
		this.saveOrUpdate(${entityName?uncap_first});
		
		
		//===================================================================================
		//获取参数
	    <#list subTab as sub>
		    <#list sub.foreignKeys as key>
		    <#if key?lower_case?index_of("${jeecg_table_id}")!=-1>
		Object ${jeecg_table_id}${sub_index} = ${entityName?uncap_first}.get${jeecg_table_id?cap_first}();
		    <#else>
		Object ${key?uncap_first}${sub_index} = ${entityName?uncap_first}.get${key}();
		    </#if>
		    </#list>
	    </#list>
		<#list subTab as sub>
		//===================================================================================
		//删除-${sub.ftlDescription}
	    String hql${sub_index} = "from ${sub.entityName}Entity where 1 = 1<#list sub.foreignKeys as key> AND ${key?uncap_first} = ? </#list>";
	    List<${sub.entityName}Entity> ${sub.entityName?uncap_first}OldList = this.findHql(hql${sub_index},<#list sub.foreignKeys as key><#if key?lower_case?index_of("${jeecg_table_id}")!=-1>${jeecg_table_id}${sub_index}<#else>${key?uncap_first}${sub_index}</#if><#if key_has_next>,</#if></#list>);
		this.deleteAllEntitie(${sub.entityName?uncap_first}OldList);
		//保存-${sub.ftlDescription}
		for(${sub.entityName}Entity ${sub.entityName?uncap_first}:${sub.entityName?uncap_first}List){
			//外键设置
			 <#list sub.foreignKeys as key>
			    <#if key?lower_case?index_of("${jeecg_table_id}")!=-1>
			${sub.entityName?uncap_first}.set${key}(${entityName?uncap_first}.get${jeecg_table_id?cap_first}());
			    <#else>
			${sub.entityName?uncap_first}.set${key}(${entityName?uncap_first}.get${key}());
			    </#if>
			 </#list>
			this.save(${sub.entityName?uncap_first});
		}
		</#list>
		
	}

	@Override
	public void delMain(${entityName}Entity ${entityName?uncap_first}) {
		//删除主表信息
		this.delete(${entityName?uncap_first});
		
		//===================================================================================
		//获取参数
	    <#list subTab as sub>
		    <#list sub.foreignKeys as key>
		    <#if key?lower_case?index_of("${jeecg_table_id}")!=-1>
		Object ${jeecg_table_id}${sub_index} = ${entityName?uncap_first}.get${jeecg_table_id?cap_first}();
		    <#else>
		Object ${key?uncap_first}${sub_index} = ${entityName?uncap_first}.get${key}();
		    </#if>
		    </#list>
	    </#list>
		<#list subTab as sub>
		//===================================================================================
		//删除-${sub.ftlDescription}
	    String hql${sub_index} = "from ${sub.entityName}Entity where 1 = 1<#list sub.foreignKeys as key> AND ${key?uncap_first} = ? </#list>";
	    List<${sub.entityName}Entity> ${sub.entityName?uncap_first}OldList = this.findHql(hql${sub_index},<#list sub.foreignKeys as key><#if key?lower_case?index_of("${jeecg_table_id}")!=-1>${jeecg_table_id}${sub_index}<#else>${key?uncap_first}${sub_index}</#if><#if key_has_next>,</#if></#list>);
		this.deleteAllEntitie(${sub.entityName?uncap_first}OldList);
		</#list>
	}
	
}