package com.box8.discountingEngine.service;

import com.box8.discountingEngine.domain.*;
import com.box8.discountingEngine.util.ConnectionUtil;
import com.box8.discountingEngine.util.HelperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.TreeMap;

import static com.box8.discountingEngine.util.GlbalVariables.COUPON_CODE_DATA_URL;
import static com.box8.discountingEngine.util.GlbalVariables.NO_DISCOUNT;

@Service
public class DiscountingServicesImpl implements DiscountingEngineServices {

    private static Integer messageFlag = 0;
    private static Boolean validFlag = true;
    private static Boolean cashBackValid = true;

    @Override
    public ResponseData getDiscount(RequestData requestData) throws IOException {
        ResponseData responseData = new ResponseData();
        messageFlag = 0;
        validFlag = true;
        cashBackValid = true;
        CouponData data = getCouponData();
        if(!requestData.validate(requestData)){
            messageFlag = 7;
            validFlag = false;
            cashBackValid = false ;
            responseData.setMessage(getMessageForFlag());
            responseData.setCashback(NO_DISCOUNT);
            responseData.setValid(validFlag);
            responseData.setDiscount(NO_DISCOUNT);
            return responseData;
        }
        double discountValue = getDiscountByCoupon(data, requestData);
        responseData.setDiscount(discountValue);

        responseData.setMessage(getMessageForFlag());
        responseData.setValid(validFlag);
        responseData.setCashback(getCashbackForRequest(requestData, data));

        return responseData;
    }


    private double getDiscountByCoupon(CouponData data, RequestData requestData) {
        double discountedValue = 0;
        String upCase = requestData.getCouponCode().toUpperCase();
        requestData.setCouponCode(requestData.getCouponCode().toUpperCase());
        CouponCode c = null;
        for (CouponCode cc : data.getCoupon_codes()) {
            if (Objects.equals(cc.getCode(), requestData.getCouponCode())) {
                c = cc;
                break;
            }
        }
        if (c == null) {
            messageFlag = 1;
            validFlag = false;
            cashBackValid = false ;
         return NO_DISCOUNT;
        }
        if(!c.getActive()){
            messageFlag = 1;
            validFlag = false;
            cashBackValid = false ;
            return NO_DISCOUNT;
        }
        //setting the code to always uppercase

        if(!c.getApplicable_outlet_ids().isEmpty()){
            if (!c.getApplicable_outlet_ids().contains(requestData.getOutletId())) {
                //return no discount
                messageFlag = 2;
                validFlag = false;
                cashBackValid = false ;
                return NO_DISCOUNT;
            }
        }
        Date currentDate = new Date();
        if (!HelperUtil.ifExistsBetweenDates(currentDate, c.getStart_date(), c.getEnd_date())) {
            messageFlag = 3;
            validFlag = false;
            cashBackValid = false ;
            return NO_DISCOUNT;
        }

        if (Objects.equals(c.getType(), "Percentage")) {
            discountedValue = getPercentageDiscount(c, requestData);
        } else if (Objects.equals(c.getType(), "Discount")) {
            discountedValue = getFlatDiscount(c, requestData);
        } else if (Objects.equals(c.getType(), "Discount&Cashback")) {
            discountedValue = getFlatAndCashBackDiscount(c, requestData);
        } else if (Objects.equals(c.getType(), "Percentage&Cashback")) {
            discountedValue = getPercentageAndCashBackDiscount(c, requestData);
        } else if (Objects.equals(c.getType(), "Bogo")) {
            discountedValue = getBOGODiscount(c, requestData);
        } else {
            messageFlag = 6;
            validFlag = false;
            cashBackValid = false ;
            return NO_DISCOUNT;
        }

        return discountedValue;
    }


    //  Buy one get one free logic --------------------------------------------------
    private double getBOGODiscount(CouponCode c, RequestData requestData) {
        TreeMap<Double, Integer> tmp = new TreeMap<>();
        Double discount = NO_DISCOUNT;
        Integer x = null;
        Integer count = 0;
        for (CartData cd : requestData.getCart_items()) {
            for (int i = 0; i < cd.getQuantity(); i++) {
                x = tmp.get(cd.getUnit_cost());
                count++;
                // If this is first occurrence of element
                if (tmp.get(cd.getUnit_cost()) == null)
                    tmp.put(cd.getUnit_cost(), 1);
                    // If elements already exists in tree map
                else
                    tmp.put(cd.getUnit_cost(), ++x);
            }
        }

        Integer loopSize = count / 2;
        discount = getFreeItemsTotalValue(tmp, loopSize);
        return checkDiscountParams(c, discount, requestData);
    }

