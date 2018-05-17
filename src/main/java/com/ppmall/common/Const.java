package com.ppmall.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String FORGET_TOKEN = "forgetToken";

    public interface Role {
        int ROLE_ADMIN = 1;
        int ROLE_CUSTOMER = 0;
    }

    /**
     * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
     */
    public enum OrderStatus {
        CANCEL(0, "已取消"),
        UNPAY(10, "未付款"),
        PAID(20, "已付款"),
        SHIPED(40, "已发货"),
        SUCCESS(50, "交易成功"),
        CLOSE(60, "交易关闭");


        OrderStatus(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static OrderStatus codeOf(int code) {
            for (OrderStatus orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("无对应的枚举");
        }

    }

    public enum PayType {
        WECHAT(0, "微信支付"),
        ALIPAY(1, "支付宝");

        PayType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static PayType codeOf(int code) {
            for (PayType payType : values()) {
                if (payType.getCode() == code) {
                    return payType;
                }
            }
            throw new RuntimeException("无对应的枚举");
        }


    }
}
