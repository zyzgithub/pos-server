package org.jeecgframework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.jeecgframework.core.util.StringUtil;


/**
 * 
 * 类描述：选择器标签
 * 
 * @author: jeecg
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ChooseTag extends TagSupport {
	protected String id;
	protected String hiddenName;
	protected String textname;//弹出窗口文本框字段
	protected String icon;
	protected String title;
	protected String url;
	protected String top;
	protected String left;
	protected String width;
	protected String height;
	protected String name;
	protected String hiddenid;// 隐藏框取值ID
	protected Boolean isclear = false;
	protected String fun;//自定义函数
	
	protected String textid;//显示页面文本框字段


	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end().toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public StringBuffer end() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"#\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"" + icon + "\" onClick=\"choose"+id+"()\">选择</a>");
		if (isclear&&StringUtil.isNotEmpty(textname)) {
			sb.append("<a href=\"#\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"icon-redo\" onClick=\"clearAll();\">清空</a>");
		}
		sb.append("<script type=\"text/javascript\">");
		sb.append("function choose"+id+"(){");
		sb.append("$.dialog({");
		sb.append("content: \'url:"+url+"\',");
		sb.append("zIndex: 1997,");
		if (title != null) {
			sb.append("title: \'" + title + "\',");
		}
		sb.append("lock : true,");
		if (width != null) {
			sb.append("width :\'" + width + "\',");
		} else {
			sb.append("width :400,");
		}
		if (height != null) {
			sb.append("height :\'" + height + "\',");
		} else {
			sb.append("height :350,");
		}
		if (left != null) {
			sb.append("left :\'" + left + "\',");
		} else {
			sb.append("left :'85%',");
		}
		if (top != null) {
			sb.append("top :\'" + top + "\',");
		} else {
			sb.append("top :'65%',");
		}
		sb.append("opacity : 0.4,");
		sb.append("button : [ {");
		sb.append("name : \'确认\',");
		sb.append("callback : function() {");
		sb.append("iframe = this.iframe.contentWindow;");
		String[] textnames=null;
		String textids[] = null;
		if(StringUtil.isNotEmpty(textname))
		{
			textnames = textname.split(",");
			if(StringUtil.isNotEmpty(textid)){
				textids = textid.split(",");
				for (int i = 0; i < textnames.length; i++) {
					sb.append("var " + textnames[i] + "=iframe.get" + name + "Selections(\'" + textnames[i] + "\');	");
					sb.append("$(\'#" + textids[i] + "\').val(" + textnames[i] + ");");
					//update-begin--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
					sb.append("$(\'#" + textids[i] + "\').blur();");
					//update-end--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
				}
			}else{
				for (int i = 0; i < textnames.length; i++) {
					sb.append("var " + textnames[i] + "=iframe.get" + name + "Selections(\'" + textnames[i] + "\');	");
					sb.append("$(\'#" + textnames[i] + "\').val(" + textnames[i] + ");");
					//update-begin--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
					sb.append("$(\'#" + textnames[i] + "\').blur();");
					//update-end--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
				}
			}
			
	    }
		sb.append("var id =iframe.get" + name + "Selections(\'" + hiddenid + "\');");
		sb.append("if (id!== undefined &&id!=\"\"){");
		sb.append("$(\'#" + hiddenName + "\').val(id);");
		sb.append("}");
		if(StringUtil.isNotEmpty(fun))
		{
		sb.append(""+fun+"();");//执行自定义函数
		}
		sb.append("},");
		sb.append("focus : true");
		sb.append("}, {");
		sb.append("name : \'取消\',");
		sb.append("callback : function() {");
		sb.append("}");
		sb.append("} ]");
		sb.append("});");
		sb.append("}");
		if (isclear&&StringUtil.isNotEmpty(textname)) {
		sb.append("function clearAll(){");
		if(StringUtil.isNotEmpty(textid)){
			textids=textid.split(",");
			for (int i = 0; i < textnames.length; i++) {
				sb.append("$(\'#" + textids[i] + "\').val(\"\");");
				//update-begin--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
				sb.append("$(\'#" + textids[i] + "\').blur();");
				//update-end--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
			}
		}else{
			for (int i = 0; i < textnames.length; i++) {
				sb.append("$(\'#" + textnames[i] + "\').val(\"\");");
				//update-begin--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
				sb.append("$(\'#" + textnames[i] + "\').blur();");
				//update-end--Author:tanghong  Date:20130422 for：用户编辑，角色选择后，仍提示错误信息
			}
		}
		
		sb.append("$(\'#" + hiddenName + "\').val(\"\");");
		sb.append("}");
		}
		sb.append("</script>");
		return sb;
	}

	public void setHiddenName(String hiddenName) {
		this.hiddenName = hiddenName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setTextname(String textname) {
		this.textname = textname;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setIsclear(Boolean isclear) {
		this.isclear = isclear;
	}

	public void setHiddenid(String hiddenid) {
		this.hiddenid = hiddenid;
	}
	public void setFun(String fun) {
		this.fun = fun;
	}

	public void setTextid(String textid) {
		this.textid = textid;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
