package com.base.constant;

public interface ErrorCode {
	int INVALID_ARGUMENT = 40000;			//参数异常
	int ACCOUNT_ALREADY_REGISTER = 40001;	//账号已经别注册
	int VERIFYCODE_ERROR = 40002;			//验证码错误
	int WRONG_DATA = 40003;					//业务数据异常
	int WRONG_PASSWORD = 40004;				//密码错误
	int UPLOAD_FILE_ERROR= 40005;			//上传文件失败
	int INSUFFCIENT_INVENTORY = 40006;		//库存不足
	int INTERNAL_ERROR = 50000;				//内部处理异常
	
}
