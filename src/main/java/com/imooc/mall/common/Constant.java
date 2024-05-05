package com.imooc.mall.common;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class Constant {
//    public static final String IMOOC_MALL_USER="imooc_mall_user";
    public static final String SALT="8svbsvjkweDF,.03[";

    public static final String EMAIL_FROM="3648901811@qq.com";

    public static final String EMAIL_SUBJECT="您的验证码";
    public static final String JWT_KEY = "imooc_mall_user";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String USER_ROLE = "user_role";
    public static final long EXPIRE_TIME = 60*1000*60*24*1000L;//单位是毫秒
    public static final String JWT_TOKEN = "jwt_token";
    public static final Integer IMAGE_SIZE = 400;
    public static final String WATER_MARK_JPG = "waterMark.jpg";
    public static final Float IMAGE_OPACITY = 0.5f;

    public static String FILE_UPLOAD_DIR="D:/fileImage/";

    //0代表未勾选，1代表已勾选
    public enum CartSelected{
        CART_SELECTED(1,"已勾选"),
        CART_NOT_SELECTED(0,"未勾选");
        private Integer selectedCode;
       private String selectedMsg;

        CartSelected(Integer selectedCode, String selectedMsg) {
            this.selectedCode = selectedCode;
            this.selectedMsg = selectedMsg;
        }

        public Integer getSelectedCode() {
            return selectedCode;
        }

        public void setSelectedCode(Integer selectedCode) {
            this.selectedCode = selectedCode;
        }

        public String getSelectedMsg() {
            return selectedMsg;
        }

        public void setSelectedMsg(String selectedMsg) {
            this.selectedMsg = selectedMsg;
        }
    }

    public interface productStatus{
       Integer NOT_SALE=0;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price asc","price desc");
    }

    public enum OrderStatus{
        CANCEL(0,"订单取消"),
        NOT_PAY(10,"订单未支付"),
        PAY(20,"订单已支付"),
        DELIVER(30,"订单货物进行发送中"),
        FINISH(40,"订单已完成");
        private Integer statusCode;
        private String statusCodeName;

        OrderStatus(Integer statusCode, String statusCodeName) {
            this.statusCode = statusCode;
            this.statusCodeName = statusCodeName;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusCodeName() {
            return statusCodeName;
        }

        public void setStatusCodeName(String statusCodeName) {
            this.statusCodeName = statusCodeName;
        }
    }
}
