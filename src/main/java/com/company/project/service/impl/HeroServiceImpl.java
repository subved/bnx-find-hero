package com.company.project.service.impl;

import com.company.project.cache.HeroListCache;
import com.company.project.cache.Price;
import com.company.project.dao.HeroMapper;
import com.company.project.model.Hero;
import com.company.project.model.HeroInfo;
import com.company.project.service.HeroService;
import com.company.project.core.AbstractService;
import com.company.project.tools.TelegramSendTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;



/**
 * Created by CodeGenerator on 2021/11/18.
 */
@Service
@Transactional
public class HeroServiceImpl extends AbstractService<Hero> implements HeroService {
    private final static Logger logger = LoggerFactory.getLogger(HeroServiceImpl.class);

    @Resource
    private HeroMapper heroMapper;

    private final long priceToBnx = 1000000000000000000L;
    private final String priceToBnxStr = "1000000000000000000";

    @Autowired
    TelegramSendTool telegramSendTool;

    @Override
    public void analyseList(List<Hero> heroList) {
        if(HeroListCache.getHeroInfoList().size()>200){
            HeroListCache.reduceList();
        }
        for (Hero hero : heroList){
            HeroInfo heroInfo = analyseHeroPrice(hero);
            if (heroInfo !=null &&heroInfo.getBestBackDay()<50.0){
                HeroListCache.add(heroInfo);
                HeroListCache.getQueue().add(heroInfo);
            }
        }
        logger.info("analyseList success");
    }

    @Override
    public HeroInfo analyse(Hero hero) {
        return analyseHeroPrice(hero);
    }

    @Override
    public List<HeroInfo> findHeroList() {
        List<HeroInfo> heroList = HeroListCache.getHeroInfoList();
        heroList.sort(Comparator.comparing(HeroInfo::getBestBackDay));
        return heroList.subList(0,10);
    }

    @Override
    public int selectCount(Hero hero) {
        return heroMapper.selectCount(hero);
    }


    private HeroInfo analyseHeroPrice(Hero hero){
        //?????? ???????????????,???????????????,??????????????????,??????????????????
        if("0xF31913a9C8EFE7cE7F08A1c08757C166b572a937".equals(hero.getCareerAddress())){
            return analyseRanger(hero);
        }
        //?????? ???????????????,???????????????,??????????????????,??????????????????
        if("0xC6dB06fF6e97a6Dc4304f7615CdD392a9cF13F44".equals(hero.getCareerAddress())){
            return analyseMage(hero);
        }
        //?????? ???????????????,???????????????,??????????????????,??????????????????
        if("0x22F3E436dF132791140571FC985Eb17Ab1846494".equals(hero.getCareerAddress())){
            return analyseWarrior(hero);
        }
        //?????? ???????????????,???????????????,??????????????????,??????????????????
        if("0xaF9A274c9668d68322B0dcD9043D79Cd1eBd41b3".equals(hero.getCareerAddress())){
            return analyseThief(hero);
        }
        //????????? ???????????????
        if("0x1505Fc2ca150971bA8f254771359e50bCF26610f".equals(hero.getCareerAddress())){
            return analyseDruid(hero);
        }
        //?????? ???????????????
        if("0x579A3e1C40124b6bcb3d244bb9a0A816aeD0c78D".equals(hero.getCareerAddress())){
            return analysePriest(hero);
        }
        //?????? ???????????????
        if("0x7773bd9b39989F9610B17Ca5972546BD475b8FAa".equals(hero.getCareerAddress())){
            return analyseKnight(hero);
        }
        return null;
    }

    private HeroInfo  analyseWarrior(Hero hero){
        if(hero.getStrength()>85 && hero.getPhysique()>=61){
            int wagesBase = (int) (((hero.getStrength()-85) * 0.5 + 1 )*288);
            return calculateInvestmentBack(hero,wagesBase);
        }
        return  null;
    }

    private HeroInfo  analyseThief(Hero hero){
        if(hero.getAgility()>85 && hero.getStrength()>=61){
            int wagesBase = (int) (((hero.getAgility()-85) * 0.5 + 1 )*288);
            return calculateInvestmentBack(hero,wagesBase);
        }
        return null;
    }

    private HeroInfo  analyseMage(Hero hero){
        if(hero.getBrains()>85 && hero.getCharm()>=61) {
            int wagesBase = (int) (((hero.getBrains() - 85) * 0.5 + 1) * 288);
            return calculateInvestmentBack(hero, wagesBase);
        }
        return null;
    }

