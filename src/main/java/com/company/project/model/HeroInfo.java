package com.company.project.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class HeroInfo extends  Hero{
   Double bestBackDay;

   Integer bestBacklevel;

   Integer bestWages;

   Double costDollar;

   public Double getBestBackDay() {
      return bestBackDay;
   }

   public void setBestBackDay(Double bestBackDay) {
      this.bestBackDay = bestBackDay;
   }

   public Integer getBestBacklevel() {
      return bestBacklevel;
   }

   public void setBestBacklevel(Integer bestBacklevel) {
      this.bestBacklevel = bestBacklevel;
   }

   public Integer getBestWages() {
      return bestWages;
   }

   public void setBestWages(Integer bestWages) {
      this.bestWages = bestWages;
   }

   public Double getCostDollar() {
      return costDollar;
   }

   public void setCostDollar(Double costDollar) {
      this.costDollar = costDollar;
   }

   public HeroInfo(Hero hero) {
      this.setId(hero.getId());
      this.setOrderId(hero.getOrderId());
      this.setName(hero.getName());
      this.setPrice(hero.getPrice());
      this.setBuyer(hero.getBuyer());
      this.setSeller(hero.getSeller());
      this.setPayAddr(hero.getPayAddr());
      this.setCareerAddress(hero.getCareerAddress());
      this.setTokenId(hero.getTokenId());
      this.setBlockNumber(hero.getBlockNumber());
      this.setStrength(hero.getStrength());
      this.setAgility(hero.getAgility());
      this.setPhysique(hero.getPhysique());
      this.setVolition(hero.getVolition());
      this.setBrains(hero.getBrains());
      this.setCharm(hero.getCharm());
      this.setTotal(hero.getTotal());
      this.setLevel(hero.getLevel());
      this.setCreateTime(hero.getCreateTime());
      this.setUpdateTime(hero.getUpdateTime());
   }
}