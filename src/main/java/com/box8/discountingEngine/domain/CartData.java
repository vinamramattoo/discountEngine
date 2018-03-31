package com.box8.discountingEngine.domain;


public class CartData {

   private Integer product_id;
   private Integer quantity;
   private double unit_cost;

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(double unit_cost) {
        this.unit_cost = unit_cost;
    }

    @Override
    public String toString() {
        return "CartData{" +
                "product_id=" + product_id +
                ", quantity=" + quantity +
                ", unit_cost=" + unit_cost +
                '}';
    }
    boolean validate(CartData cartData){
        return !(cartData.getProduct_id() == null || cartData.getQuantity() == null || cartData.getQuantity() == null);
    }
}