    private HeroInfo  analyseRanger(Hero hero){
        if(hero.getStrength()>85 && hero.getAgility()>=61){
            int wagesBase = (int) (((hero.getStrength()-85) * 0.5 + 1 )*288);
            return calculateInvestmentBack(hero,wagesBase);
        }
        return null;
    }
    //????????? ?????????
    private HeroInfo  analyseDruid(Hero hero){
        if(hero.getBrains()>85 && hero.getPhysique()>=61){
            double wagesBase =  ((hero.getBrains()-85) * 0.0004 + 0.0008  )*28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
        //??????
        else {
            double wagesBase =  0.0008 *28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
    }
    //????????? ?????????
    private HeroInfo  analysePriest(Hero hero){
        if(hero.getBrains()>85 && hero.getVolition()>=61){
            double wagesBase =  ((hero.getBrains()-85) * 0.0004 + 0.0008 )*28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
        //??????
        else {
            double wagesBase =  0.0008 *28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
    }
    //????????? ?????????
    private HeroInfo  analyseKnight(Hero hero){
        if(hero.getPhysique()>85 && hero.getStrength()>=61){
            double wagesBase = ((hero.getPhysique()-85) * 0.0004 + 0.0008 )*28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
       //??????
        else {
            double wagesBase =  0.0008 *28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
    }

    private HeroInfo  calculateInvestmentBack (Hero hero,int wagesBase){
        HeroInfo heroInfo = new HeroInfo(hero);

        heroInfo.setBestBackDay(99999.999);
        int backLevel = 0;
        //?????????2???  ??????????????????
        if(hero.getLevel()<2){
            //1???2
            int wages =  288 * 2  ;
            double costDollar =  (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue()
                    * Price.getBnxPrice() + 25000 * Price.getGoldPrice());
            double backDayTmp = costDollar / (wages * Price.getGoldPrice());

            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(2);
                heroInfo.setBestWages(wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        //?????????3???
        if (hero.getLevel()<3){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            int wages =  wagesBase *2 *2 ;
            //1???3
            if(hero.getLevel() ==1){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 125000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getGoldPrice());
            }
            //2???3
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 100000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(3);
                heroInfo.setBestWages(wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        //?????????4???
        if (hero.getLevel()<4){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            int wages =  wagesBase *2 *2 *2;
            //1???4
            if(hero.getLevel() ==1){
                //3???
                costDollar =(new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 525000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            //2???4
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 500000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            //3???4
            if(hero.getLevel() ==3){
                costDollar =( new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 400000 * Price.getGoldPrice());
                backDayTmp = costDollar /(wages * Price.getGoldPrice());
            }
            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(4);
                heroInfo.setBestWages(wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        //?????????5???
        if (hero.getLevel()<5){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            int wages =  wagesBase *2 *2 *2 *2;
            //1???5
            if(hero.getLevel() ==1){
                //3???
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1525000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getGoldPrice());
            }
            //2???5
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1500000 * Price.getGoldPrice());
                backDayTmp = costDollar /(wages * Price.getGoldPrice());
            }
            //3???5
            if(hero.getLevel() ==3){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1400000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            //4???5
            if(hero.getLevel() ==4){
                costDollar =  (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1000000 * Price.getGoldPrice());
                backDayTmp = costDollar/ (wages * Price.getGoldPrice());
            }
            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(5);
                heroInfo.setBestWages(wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        return heroInfo;


    }


    private HeroInfo  calculateCrystalInvestmentBack (Hero hero,double wagesBase){
        HeroInfo heroInfo = new HeroInfo(hero);

        heroInfo.setBestBackDay(99999.999);
        int backLevel = 0;
        //?????????2??? ??????????????????
        if(hero.getLevel()<2){
            //1???2
            double wages =  0.0008 * 28800 * 1.1  ;
            double costDollar =  (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue()
                    * Price.getBnxPrice() + 25000 * Price.getGoldPrice());
            double backDayTmp = costDollar / (wages * Price.getCrystalPrice());

            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(2);
                heroInfo.setBestWages((int)wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        //?????????3???
        if (hero.getLevel()<3){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            double wages =  wagesBase *1.3 ;
            //1???3
            if(hero.getLevel() ==1){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 125000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getCrystalPrice());
            }
            //2???3
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 100000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(3);
                heroInfo.setBestWages((int)wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        //?????????4???
        if (hero.getLevel()<4){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            double wages =  wagesBase *1.6;
            //1???4

            if(hero.getLevel() ==1){
                //3???
                costDollar =(new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 525000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            //2???4
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 500000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            //3???4
            if(hero.getLevel() ==3){
                costDollar =( new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 400000 * Price.getGoldPrice());
                backDayTmp = costDollar /(wages * Price.getCrystalPrice());
            }
            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(4);
                heroInfo.setBestWages((int)wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        //?????????5???
        if (hero.getLevel()<5){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            double wages =  wagesBase *2;
            //1???5
            if(hero.getLevel() ==1){
                //3???
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1525000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getCrystalPrice());
            }
            //2???5
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1500000 * Price.getGoldPrice());
                backDayTmp = costDollar /(wages * Price.getCrystalPrice());
            }
            //3???5
            if(hero.getLevel() ==3){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1400000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            //4???5
            if(hero.getLevel() ==4){
                costDollar =  (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1000000 * Price.getGoldPrice());
                backDayTmp = costDollar/ (wages * Price.getCrystalPrice());
            }
            if (backDayTmp < heroInfo.getBestBackDay()){
                heroInfo.setBestBackDay(backDayTmp);
                heroInfo.setBestBackLevel(5);
                heroInfo.setBestWages((int)wages);
                heroInfo.setCostDollar(costDollar);
            }
        }
        return heroInfo;


    }



}
