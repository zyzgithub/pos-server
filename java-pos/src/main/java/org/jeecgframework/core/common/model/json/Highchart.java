package org.jeecgframework.core.common.model.json;
import java.util.List;
/**
 *  统计报表模型
 * @author Administrator
 *
 */
public class Highchart {
private String name;
private String type;//类型
@SuppressWarnings("rawtypes")
private List data;//数据
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
@SuppressWarnings("rawtypes")
public List getData() {
	return data;
}
@SuppressWarnings("rawtypes")
public void setData(List data) {
	this.data = data;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

}
