package com.geigeoffer.honeycombcoupon.merchant.admin.common.log;

import com.mzt.logapi.service.IParseFunction;

public class CommonEnumParseFunction implements IParseFunction {

    public static final String DISCOUNT_TARGET_ENUM_NAME = DiscountTargetNum.class.getSimpleName();
    @Override
    public String functionName() {
        return "COMMON_ENUM_PARSE";
    }

//    @Override
//    public String apply(Object value) {
//        try
//    }
}
