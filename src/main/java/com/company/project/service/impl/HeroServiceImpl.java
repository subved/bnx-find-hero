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
        //游侠 主属性力量,副属性敏捷,第三属性体质,第四属性意志
        if("0xF31913a9C8EFE7cE7F08A1c08757C166b572a937".equals(hero.getCareerAddress())){
            return analyseRanger(hero);
        }
        //法师 主属性智力,副属性精神,第三属性体质,第四属性敏捷
        if("0xC6dB06fF6e97a6Dc4304f7615CdD392a9cF13F44".equals(hero.getCareerAddress())){
            return analyseMage(hero);
        }
        //战士 主属性力量,副属性体质,第三属性敏捷,第四属性意志
        if("0x22F3E436dF132791140571FC985Eb17Ab1846494".equals(hero.getCareerAddress())){
            return analyseWarrior(hero);
        }
        //盗贼 主属性敏捷,副属性力量,第三属性体质,第四属性意志
        if("0xaF9A274c9668d68322B0dcD9043D79Cd1eBd41b3".equals(hero.getCareerAddress())){
            return analyseThief(hero);
        }
        //德鲁伊 主属性智力
        if("0x1505Fc2ca150971bA8f254771359e50bCF26610f".equals(hero.getCareerAddress())){
            return analyseDruid(hero);
        }
        //牧师 主属性智力
        if("0x579A3e1C40124b6bcb3d244bb9a0A816aeD0c78D".equals(hero.getCareerAddress())){
            return analysePriest(hero);
        }
        //骑士 主属性体质
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

    private HeroInfo  analyseDruid(Hero hero){
        if(hero.getStrength()>85 && hero.getAgility()>=61){
            double wagesBase =  ((hero.getBrains()-85) * 0.0004 + 0.0008  )*28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
        return null;
    }

    private HeroInfo  analysePriest(Hero hero){
        if(hero.getStrength()>85 && hero.getAgility()>=61){
            double wagesBase =  ((hero.getBrains()-85) * 0.0004 + 0.0008 )*28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
        return null;
    }

    private HeroInfo  analyseKnight(Hero hero){
        if(hero.getStrength()>85 && hero.getAgility()>=61){
            double wagesBase = ((hero.getPhysique()-85) * 0.0004 + 0.0008 )*28800;
            return calculateCrystalInvestmentBack(hero,wagesBase);
        }
        return null;
    }

    private HeroInfo  calculateInvestmentBack (Hero hero,int wagesBase){
        HeroInfo heroInfo = new HeroInfo(hero);

        heroInfo.setBestBackDay(99999.999);
        int backLevel = 0;
        //假设升2级
        if(hero.getLevel()<2){
            //1升2
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
        //假设升3级
        if (hero.getLevel()<3){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            int wages =  wagesBase *2 *2 ;
            //1升3
            if(hero.getLevel() ==1){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 125000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getGoldPrice());
            }
            //2升3
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
        //假设升4级
        if (hero.getLevel()<4){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            int wages =  wagesBase *2 *2 *2;
            //1升4
            if(hero.getLevel() ==1){
                //3级
                costDollar =(new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 525000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            //2升4
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 500000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            //3升4
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
        //假设升5级
        if (hero.getLevel()<5){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            int wages =  wagesBase *2 *2 *2 *2;
            //1升5
            if(hero.getLevel() ==1){
                //3级
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1525000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getGoldPrice());
            }
            //2升5
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1500000 * Price.getGoldPrice());
                backDayTmp = costDollar /(wages * Price.getGoldPrice());
            }
            //3升5
            if(hero.getLevel() ==3){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1400000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getGoldPrice());
            }
            //4升5
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
        //假设升2级
        if(hero.getLevel()<2){
            //1升2
            double wages =  0.0008 * 28800 * 2  ;
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
        //假设升3级
        if (hero.getLevel()<3){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            double wages =  wagesBase *2 *2 ;
            //1升3
            if(hero.getLevel() ==1){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 125000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getCrystalPrice());
            }
            //2升3
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
        //假设升4级
        if (hero.getLevel()<4){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            double wages =  wagesBase *2 *2 *2;
            //1升4
            if(hero.getLevel() ==1){
                //3级
                costDollar =(new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 525000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            //2升4
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 500000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            //3升4
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
        //假设升5级
        if (hero.getLevel()<5){
            double backDayTmp= 999999.0;
            double costDollar = 0;
            double wages =  wagesBase *2 *2 *2 *2;
            //1升5
            if(hero.getLevel() ==1){
                //3级
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1525000 * Price.getGoldPrice());
                backDayTmp = costDollar / (wages * Price.getCrystalPrice());
            }
            //2升5
            if(hero.getLevel() ==2){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1500000 * Price.getGoldPrice());
                backDayTmp = costDollar /(wages * Price.getCrystalPrice());
            }
            //3升5
            if(hero.getLevel() ==3){
                costDollar = (new BigDecimal(hero.getPrice()).divide(new BigDecimal(priceToBnxStr)).doubleValue() * Price.getBnxPrice() + 1400000 * Price.getGoldPrice());
                backDayTmp =  costDollar/ (wages * Price.getCrystalPrice());
            }
            //4升5
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
