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

    @Test
    public void myFirstTest() {
        assertEquals(2, 1 + 1);
    }


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






    // basic methids ----------------------------------------------
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
        cd.setQuantity(quantity);
        cd.setUnit_cost(cost+500);
        c.add(cartData);
        c.add(cd);
        requestData.setCart_items(c);
        return requestData;
    }
    public String getResponseJsonString(Boolean valid,String message,Double discount, Double casBack){
        StringBuffer s = new StringBuffer();
        s.append("{\"valid\": ").append(valid).append(",\"message\":\"").append(message).append("\",\"discount\":").append(discount).append(",\"cashback\":").append(casBack).append("}");
        return String.valueOf(s);
    }
}