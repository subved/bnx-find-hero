package com.company.project.service;
import com.company.project.model.Hero;
import com.company.project.core.Service;
import com.company.project.model.HeroInfo;

import java.util.List;


/**
 * Created by CodeGenerator on 2021/11/18.
 */
public interface HeroService extends Service<Hero> {

    void analyseList(List<Hero> heroList);

    HeroInfo analyse(Hero hero);

    List<HeroInfo> findHeroList();

    public int selectCount(Hero hero);
}
