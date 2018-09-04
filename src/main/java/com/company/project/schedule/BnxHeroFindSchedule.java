package com.company.project.schedule;

import com.alibaba.fastjson.JSONObject;
import com.company.project.configurer.HttpsClientRequestFactory;
import com.company.project.core.Result;
import com.company.project.model.Hero;
import com.company.project.model.HeroInfo;
import com.company.project.service.HeroService;
import com.github.pagehelper.Constant;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class BnxHeroFindSchedule {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HeroService heroService;

    @Scheduled(cron = "0/20 * * * * *")
    private void cronScheduleFindHero(){
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        JSONObject jsonResutl = new JSONObject();
        try {
            driver.get("https://market.binaryx.pro/info/getSales?page=1&page_size=100&status=selling&name=&sort=time&direction=desc&career=&value_attr=&start_value=&end_value=&pay_addr=");
            String resultStr = driver.getPageSource().toString();
            System.out.println(resultStr);
            String resultStrSub = resultStr.substring(resultStr.indexOf('{'),resultStr.lastIndexOf('}')+1);
            System.out.println(resultStrSub);
            jsonResutl= JSONObject.parseObject(resultStrSub);
            heroService.analyseList(jsonResutl.getJSONObject("data").getJSONObject("result").getJSONArray("items").toJavaList(Hero.class));
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 关闭浏览器
            driver.quit();
        }

    }



}
