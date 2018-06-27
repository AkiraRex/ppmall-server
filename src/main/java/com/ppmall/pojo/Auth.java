package com.ppmall.pojo;

import java.util.Date;

public class Auth {
    private Integer id;

    private String openId;

    private String nickName;

    private Integer gender;

    private String language;

    private String province;

    private String country;

    private String city;

    private String avatarUrl;

    private Date createTime;

    private Date updateTime;

    public Auth(Integer id, String openId, String nickName, Integer gender, String language, String province, String country, String city, String avatarUrl, Date createTime, Date updateTime) {
        this.id = id;
        this.openId = openId;
        this.nickName = nickName;
        this.gender = gender;
        this.language = language;
        this.province = province;
        this.country = country;
        this.city = city;
        this.avatarUrl = avatarUrl;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Auth() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}