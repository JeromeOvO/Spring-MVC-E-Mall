package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by 70457 on 1/12/2023.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";


    public interface Cart{
        int CHECKED = 1;
        int UN_CHECKED = 0;

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProdcutListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Role{
        int ROLE_CUSTOMER = 0;//Normal User
        int ROLE_ADMI = 1;//administrator
    }

    public enum ProductStatusEnum{
        ON_SALE(1, "Online");
        private String value;
        private int code;

        ProductStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OrderStatusEnum{
        CANCELED(0, "Canceled"),
        NO_PAY(10, "No Pay"),
        PAID(20, "Paid"),
        SHIPPED(40, "Shipped"),
        ORDER_SUCCESS(50, "Order Success"),
        ORDER_CLOSED(60, "Order Closed");

        OrderStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw  new RuntimeException("Can't recording Example");
        }
    }

    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum{
        ALIPAY(1, "Alipay");

        PayPlatformEnum(int code, String value){
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentTpyeEnum{
        ONLINE_PAY(1, "Online Pay");

        PaymentTpyeEnum(int code, String value){
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTpyeEnum codeOf(int code){
            for(PaymentTpyeEnum paymentTpyeEnum : values()){
                if(paymentTpyeEnum.getCode() == code){
                    return paymentTpyeEnum;
                }
            }
            throw  new RuntimeException("Can't recording Example");
        }
    }
}
