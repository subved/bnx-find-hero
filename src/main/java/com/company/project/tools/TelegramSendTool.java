package com.company.project.tools;

import com.company.project.model.HeroInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Component
public class TelegramSendTool {

    @Autowired
    private RestTemplate restTemplate;



    public void sendTelegram(HeroInfo heroInfo){

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();

        map.add("chat_id", "-730219795");
        map.add("text",parseString(heroInfo));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
        String resultBnx = restTemplate.postForObject("https://api.telegram.org/*******/sendMessage",requestEntity,String.class);
        //System.out.printf(resultBnx);
    }

    private String parseString(HeroInfo heroInfo){
        String stringTemplate =
                "购买地址:{0} \n" +
                "订单号:{1} \n" +
                "价格:{2} bnx \n" +
                "职业:{3} \n" +
                "等级:{4} \n" +
                "总属性:{5} \n" +
                "力量:{6} \n" +
                "敏捷:{7} \n" +
                "体质:{8} \n" +
                "意志:{9} \n" +
                "智力:{10} \n" +
                "精神:{11} \n" +
                "最佳回本天数:{12} \n" +
                "最佳回本等级:{13} \n" +
                "最佳日产金币:{14} \n" +
                "总花费:{15} 美刀\n" ;

        String context2 = MessageFormat.format(stringTemplate,
                "https://market.binaryx.pro/#/oneoffsale/detail/"+ heroInfo.getOrderId(),
                heroInfo.getOrderId(),
                new BigDecimal(heroInfo.getPrice()).divide(new BigDecimal("1000000000000000000")).toString(),
                CareerParseTool.careerParse(heroInfo.getCareerAddress()),
                heroInfo.getLevel(),
                heroInfo.getTotal(),
                heroInfo.getStrength(),
                heroInfo.getAgility(),
                heroInfo.getPhysique(),
                heroInfo.getVolition(),
                heroInfo.getBrains(),
                heroInfo.getCharm(),
                heroInfo.getBestBackDay(),
                heroInfo.getBestBackLevel(),
                heroInfo.getBestWages(),
                heroInfo.getCostDollar());
        System.out.println(context2);
        return  context2;

    }
}
