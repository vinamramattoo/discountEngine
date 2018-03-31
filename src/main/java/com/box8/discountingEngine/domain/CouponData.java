package com.box8.discountingEngine.domain;


import java.util.ArrayList;

public class CouponData {
private ArrayList<CouponCode> coupon_codes;

    public ArrayList<CouponCode> getCoupon_codes() {
        return coupon_codes;
    }

    public void setCoupon_codes(ArrayList<CouponCode> coupon_codes) {
        this.coupon_codes = coupon_codes;
    }
}
