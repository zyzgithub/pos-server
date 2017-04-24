package org.jeecgframework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.jeecgframework.core.util.StringUtil;


@SuppressWarnings("serial")
public class FormValidationTag extends TagSupport {
	protected String formid = "formobj";// 表单FORM ID
	protected Boolean refresh = true;
	protected String callback;// 回调函数
	protected String beforeSubmit;// 提交前处理函数
	protected String btnsub = "btn_sub";// 以ID为标记触发提交事件
	protected String btnreset = "btn_reset";// 以ID为标记触发提交事件
	protected String layout = "div";// 表单布局
	protected String usePlugin;// 外调插件
	protected boolean dialog = true;// 是否是弹出窗口模式
	protected String action;// 表单提交路径
	protected String tabtitle;// 表单选项卡
	protected String tiptype = "4";//校验方式

	public void setTabtitle(String tabtitle) {
		this.tabtitle = tabtitle;
	}

	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}

	public void setBtnsub(String btnsub) {
		this.btnsub = btnsub;
	}

	public void setRefresh(Boolean refresh) {
		this.refresh = refresh;
	}

	public void setBtnreset(String btnreset) {
		this.btnreset = btnreset;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			StringBuffer sb = new StringBuffer();
			if ("div".equals(layout)) {
				sb.append("<div id=\"content\">");
				sb.append("<div id=\"wrapper\">");
				sb.append("<div id=\"steps\">");
			}
			sb.append("<form id=\"" + formid + "\" action=\"" + action + "\" name=\"" + formid + "\" method=\"post\">");
			if ("btn_sub".equals(btnsub) && dialog)
				sb.append("<input type=\"hidden\" id=\"" + btnsub + "\" class=\"" + btnsub + "\"/>");
			out.print(sb.toString());
			out.flush();
			System.out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			
			String resPath = this.pageContext.getServletContext().getContextPath();
			
			StringBuffer sb = new StringBuffer();
			if (layout.equals("div")) {
				sb.append("<link rel=\"stylesheet\" href=\"" + resPath + "/plug-in/Validform/css/divfrom.css\" type=\"text/css\"/>");
				if (tabtitle != null)
					sb.append("<script type=\"text/javascript\" src=\"" + resPath + "/plug-in/Validform/js/form.js\"></script>");
			}
			sb.append("<link rel=\"stylesheet\" href=\"" + resPath + "/plug-in/Validform/css/style.css\" type=\"text/css\"/>");
			sb.append("<link rel=\"stylesheet\" href=\"" + resPath + "/plug-in/Validform/css/tablefrom.css\" type=\"text/css\"/>");
			sb.append("<script type=\"text/javascript\" src=\"" + resPath + "/plug-in/Validform/js/Validform_v5.3.1_min.js\"></script>");
			sb.append("<script type=\"text/javascript\" src=\"" + resPath + "/plug-in/Validform/js/Validform_Datatype.js\"></script>");
			//----------------------------------------------------------------
			//update-begin--Author:zhangdaihao  Date:20130225 for：金额校验规则
			sb.append("<script type=\"text/javascript\" src=\"" + resPath + "/plug-in/Validform/js/datatype.js\"></script>");
			//update-end--Author:zhangdaihao  Date:20130225 for：金额校验规则
			//----------------------------------------------------------------
			if (usePlugin != null) {
				if (usePlugin.indexOf("jqtransform") >= 0) {
					sb.append("<SCRIPT type=\"text/javascript\" src=\"" + resPath + "/plug-in/Validform/plugin/jqtransform/jquery.jqtransform.js\"></SCRIPT>");
					sb.append("<LINK rel=\"stylesheet\" href=\"" + resPath + "/plug-in/Validform/plugin/jqtransform/jqtransform.css\" type=\"text/css\"></LINK>");
				}
				if (usePlugin.indexOf("password") >= 0) {
					sb.append("<SCRIPT type=\"text/javascript\" src=\"" + resPath + "/plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js\"></SCRIPT>");
				}
			}
			sb.append("<script type=\"text/javascript\">");
			sb.append("$(function(){");
			sb.append("$(\"#" + formid + "\").Validform({");
			//update-begin--Author:zhaojunfu  Date:20130415 for：验证信息的显示方式改动
			if(this.getTiptype()!=null && !"".equals(this.getTiptype())){
				sb.append("tiptype:"+this.getTiptype()+",");
			}else{
				sb.append("tiptype:1,");
			}
//			sb.append("tiptype:function(msg,o,cssctl){");
//			sb.append("if(!o.obj.is(\"form\")){");
//			sb.append("	var objtip=o.obj.parent().find(\".Validform_checktip\");");
//			sb.append("	cssctl(objtip,o.type);");
//			sb.append("	objtip.text(msg);");
//			sb.append("	var infoObj=o.obj.parent().find(\".Validform_checktip\");");
//			sb.append("	if(o.type==2){");
//			sb.append("		infoObj.hide();infoObj.show();");
//			sb.append("		infoObj.fadeOut(8000);");
//			sb.append("	}else{");
//			sb.append("		infoObj.hide();");
//			sb.append("		var left=o.obj.offset().left;");
//			sb.append("		var top=o.obj.offset().top;");
//			sb.append("		infoObj.css({	");
//			sb.append("			left:left+85,");
//			sb.append("			top:top-10");
//			sb.append("		}).show().animate({");
//			sb.append("			top:top-5");
//			sb.append("		},200);infoObj.fadeOut(8000);");
//			sb.append("	}");
//			sb.append("}");
//			sb.append("},");
			//update-end--Author:zhaojunfu  Date:20130415 for：验证信息的显示方式改动
			sb.append("btnSubmit:\"#" + btnsub + "\",");
			sb.append("btnReset:\"#" + btnreset + "\",");
			sb.append("ajaxPost:true,");
			if (beforeSubmit != null) {
				sb.append("beforeSubmit:function(curform){var tag=false;");
				sb.append("return " + beforeSubmit + "(curform);");

				sb.append("},");
			}
			if (usePlugin != null) {
				StringBuffer passsb = new StringBuffer();
				if (usePlugin.indexOf("password") >= 0) {
					passsb.append("passwordstrength:{");
					passsb.append("minLen:6,");
					passsb.append("maxLen:18,");
					passsb.append("trigger:function(obj,error)");
					passsb.append("{");
					passsb.append("if(error)");
					passsb.append("{");
					passsb.append("obj.parent().next().find(\".Validform_checktip\").show();");
					passsb.append("obj.find(\".passwordStrength\").hide();");
					passsb.append("}");
					passsb.append("else");
					passsb.append("{");
					passsb.append("$(\".passwordStrength\").show();");
					passsb.append("obj.parent().next().find(\".Validform_checktip\").hide();");
					passsb.append("}");
					passsb.append("}");// trigger结尾
					passsb.append("}");// passwordstrength结尾
				}
				StringBuffer jqsb = new StringBuffer();
				if (usePlugin.indexOf("jqtransform") >= 0) {
					if (usePlugin.indexOf("password") >= 0) {
						sb.append(",");
					}
					jqsb.append("jqtransform :{selector:\"select\"}");
				}
				sb.append("usePlugin:{");
				if (usePlugin.indexOf("password") >= 0) {
					sb.append(passsb);
				}
				if (usePlugin.indexOf("jqtransform") >= 0) {
					sb.append(jqsb);
				}
				sb.append("},");
			}
			sb.append("callback:function(data){");
			if (dialog) {
				sb.append("var win = frameElement.api.opener;");
				//----------------------------------------------------------------
				//update-begin--Author:wangyang  Date:20130409 for：错误提示
				//----------------------------------------------------------------
				//先判断是否成功，成功再刷新父页面，否则return false    
		        
				//----------------------------------------------------------------
				//update-begin--Author:sun  Date:20130426 for：错误提示返回提示的展示bug
				//----------------------------------------------------------------
				// 如果不成功，返回值接受使用data.msg. 原有的data.responseText会报null 
				sb.append("if(data.success==true){frameElement.api.close();win.tip(data.msg);}else{if(data.responseText==''||data.responseText==undefined)$(\"#"+formid+"\").html(data.msg);else $(\"#"+formid+"\").html(data.responseText); return false;}");
				//----------------------------------------------------------------
				//update-begin--Author:sun  Date:20130426 for：错误提示返回提示的展示bug
				//
				if (refresh) {
					sb.append("win.reloadTable();");
				}
				if (StringUtil.isNotEmpty(callback)) {
					sb.append("win." + callback + "(data);");
				}
				//失败tip不提示
				//sb.append("win.tip(data.msg);");
				
				//----------------------------------------------------------------
				//update-end--Author:wangyang  Date:20130409 for：错误提示
				//----------------------------------------------------------------
			} else {
				sb.append("" + callback + "(data);");
			}
			sb.append("}" + "});" + "});" + "</script>");
			sb.append("");
			sb.append("</form>");
			if ("div".equals(layout)) {
				sb.append("</div>");
				if (tabtitle != null) {
					String[] tabtitles = tabtitle.split(",");
					sb.append("<div id=\"navigation\" style=\"display: none;\">");
					sb.append("<ul>");
					for (String string : tabtitles) {
						sb.append("<li>");
						sb.append("<a href=\"#\">" + string + "</a>");
						sb.append("</li>");
					}
					sb.append("</ul>");
					sb.append("</div>");
				}
				sb.append("</div></div>");
			}
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public void setUsePlugin(String usePlugin) {
		this.usePlugin = usePlugin;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public void setBeforeSubmit(String beforeSubmit) {
		this.beforeSubmit = beforeSubmit;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getTiptype() {
		return tiptype;
	}

	public void setTiptype(String tiptype) {
		this.tiptype = tiptype;
	}
	
}
