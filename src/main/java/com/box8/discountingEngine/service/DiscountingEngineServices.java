package com.box8.discountingEngine.service;


import com.box8.discountingEngine.domain.RequestData;
import com.box8.discountingEngine.domain.ResponseData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.MalformedURLException;

@Service
@Component
public interface DiscountingEngineServices {

    ResponseData getDiscount(RequestData requestData) throws IOException;

}
