package org.jeecgframework.codegenerate.generate;

public enum CodeType {

	A("","");
	private CodeType(String label,String code){
		this.label = label;
		this.code = code;
	}
	
	private String label;
	
	private String code;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
