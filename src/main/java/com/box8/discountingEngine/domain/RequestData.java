package com.box8.discountingEngine.domain;

import java.util.ArrayList;
import java.util.List;

public class RequestData {
    private Integer outletId;
    private String couponCode;
    private List<CartData> cart_items;

    public Integer getOutletId() {
        return outletId;
    }

    public void setOutletId(Integer outletId) {
        this.outletId = outletId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public List<CartData> getCart_items() {
        return cart_items;
    }

    public void setCart_items(List<CartData> cart_items) {
        this.cart_items = cart_items;
    }

    public boolean validate(RequestData requestData){
    if(requestData.getOutletId() == null){
        return false;
    }
    else if(requestData.getCouponCode().equals(null) || requestData.getCouponCode().equals("")){
        return false;
    }
        for (CartData cd: requestData.getCart_items()) {
            if(!cd.validate(cd)){
                return false;
            }
        }
        return true;
    }
}
