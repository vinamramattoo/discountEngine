package com.box8.discountingEngine.controller;


import com.box8.discountingEngine.domain.CartData;
import com.box8.discountingEngine.domain.RequestData;
import com.box8.discountingEngine.domain.ResponseData;
import com.box8.discountingEngine.service.DiscountingEngineServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/engine")
public class DiscountingRestController {

    private static final Logger LOG = LoggerFactory.getLogger(DiscountingRestController.class);
 //   private static final String PATH = "/error";


    @Autowired
    DiscountingEngineServices discountingEngineServices;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() throws IOException {
        LOG.info("Initiated test method");
        return discountingEngineServices.test();
    }

    @RequestMapping(value = "/discount", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getDiscount(@RequestBody RequestData requestData) throws IOException {
        LOG.info("Initiated getDiscount method");
        return discountingEngineServices.getDiscount(requestData);
    }


    @RequestMapping(value = "/testing", method = RequestMethod.POST)
    @ResponseBody
    public String getDiscount(@RequestBody List<CartData> cartDatas) throws IOException {
        LOG.info("Initiated getDiscount method");
        StringBuilder s = new StringBuilder();
        for (CartData cd : cartDatas) {
            s.append(cd.toString());
        }
        return String.valueOf(s);
    }
}
