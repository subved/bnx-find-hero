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
public class BnxAndGoldPriceFindScheduel {
    private final static Logger logger = LoggerFactory.getLogger(BnxHeroFindSchedule.class);
    @Autowired
    private RestTemplate restTemplate;

    //启动时和每30s执行一次
    //@Scheduled(cron = "0/30 * * * * *")
    //@PostConstruct
    private void cronScheduleFindHero(){



        String urlBnx = "https://api.coinmarketcap.com/data-api/v3/cryptocurrency/detail/chart?id=9891&range=1D&convertId=2787";
        String urlGold = "https://api.coinmarketcap.com/data-api/v3/cryptocurrency/detail/chart?id=12082&range=1D";

        String resultBnx = restTemplate.getForObject(urlBnx, String.class);
        String resultGold = restTemplate.getForObject(urlGold, String.class);
        double bnxPrice = findPrice(resultBnx);
        double goldPrice = findPrice(resultGold);
        logger.info("当前bnx币价"+bnxPrice);
        logger.info("当前gold币价"+goldPrice);
        Price.setBnxPrice(bnxPrice);
        Price.setGoldPrice(goldPrice);

    }

    private double findPrice(String str) {
        JSONObject obj = JSON.parseObject(str);
        return Double.parseDouble(JSON.parseObject(obj.getJSONObject("data").getJSONObject("points").values().toArray()[0].toString()).getJSONArray("v").get(0).toString());
    }
}
