package org.jeecgframework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.jeecgframework.core.util.oConvertUtils;


public class BaseTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String type = "default";// 加载类型
	
	private String basePath="";//默认项目跟路径

	public void setType(String type) {
		this.type = type;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}


	@Override
	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			StringBuffer sb = new StringBuffer();

			String types[] = type.split(",");
			if (oConvertUtils.isIn("jquery", types)) {
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/jquery/jquery-1.8.3.js\"></script>");
			}
			if (oConvertUtils.isIn("easyui", types)) {
				//sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/jquery.cookie.js\"></script>");
				//sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/changeEasyuiTheme.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/dataformat.js\"></script>");
				sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\""+basePath+"/plug-in/easyui/themes/default/easyui.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" href=\""+basePath+"/plug-in/easyui/themes/icon.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+basePath+"/plug-in/accordion/css/accordion.css\">");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/easyui/jquery.easyui.min.1.3.2.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/easyui/locale/easyui-lang-zh_CN.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/syUtil.js\"></script>");
			}
			if (oConvertUtils.isIn("DatePicker", types)) {
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/My97DatePicker/WdatePicker.js\"></script>");
			}
			if (oConvertUtils.isIn("jqueryui", types)) {
				//----------------------------------------------------------------
				//update-begin--Author:zhangdaihao  Date:20130205 for：自动补全
//				sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery-ui/js/jquery-1.8.3.js\"></script>");
				sb.append("<link rel=\"stylesheet\" href=\""+basePath+"/plug-in/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/jquery-ui/js/jquery-ui-1.9.2.custom.min.js\"></script>");
			}
			if (oConvertUtils.isIn("prohibit", types)) {
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/prohibitutil.js\"></script>");		}
			if (oConvertUtils.isIn("designer", types)) {
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/designer/easyui/jquery-1.7.2.min.js\"></script>");
				sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\""+basePath+"/plug-in/designer/easyui/easyui.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" href=\""+basePath+"/plug-in/designer/easyui/icon.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/designer/easyui/jquery.easyui.min.1.3.0.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/designer/easyui/locale/easyui-lang-zh_CN.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/syUtil.js\"></script>");
				
				sb.append("<script type=\'text/javascript\' src=\'"+basePath+"/plug-in/jquery/jquery-autocomplete/lib/jquery.bgiframe.min.js\'></script>");
				sb.append("<script type=\'text/javascript\' src=\'"+basePath+"/plug-in/jquery/jquery-autocomplete/lib/jquery.ajaxQueue.js\'></script>");
				sb.append("<script type=\'text/javascript\' src=\'"+basePath+"/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js\'></script>");
				sb.append("<link href=\""+basePath+"/plug-in/designer/designer.css\" type=\"text/css\" rel=\"stylesheet\" />");
				sb.append("<script src=\""+basePath+"/plug-in/designer/draw2d/wz_jsgraphics.js\"></script>");
				sb.append("<script src=\'"+basePath+"/plug-in/designer/draw2d/mootools.js\'></script>");
				sb.append("<script src=\'"+basePath+"/plug-in/designer/draw2d/moocanvas.js\'></script>");
				sb.append("<script src=\'"+basePath+"/plug-in/designer/draw2d/draw2d.js\'></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/MyCanvas.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/ResizeImage.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/event/Start.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/event/End.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/connection/MyInputPort.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/connection/MyOutputPort.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/connection/DecoratedConnection.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/Task.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/UserTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/ManualTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/ServiceTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/gateway/ExclusiveGateway.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/gateway/ParallelGateway.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/boundaryevent/TimerBoundary.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/boundaryevent/ErrorBoundary.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/subprocess/CallActivity.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/ScriptTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/MailTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/ReceiveTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/task/BusinessRuleTask.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/designer.js\"></script>");
				sb.append("<script src=\""+basePath+"/plug-in/designer/mydesigner.js\"></script>");

			}
			if (oConvertUtils.isIn("tools", types)) {
				//----begin -----Author:邢双阳   ---日期：2013-5-14----for：取消lhgDiaglog的风格设置参数，增加页面加载性能-----
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/lhgDialog/lhgdialog.min.js\"></script>");
				//----end -----Author:邢双阳   ---日期：2013-5-14----for：取消lhgDiaglog的风格设置参数，增加页面加载性能-----
				//sb.append("<script type=\"text/javascript\" src=\"plug-in/artDiglog/plugins/iframeTools.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/curdtools.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/easyuiextend.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/json2.js\"></script>");
			}
			if (oConvertUtils.isIn("toptip", types)) {
				sb.append("<link rel=\"stylesheet\" href=\""+basePath+"/plug-in/toptip/css/css.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/toptip/manhua_msgTips.js\"></script>");
			}
//			sb.append("<link rel=\"stylesheet\" href=\"resources/css/base.css\" type=\"text/css\"></link>");

			// sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/myplug/easycurd.js\"></script>");
			// sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/myplug/mask.js\"></script>");
			// sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/myplug/windowControl.js\"></script>");

			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
