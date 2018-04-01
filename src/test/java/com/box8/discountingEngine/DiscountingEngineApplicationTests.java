package com.box8.discountingEngine;

import com.box8.discountingEngine.domain.CartData;
import com.box8.discountingEngine.domain.RequestData;
import com.box8.discountingEngine.domain.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountingEngineApplicationTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String baseURI = "http://localhost:9091/engine/discount";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }


// percentage test ------------------
    @Test
    public void testPercentageCoupon() throws Exception{
       RequestData requestData = createRequestData(1,"BOX8LOVE",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",50.0,0.0);
       mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }


    @Test
    public void testPercentageLowRateCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"BOX8LOVE",  1,1,100.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"Success!! total value below min delivery amount adjusting appropriately",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testPercentageTooMuchDiscountCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"BOX8LOVE",  1,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"Success!! discount exceeds max setting it to max",200.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testPercentageNullQuantityCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"BOX8LOVE",  1,null,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testPercentageNullProductCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"BOX8LOVE",  null,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testPercentageNullCostCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"BOX8LOVE",  1,1,null);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }





    @Test
    public void testPercentageNullCoupon() throws Exception{
        RequestData requestData = createRequestData(1,null,  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }






    // Flat test ------------------
    @Test
    public void testFlatCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"HELLOBOX8",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",150.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }


    @Test
    public void testFlatWhenNoOutletProvidedCoupon() throws Exception{
        RequestData requestData = createRequestData(100,"HELLOBOX8",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",150.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }


    @Test
    public void testFlatLowRateCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"HELLOBOX8",  1,1,100.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"Success!! total value below min delivery amount adjusting appropriately",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testFlatTooMuchDiscountCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"HELLOBOX8",  1,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",150.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testFlatNullQuantityCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"HELLOBOX8",  1,null,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testFlatNullProductCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"HELLOBOX8",  null,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testFlatNullCostCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"HELLOBOX8",  1,1,null);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }





    // DiscountAndCashback test ------------------
    @Test
    public void testDiscountAndCashbackCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"GETCASHBACK",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",150.0,150.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }


    @Test
    public void testDiscountAndCashbackLowRateCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"GETCASHBACK",  1,1,100.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"Success!! total value below min delivery amount adjusting appropriately",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testDiscountAndCashbackTooMuchDiscountCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"GETCASHBACK",  1,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",150.0,150.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testDiscountAndCashbackNullQuantityCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"GETCASHBACK",  1,null,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testDiscountAndCashbackNullProductCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"GETCASHBACK",  null,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testDiscountAndCashbackNullCostCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"GETCASHBACK",  1,1,null);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }



    // BOGO test ------------------
    @Test
    public void testBOGOCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"BOGO",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testBOGOInvalidOutletCoupon() throws Exception{
        RequestData requestData = createRequestData(1,"BOGO",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid Outlet ID",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testBOGOCodeInLowerCaseCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"bogo",  1,1,500.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }



    @Test
    public void testBOGOLowRateCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"BOGO",  1,1,100.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"Success!! total value below min delivery amount adjusting appropriately",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testBOGOTooMuchDiscountCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"BOGO",  1,4,1000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"Success!! discount exceeds max setting it to max",150.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testBOGONullQuantityCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"BOGO",  1,null,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testBOGONullProductCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"BOGO",  null,1,10000.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }

    @Test
    public void testBOGONullCostCoupon() throws Exception{
        RequestData requestData = createRequestData(2,"BOGO",  1,1,null);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(false,"invalid input json",0.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }


    @Test
    public void testBOGOMultipleCartItemsCoupon() throws Exception{
        RequestData requestData = createRequestDataMultipleCart(2,"BOGO",  1,1,30.0);

        Gson gson = new Gson();
        String json = gson.toJson(requestData);
        System.out.println(json);
        String s = getResponseJsonString(true,"successful",140.0,0.0);
        mockMvc.perform(post(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andExpect(content().json(s));
    }


    // basic methods ----------------------------------------------
    public RequestData createRequestData(Integer outletId,String coupon , Integer product , Integer quantity , Double cost){
        RequestData requestData= new RequestData();
        requestData.setOutletId(outletId);
        requestData.setCouponCode(coupon);
        List<CartData> c = new LinkedList<>();

        CartData cartData = new CartData();
        cartData.setProduct_id(product);
        cartData.setQuantity(quantity);
        cartData.setUnit_cost(cost);
        c.add(cartData);
        requestData.setCart_items(c);
        return requestData;
    }

    public RequestData createRequestDataMultipleCart(Integer outletId,String coupon , Integer product , Integer quantity , Double cost){
        RequestData requestData= new RequestData();
        requestData.setOutletId(outletId);
        requestData.setCouponCode(coupon);
        List<CartData> c = new LinkedList<>();

        CartData cartData = new CartData();
        cartData.setProduct_id(product);
        cartData.setQuantity(quantity);
        cartData.setUnit_cost(cost);
        c.add(cartData);

        CartData cd = new CartData();
        cd.setProduct_id(product+2);
        cd.setQuantity(quantity+3);
        cd.setUnit_cost(cost+50);
        c.add(cartData);
        c.add(cd);
        requestData.setCart_items(c);
        return requestData;
    }

    public String getResponseJsonString(Boolean valid,String message,Double discount, Double cashBack){
        StringBuffer s = new StringBuffer();
        s.append("{\"valid\": ").append(valid).append(",\"message\":\"").append(message).append("\",\"discount\":").append(discount).append(",\"cashback\":").append(cashBack).append("}");
        return String.valueOf(s);
    }
}