package com.wm.controller.open_api;

/**
 * 第三方平台类型
 * @author folo
 *
 */
public enum ThirdPlat {
	tswj("i玩派"),iwash("我要洗衣");
	public String cn_name;
	private ThirdPlat(String msg){
		this.cn_name = msg;
	}
	
	public static ThirdPlat toThirdPlat(String plat){
		for (ThirdPlat thirdPlat : ThirdPlat.values()) {
			if(thirdPlat.toString().equals(plat)) return thirdPlat;
		}
		return null;
	}
	
	public boolean equals(ThirdPlat plat){
		if(plat.toString().equals(this.toString())) return true;
		return false;
	}
	
	public boolean equals(String name){
		if(this.toString().equals(name)) return true;
		return false;
	}
	
	/**
	 * 是否1.5天生玩家
	 * @return
	 */
	public boolean isOld(){
		if(this.toString().equals(ThirdPlat.tswj.toString())) return true;
		return false;
	}
}
