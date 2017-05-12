package com.dianba.pos.common.exception.core;

import com.dianba.pos.common.exception.EnumerationCheckException;
import com.dianba.pos.common.exception.lang.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;

public enum AssertCore implements ExceptionCode {
    G0000("请求成功"),
    G1000("系统繁忙"),
    G1010("保留"),
    G4000("错误码:4000"),
    G4001("数据不存在"),
    G4002("操作过于频繁"),
    G4003("功能已经关闭"),
    G4004("参数错误"),
    G4005("保留"),
    G7000("错误码:7000"),
    G7010("保留"),
    G7011("默认地址不能删除"),
    G7012("查询不到该商家营业信息"),
    G7013("不能使用相同的密码"),
    G7014("不存在商家信息"),
    G7015("该用户不存在"),
    G7016("查询不到该用户的地址信息,请联系技术人员"),
    G7017("orderId不能为空"),
    G7018("查询不到订单信息"),
    G7019("非法支付,重复支付"),
    G7020("非法支付,用户错误"),
    G7021("非法支付,金额错误"),
    G7022("不能再次确认收货"),
    G7023("商家已打烊"),
    G7024("部分商品不存在"),
    G7025("部分商品库存不足，请到店铺内选择其他商品下单"),
    G7026("部分商品已下架，请到店铺内选择其他商品下单"),
    G7027("部分商品已售完，请到店铺内选择其他商品下单"),
    G7028("部分商品促销状态改变，请到店铺内选择其他商品下单"),
    G7029("商家已关店"),
    G7030("查询不到用户订单"),
    G9001("超出范围"),
    G9002("无效地址"),
    G9010("无法查询到该地址信息"),
    G9011("无法查询到商家信息"),
    G9012("检测存在旧密码，请输入旧密码"),
    G9013("密码验证失败"),
    G9014("不是有效的手机号码"),
    G9015("不是有效的验证码");

    public int code;
    private String msg;

    AssertCore(String msg) {
        this.msg = msg;
        this.code = Integer.parseInt(this.toString().replace("G", ""));
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void notEmpty(Collection<?> obj, String msg) {
        this.isTrue(obj == null || obj.isEmpty(), msg);
    }

    public void notEmpty(CharSequence cs, String msg) {
        this.isTrue(StringUtils.hasText(cs), msg);
    }

    public void notBlank(CharSequence cs, String msg) {
        this.isTrue(StringUtils.hasText(cs), msg);
    }

    public void notNull(Object obj, String msg) {
        this.isTrue(obj != null, msg);
    }

    public void notNull(Object obj) {
        this.isTrue(obj != null, "");
    }

    public void isTrue(boolean b, String msg) {
        Assert.isTrue(b, this, msg);
    }

    public void checkIsTrue(boolean b, String msg) throws EnumerationCheckException {
        Assert.checkIsTrue(b, this, msg);
    }

    public void isTrue(boolean b) {
        this.isTrue(b, "");
    }

    public void notEmpty(Collection<?> collection) {
        this.isTrue(collection != null && !collection.isEmpty(), "");
    }

    public void isEmpty(Collection<?> collection) {
        this.isTrue(collection == null || collection.isEmpty(), "");
    }
}
