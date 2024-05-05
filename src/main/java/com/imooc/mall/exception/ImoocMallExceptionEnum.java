package com.imooc.mall.exception;

public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_USER_PASSWORD(10002, "用户密码不能为空"),
    USER_PASSWORD_SHORT(10003, "密码不能小于8位数"),
    INSERT_FAILED(10004, "插入失败"),
    USER_EXISTED(10005, "用户名重复"),
    NEED_USER_REGISTER(10006, "用户需要注册"),
    USER_PASSWORD_ERROR(10007, "密码错误"),
    NEED_LOGIN(10008,"用户未登录" ),
    NEED_ADMIN(10009, "需要管理员权限"),
    SYSTEM_ERROR(20000, "系统异常"),
    CATEGORY_EXISTED(10010, "目录已经存在"),
    CATEGORY_NOT_EXISTED(10011, "目录不存在"),
    UPDATE_FAILED(10012, "更新失败"),
    DELETE_FAILED(10013, "删除失败"),
    PRODUCT_EXISTED(10014,"商品已经存在" ),
    MAKE_FAILED(10015, "创建失败"),
    FILE_UPLOAD_FAILED(10016, "文件加载失败"),
    PRODUCT_NOT_EXISTED(10017, "该商品不存在"),
    NOT_SALE(10018, "该商品已经下架"),
    LOWS_STOCK(10019, "库存不足"),
    CART_NOT_EXISTED(10020, "购物车不存在"),
    NOT_ORDER(10021, "订单不存在"),
    STATUS_ERROR(10022, "订单状态不符"),
    ORDER_BOT_CANCEL(10023, "该订单不能被取消"),
    WRONG_EMAIL(10024, "邮件地址非法"),
    EMAIL_ALREADY_BEEN_REGISTERED(10025, "邮件已经被注册"),
    EMAIL_EXISTED(10026, "验证码已发，若没获得，则等待下一次"),
    TOKEN_EXPIRED(10027, "token已过期，请重新登录或设置token"),
    TOKEN_WRONG(10028, "token解码失败"),
    CODE_ERROR(10029, "验证码错误"),
    CANCEL_WRONG_ORDER_STATUS(10030,"订单状态有误,发货后暂不支持取消订单"),
    PAY_WRONG_ORDER_STATUS(10031,"订单状态有误,仅能在未付款时付款"),
    DELIVER_WRONG_ORDER_STATUS(10032,"订单状态有误,仅能在付款后发货"),
    FINISH_WRONG_ORDER_STATUS(10033,"订单状态有误,仅能在发货后完单"),

    QR_WRONG_ORDER_STATUS(10034, "订单状态有误,仅能在未付款时进行生成支付二维码"),
    REQUEST_PARAM_ERROR(10035, "参数错误");


    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ImoocMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
