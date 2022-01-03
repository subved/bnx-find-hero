package com.company.project.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.cache.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;


@Component
public class CrystalPriceFindScheduel {
    private final static Logger logger = LoggerFactory.getLogger(BnxHeroFindSchedule.class);
    @Autowired
    private RestTemplate restTemplate;

    //启动时和每30s执行一次
    @Scheduled(cron = "0/30 * * * * *")
    @PostConstruct
    private void cronScheduleFindHero(){



        String urlBnx = "https://bsc.streamingfast.io/subgraphs/name/pancakeswap/exchange-v2";
        JSONObject json = new JSONObject();
        json.put("query","\n      query derivedTokenPriceData {\n        \n   lastest:token(id:\"0x6ad7e691f1d2723523e70751f82052a8a2c47726\") { \n        derivedBNB\n      } \n}");
        String resultCrystal = restTemplate.postForObject(urlBnx,json, String.class);
        json.put("query","\n      query derivedTokenPriceData {\n        \n   lastest:token(id:\"0xe9e7cea3dedca5984780bafc599bd69add087d56\") { \n        derivedBNB\n      } \n}");
        String resultBusd = restTemplate.postForObject(urlBnx,json, String.class);
        double crystalBnbPrice =findPrice(resultCrystal);
        double busdBnbPrice =findPrice(resultBusd);
        double crystalBusdPrice = crystalBnbPrice / busdBnbPrice;
        logger.info("当前crystal币价"+crystalBusdPrice);
        Price.setCrystalPrice(crystalBusdPrice);
    }

    private double findPrice(String str) {
        JSONObject obj = JSON.parseObject(str);
        return Double.parseDouble(obj.getJSONObject("data").getJSONObject("lastest").getString("derivedBNB"));
    }
}
