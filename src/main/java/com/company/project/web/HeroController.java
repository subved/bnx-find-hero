package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Hero;
import com.company.project.model.HeroInfo;
import com.company.project.service.HeroService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2021/11/18.
 */
@RestController
@RequestMapping("/hero")
public class HeroController {
    @Resource
    private HeroService heroService;

    @PostMapping("/add")
    public Result add(Hero hero) {
        heroService.save(hero);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/analyse")
    public Result analyse(@RequestBody Hero hero) {
        HeroInfo heroInfo = heroService.analyse(hero);
        return ResultGenerator.genSuccessResult(heroInfo);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        heroService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Hero hero) {
        heroService.update(hero);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Hero hero = heroService.findById(id);
        return ResultGenerator.genSuccessResult(hero);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageHelper.startPage(page, size);
        List<HeroInfo> list = heroService.findHeroList();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
