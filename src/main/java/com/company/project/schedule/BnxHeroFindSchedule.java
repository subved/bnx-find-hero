package com.company.project.schedule;

import com.alibaba.fastjson.JSONObject;
import com.company.project.model.Hero;
import com.company.project.service.HeroService;
import com.company.project.service.impl.HeroServiceImpl;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class BnxHeroFindSchedule {
    private final static Logger logger = LoggerFactory.getLogger(BnxHeroFindSchedule.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HeroService heroService;

    //@Scheduled(cron = "0 */2 * * * *")
    private void cronScheduleFindHero(){

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.addArguments("--disable-gpu");
//        options.setHeadless(true);
        WebDriver driver = new ChromeDriver();
        JSONObject jsonResutl = new JSONObject();
        try {
            driver.get("https://market.binaryx.pro/getSales?page=1&page_size=100&status=selling&name=&sort=time&direction=desc&career=&value_attr=&start_value=&end_value=&pay_addr=");
            String resultStr = driver.getPageSource().toString();
            String resultStrSub = resultStr.substring(resultStr.indexOf('{'),resultStr.lastIndexOf('}')+1);
            jsonResutl= JSONObject.parseObject(resultStrSub);
            heroService.analyseList(jsonResutl.getJSONObject("data").getJSONObject("result").getJSONArray("items").toJavaList(Hero.class));
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 关闭浏览器
            driver.quit();
        }

    }



}
