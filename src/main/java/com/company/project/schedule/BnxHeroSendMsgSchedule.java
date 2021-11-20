package com.company.project.schedule;

import com.alibaba.fastjson.JSONObject;
import com.company.project.cache.HeroListCache;
import com.company.project.model.Hero;
import com.company.project.model.HeroInfo;
import com.company.project.service.HeroService;

import com.company.project.tools.TelegramSendTool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class BnxHeroSendMsgSchedule {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HeroService heroService;

    @Autowired
    private TelegramSendTool telegramSendTool;

    //@Scheduled(fixedDelay = 5000)
    @Scheduled(cron = "0 * * * * *")
    private void cronScheduleFindHero(){
        while (HeroListCache.getQueue().size()>0){
            HeroInfo heroInfo = HeroListCache.getQueue().poll();
            if (heroService.selectCount(heroInfo) ==0){
                heroService.save(heroInfo);
                telegramSendTool.sendTelegram(heroInfo);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
