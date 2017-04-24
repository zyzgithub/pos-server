package com.courier_mana.common;


public interface Constants {
	//组织架构级别
	int NATION_LEVEL = 1;		//国家
	int PROVINCE_LEVEL =  2;	//省份
	int CITY_LEVEL = 3;			//城市
	int DISTINCT_LEVEL = 4;		//区
	int AREA_LEVEL = 5;			//片区
	int BRANCH_LEVEL = 6;				//网点
	
	int NORMAL_DEDUCT = 0;    //普通订单提成
	int MANA_DEDUCT = 1;      //管理层提成
	int BRANCH_DEDUCT = 2;       //一级子网点提成
	int CROWDSOURCING_DEDUCT = 3;      //众包提成
	
	
	int ONDUTY = 0;		//打卡上班
	int OFFDUTY = 1; 	//打卡下班
	
	String NORMAL_RECHARGE = "0";					//普通充值
	String COURIER_PROMOTION_RECHARGE = "1";		//快递员推广充值
	
	int PLAT_COURIER = 1;		//平台快递员
	int AGENT_COURIER = 2;		//代理商快递员
	int CROWDSOURCING_COURIER = 3;	//众包快递员
	
	//考勤天数设置
	String LOWEST_WORKHOURS_SET = "attendance_set";
	//用户封号规则
	String PUNCH_CONSTRAINT_SET = "punch";		//打卡规则
	String SCAMBLE_CONSTRAINT_SET = "snatch";	//抢单规则
	String OPENUP_CONSTRAINT_SET = "openup";	//开拓商家
	
	
	//用户状态
	int NORMAL = 1;			//正常
	int FIRST_REGISTER = 2;		//首次注册
	int WAITING_AUDIT = 3;		//等待审核
	int INFO_MODIFIED = 4;		//资料修改
	int LOCKED = 5;				//被锁定
	
	
	//快递员种类
	public static final Integer COURIER = 1;                        	//平台快递员
	public static final Integer COURIER_COOPERATE_BUSINESS = 2;     	//合作商快递员
	public static final Integer COURIER_CROWDSOURING = 3;           	//众包快递员
	public static final Integer COURIER_TYPE_SUPPLY_DRIVER = 4;			//供应链司机
	public static final Integer COURIER_TYPE_SUPPLY_DRIVER_FLAT = 41;	//供应链司机兼平台司机
	public static final Integer COURIER_TYPE_SUPPLY_NORMAL = 5;			//供应链快递员
	public static final Integer COURIER_TYPE_SUPPLY_NORMAL_FLAT = 51;	//供应链快递员兼平台快递员
								  
	// 订单来源
    String CROWDSOURCING_ORDER = "crowdsourcing";   //众包类型订单
    String SUPPLYCHAIN_ORDER = "supplychain";   	//供应链订单
    
    int SUPPLYCHAIN_MERCHANT_ORDER = 1;
    int SUPPLYCHAIN_WAREHOUSE_ORDER = 2;
    
	/** 缓存商家ID与商家来源 的 KEY */
    public static final String MERCHANT_SOURCE_KEY = "MERCHANT_SOURCE_";
    
    /** 乡村基 */
    public static final String MERCHANT_SOURCE_RARUL = "1";
    /** 私厨 */
    public static final String MERCHANT_SOURCE_PRIVATE = "2";
    /** Retail超市类型 */
    public static final String MERCHANT_SOURCE_RETAIL = "7";
    
    //众包快递员上传位置到redis锁，有其它线程在读
    public static final String LOCK_COURIER_POS = "lock_courier_pos";
    
    public static final String DRIVING_MODE = "driving";	
    public static final String RIDING_MODE = "riding";
    public static final String WALKING_MODE = "walking";
    
    
    public static final String ACCEPTED = "accepted";	//快递员抢单后
    public static final String DELIVERYING = "deliverying";	//快递员开始配送
    public static final String CONFIRM = "confirm";			//快递员确认
	
}
