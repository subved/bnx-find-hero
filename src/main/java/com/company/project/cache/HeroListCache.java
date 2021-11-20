package com.company.project.cache;

import com.company.project.model.HeroInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HeroListCache {

    private static List<HeroInfo> heroInfoList= new ArrayList();

    private static Queue<HeroInfo> queue = new LinkedList<HeroInfo>();

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

    public static Queue<HeroInfo> getQueue() {
        return queue;
    }

    public static void setQueue(Queue<HeroInfo> queue) {
        HeroListCache.queue = queue;
    }
}
