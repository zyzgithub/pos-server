package org.jeecgframework.core.interceptors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class DateConverter implements Converter<String, Date> {
	private SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	

	@Override
	public Date convert(String text) {
		if (StringUtils.hasText(text)) {
			try {
				if (text.indexOf(":") == -1 && text.length() == 10) {
					return this.dateFormat.parse(text);
				} else if (text.indexOf(":") > 0 && text.length() == 19) {
					return this.datetimeFormat.parse(text);
				//update-begin----author:zhangdaihao----------------------date:20130312 ------- for:时间格式化-------
				} else if (text.indexOf(":") > 0 && text.length() == 21) {
					text = text.replace(".0", "");
					return this.datetimeFormat.parse(text);
				//update-end----author:zhangdaihao----------------------date:20130312 ------- for:时间格式化-------
				} else {
					throw new IllegalArgumentException(
							"Could not parse date, date format is error ");
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException(
						"Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		} else {
			return null;
		}
	}

}
