package com.box8.discountingEngine.domain;

import java.util.ArrayList;
import java.util.Date;


public class CouponCode {
         private Integer id;
         private String code;
         private String type;
         private double value;
         private double cashback_value;
         private Date start_date;
         private Date end_date;
         private Boolean active;
         private ArrayList<Integer> applicable_outlet_ids;
         private double minimum_delivery_amount_after_discount;
         private double maximum_discount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getCashback_value() {
        return cashback_value;
    }

    public void setCashback_value(double cashback_value) {
        this.cashback_value = cashback_value;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ArrayList<Integer> getApplicable_outlet_ids() {
        return applicable_outlet_ids;
    }

    public void setApplicable_outlet_ids(ArrayList<Integer> applicable_outlet_ids) {
        this.applicable_outlet_ids = applicable_outlet_ids;
    }

    public double getMinimum_delivery_amount_after_discount() {
        return minimum_delivery_amount_after_discount;
    }

    public void setMinimum_delivery_amount_after_discount(double minimum_delivery_amount_after_discount) {
        this.minimum_delivery_amount_after_discount = minimum_delivery_amount_after_discount;
    }

    public double getMaximum_discount() {
        return maximum_discount;
    }

    public void setMaximum_discount(double maximum_discount) {
        this.maximum_discount = maximum_discount;
    }

    @Override
    public String toString() {
        return "CouponCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", cashback_value=" + cashback_value +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", active=" + active +
                ", applicable_outlet_ids=" + applicable_outlet_ids +
                ", minimum_delivery_amount_after_discount=" + minimum_delivery_amount_after_discount +
                ", maximum_discount=" + maximum_discount +
                '}';
    }
}
