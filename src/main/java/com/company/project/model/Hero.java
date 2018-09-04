package com.company.project.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

public class Hero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    private String name;

    private String price;

    private String buyer;

    private String seller;

    @Column(name = "pay_addr")
    private String payAddr;

    @Column(name = "career_address")
    private String careerAddress;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "block_number")
    private Integer blockNumber;

    private Integer strength;

    private Integer agility;

    private Integer physique;

    private Integer volition;

    private Integer brains;

    private Integer charm;

    private Integer total;

    private Integer level;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return buyer
     */
    public String getBuyer() {
        return buyer;
    }

    /**
     * @param buyer
     */
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    /**
     * @return seller
     */
    public String getSeller() {
        return seller;
    }

    /**
     * @param seller
     */
    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * @return pay_addr
     */
    public String getPayAddr() {
        return payAddr;
    }

    /**
     * @param payAddr
     */
    public void setPayAddr(String payAddr) {
        this.payAddr = payAddr;
    }

    /**
     * @return career_address
     */
    public String getCareerAddress() {
        return careerAddress;
    }

    /**
     * @param careerAddress
     */
    public void setCareerAddress(String careerAddress) {
        this.careerAddress = careerAddress;
    }

    /**
     * @return token_id
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * @param tokenId
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * @return block_number
     */
    public Integer getBlockNumber() {
        return blockNumber;
    }

    /**
     * @param blockNumber
     */
    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * @return strength
     */
    public Integer getStrength() {
        return strength;
    }

    /**
     * @param strength
     */
    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    /**
     * @return agility
     */
    public Integer getAgility() {
        return agility;
    }

    /**
     * @param agility
     */
    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    /**
     * @return physique
     */
    public Integer getPhysique() {
        return physique;
    }

    /**
     * @param physique
     */
    public void setPhysique(Integer physique) {
        this.physique = physique;
    }

    /**
     * @return volition
     */
    public Integer getVolition() {
        return volition;
    }

    /**
     * @param volition
     */
    public void setVolition(Integer volition) {
        this.volition = volition;
    }

    /**
     * @return brains
     */
    public Integer getBrains() {
        return brains;
    }

    /**
     * @param brains
     */
    public void setBrains(Integer brains) {
        this.brains = brains;
    }

    /**
     * @return charm
     */
    public Integer getCharm() {
        return charm;
    }

    /**
     * @param charm
     */
    public void setCharm(Integer charm) {
        this.charm = charm;
    }

    /**
     * @return total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return Objects.equals(id, hero.id) && orderId.equals(hero.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId);
    }
}