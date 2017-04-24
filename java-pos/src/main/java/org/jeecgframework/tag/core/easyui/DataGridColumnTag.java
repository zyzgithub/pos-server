package org.jeecgframework.tag.core.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * 类描述：列表字段处理项目
 * 
 * @author: jeecg
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DataGridColumnTag extends TagSupport {
	protected String title;
	protected String field;
	protected Integer width;
	protected String rowspan;
	protected String colspan;
	protected String align;
	protected boolean sortable=true;
	protected boolean checkbox;
	protected String formatter;
	protected boolean hidden=true;
	protected String replace;
	protected String treefield;
	protected boolean image;
	protected boolean query=false;
	private String queryMode = "single";//字段查询模式：single单字段查询；scope范围查询
	protected boolean bSearchable=true;
	
	//private boolean autoLoadData=true; // 列表是否自动加载数据  
	// ---begin---  author:邢双阳 ---date:2013-05-18 ---for:[106]实现冰冻列
	private boolean frozenColumn=false; // 是否是冰冻列    默认不是
	// ---end---  author:邢双阳 ---date:2013-05-18 ---for:[106]实现冰冻列
	protected String url;//自定义链接
	protected String funname="openwindow";//自定义函数名称
	protected String arg;//自定义链接传入参数字段
	protected String dictionary;	//数据字典组编码
	
	
	
	public boolean isFrozenColumn() {
		return frozenColumn;
	}

	public void setFrozenColumn(boolean frozenColumn) {
		this.frozenColumn = frozenColumn;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setFunname(String funname) {
		this.funname = funname;
	}

	public void setbSearchable(boolean bSearchable) {
		this.bSearchable = bSearchable;
	}

	public void setQuery(boolean query) {
		this.query = query;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public void setTreefield(String treefield) {
		this.treefield = treefield;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	
	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}
	
	//--------begin ------------author:邢双阳  ---date	：2013-5-13  for：【103】页面载入时，数据是否载入采取可配置模式
	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}
	
	public int doEndTag() throws JspTagException {
		Tag t = findAncestorWithClass(this, DataGridTag.class);
		DataGridTag parent = (DataGridTag) t;
		parent.setColumn(title,field,width,rowspan,colspan,align,sortable,checkbox,formatter,hidden,replace,treefield,image,query,url,funname,arg,queryMode, dictionary,frozenColumn);
		return EVAL_PAGE;
	}
	//--------end ------------author:邢双阳  ---date：2013-5-13  for：【103】页面载入时，数据是否载入采取可配置模式
	
}