    // percentage + cash back -------------------------------------------------
    private double getPercentageAndCashBackDiscount(CouponCode c, RequestData requestData) {

        double discountPercent = c.getValue();
        double totalValue = getTotalValue(requestData);
        return checkDiscountParams(c, ((totalValue * discountPercent) / 100), requestData);
    }

    // flat + cash back ------------------
    private double getFlatAndCashBackDiscount(CouponCode c, RequestData requestData) {
        double flatDiscount = c.getValue();
        double totalValue = getTotalValue(requestData);
        return checkDiscountParams(c, flatDiscount, requestData);
    }

    // flat discount ----------------------------------
    private double getFlatDiscount(CouponCode c, RequestData requestData) {
        double flatDiscount = c.getValue();
        double totalValue = getTotalValue(requestData);
        return checkDiscountParams(c, flatDiscount, requestData);
    }


    //    Percentage Discount ------------------------------------------------------------
    private double getPercentageDiscount(CouponCode c, RequestData requestData) {

        double discountPercent = c.getValue();
        double totalValue = getTotalValue(requestData);
        return checkDiscountParams(c, (totalValue * discountPercent) / 100, requestData);
    }


    //    Common methods for all discounts =================================================

    private CouponData getCouponData() throws IOException {
        String response = ConnectionUtil.getStringFromUrl(COUPON_CODE_DATA_URL);
        return new ObjectMapper().readValue(response, CouponData.class);
    }


    private double checkDiscountParams(CouponCode couponCode, double discount, RequestData requestData) {
        if (couponCode.getMaximum_discount() < discount) {
            messageFlag = 4;
            discount = couponCode.getMaximum_discount();
        }
        double totalValue = getTotalValue(requestData);
        if (couponCode.getMinimum_delivery_amount_after_discount() > totalValue - discount) {
            messageFlag= 5;
            cashBackValid=false;
            discount = totalValue - couponCode.getMinimum_delivery_amount_after_discount();
        }
        if (discount < 0) {
            discount = 0;
        }
        return discount;
    }

    private double getTotalValue(RequestData requestData) {
        double totalValue = 0;
        for (CartData cd : requestData.getCart_items()) {
            totalValue += cd.getQuantity() * cd.getUnit_cost();
        }
        return totalValue;
    }


    private Double getFreeItemsTotalValue(TreeMap<Double, Integer> tmp, Integer loopSize) {
        Integer x;
        Double discount = NO_DISCOUNT;
        for (int i = 0; i < loopSize; i++) {
            if (tmp.firstEntry().getValue() > 1) {
                discount += tmp.firstEntry().getKey();
                x = tmp.firstEntry().getValue();
                x -= 1;
                tmp.put(tmp.firstKey(), x);
            } else {
                discount += tmp.pollFirstEntry().getKey();
            }
        }
        return discount;
    }


    private double getCashbackForRequest(RequestData requestData, CouponData data) {
        CouponCode c = null;
        for (CouponCode cc : data.getCoupon_codes()) {
            if (Objects.equals(cc.getCode(), requestData.getCouponCode())) {
                c = cc;
                break;
            }
        }

        if (c == null) {
            messageFlag = 2;
            validFlag = false;
            cashBackValid = false ;
            return NO_DISCOUNT;
        }

        if(!cashBackValid){
            return NO_DISCOUNT;
        }
        return c.getCashback_value();
    }

    public String getMessageForFlag() {
        switch (messageFlag) {
            case 0 : return "successful";
            case 1 : return "invalid coupon code";
            case 2 : return "invalid Outlet ID";
            case 3 : return "invalid Date";
            case 4 : return "Success!! discount exceeds max setting it to max";
            case 5 : return "Success!! total value below min delivery amount adjusting appropriately";
            case 6 : return "invalid coupon discount type";
            case 7 : return "invalid input json";

            default: return "Unknown error";

        }
        }
    }

