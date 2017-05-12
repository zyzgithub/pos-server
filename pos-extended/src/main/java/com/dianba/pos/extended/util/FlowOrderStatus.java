package com.dianba.pos.extended.util;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public enum FlowOrderStatus{

    TheTopUp("充值中", "1"), ChargeError("充值失败", "2"), ChargeSuccess("充值成功", "3"), Review("待审核", "4");
    // 成员变量
    private String name;
    private String index;
    // 构造方法
    private FlowOrderStatus(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    //覆盖方法
    @Override
    public String toString() {
        return this.index+"_"+this.name;
    }
}
