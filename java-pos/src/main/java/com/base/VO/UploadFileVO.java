/**
 * 
 */
package com.base.VO;


import com.base.util.Base64;


/**
 * @author Administrator
 * 
 */
public class UploadFileVO {

	public UploadFileVO() {
		super();
	}
	private String file;
	private String name;
	private String type;
	





	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}








	public byte[] decodePicBase64() {
		// 处理base64加号问题
		return Base64.decode(file.replaceAll(" ", "+"));
	}


}
