package org.jeecgframework.tag.core.easyui;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jeecg.system.pojo.base.TSType;
import jeecg.system.pojo.base.TSTypegroup;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * 选择下拉框
 * 
 * @author: lianglaiyang
 * @date： 日期：2013-04-18
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DictSelectTag extends TagSupport {
	
	private String typeGroupCode;	//数据字典类型
	private String field;	//选择表单的Name  EAMPLE:<select name="selectName" id = "" />
	private String id;	//选择表单ID   EAMPLE:<select name="selectName" id = "" />
	private String defaultVal;	//默认值 
	private String divClass;	//DIV样式
	private String labelClass;	//Label样式
	private String title;	//label显示值
	private boolean hasLabel = true;	//是否显示label
	
	
	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	

	public StringBuffer end() {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isBlank(divClass)) {
			divClass = "form";	//默认form样式
		}
		if (StringUtils.isBlank(labelClass)){
			labelClass = "Validform_label";	//默认label样式
		}
		TSTypegroup typeGroup = TSTypegroup.allTypeGroups.get(this.typeGroupCode.toLowerCase());
		List<TSType> types = TSTypegroup.allTypes.get(this.typeGroupCode.toLowerCase());
		if (hasLabel) {
			sb.append("<div class=\""+divClass+"\">");
			sb.append("<label class=\""+labelClass+"\" >");
		}
		if (typeGroup ==  null) {
		}else {
			if (hasLabel) {
				if (StringUtils.isBlank(this.title)) {
					this.title = typeGroup.getTypegroupname();
				}
				sb.append(this.title+":");
				sb.append("</label>");
			}
			sb.append("<select name=\""+field+"\"");
			if (!StringUtils.isBlank(this.id)) {
				 sb.append(" id=\""+id+"\"");
			}
			sb.append(">");
			for (TSType type : types) {
				if (type.getTypecode().equals(this.defaultVal)) {
					sb.append(" <option value=\""+type.getTypecode()+"\" selected=\"selected\">");
				}else {
					sb.append(" <option value=\""+type.getTypecode()+"\">");
				}
				sb.append(type.getTypename());
				sb.append(" </option>");
			}
			
			sb.append("</select>");
			if (hasLabel) {
				sb.append("</div>");
			}
		}

		return sb;
	}


	public String getTypeGroupCode() {
		return typeGroupCode;
	}

	public void setTypeGroupCode(String typeGroupCode) {
		this.typeGroupCode = typeGroupCode;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getDivClass() {
		return divClass;
	}

	public void setDivClass(String divClass) {
		this.divClass = divClass;
	}

	public String getLabelClass() {
		return labelClass;
	}

	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isHasLabel() {
		return hasLabel;
	}

	public void setHasLabel(boolean hasLabel) {
		this.hasLabel = hasLabel;
	}



	
}
