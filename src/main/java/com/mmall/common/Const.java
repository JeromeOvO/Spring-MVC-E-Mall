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
}
