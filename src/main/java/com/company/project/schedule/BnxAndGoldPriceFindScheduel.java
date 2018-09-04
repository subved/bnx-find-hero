package com.company.project.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.Constant.Price;
import com.company.project.configurer.HttpsClientRequestFactory;
import com.company.project.core.Result;
import com.company.project.model.Hero;
import com.company.project.model.HeroInfo;
import com.github.pagehelper.Constant;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class BnxAndGoldPriceFindScheduel {
    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0/20 * * * * *")
    @PostConstruct
    private void cronScheduleFindHero(){



        String urlBnx = "https://api.coinmarketcap.com/data-api/v3/cryptocurrency/detail/chart?id=9891&range=1D&convertId=2787";
        String urlGold = "https://api.coinmarketcap.com/data-api/v3/cryptocurrency/detail/chart?id=12082&range=1D";

        String resultBnx = restTemplate.getForObject(urlBnx, String.class);
        String resultGold = restTemplate.getForObject(urlGold, String.class);
        double bnxPrice = findPrice(resultBnx);
        double goldPrice = findPrice(resultGold);
        System.out.println("当前bnx币价"+bnxPrice);
        System.out.println("当前gold币价"+goldPrice);
        Price.setBnxPrice(bnxPrice);
        Price.setGoldPrice(goldPrice);

    }

    private double findPrice(String str) {
        JSONObject obj = JSON.parseObject(str);
        return Double.parseDouble(JSON.parseObject(obj.getJSONObject("data").getJSONObject("points").values().toArray()[0].toString()).getJSONArray("v").get(0).toString());
    }
}
