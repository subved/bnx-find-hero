package com.company.project.cache;

import com.company.project.model.HeroInfo;

import java.util.ArrayList;
import java.util.List;

public class HeroListCache {

    private static List<HeroInfo> heroInfoList= new ArrayList();

    public static List<HeroInfo> getHeroInfoList() {
        return heroInfoList;
    }

    public static void setHeroInfoList(List<HeroInfo> heroInfoListTmp) {
        heroInfoList = heroInfoListTmp;
    }

    public static void reduceList() {
        heroInfoList.subList(0,100).clear();
    }

    public static void add(HeroInfo heroInfo) {
        if (heroInfo!=null&& !heroInfoList.contains(heroInfo)){
            heroInfoList.add(heroInfo);
        }

    }

}
