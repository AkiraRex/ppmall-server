package com.ppmall.pojo;

import java.util.Date;

public class Advert {
    private Integer id;

    private Integer position;

    private String detail;

    private String image;

    private String link;

    private Integer type;

    private Integer status;

    private Integer productId;

    private Integer activityId;

    private Date beginTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    public Advert(Integer id, Integer position, String detail, String image, String link, Integer type, Integer status, Integer productId, Integer activityId, Date beginTime, Date endTime, Date createTime, Date updateTime) {
        this.id = id;
        this.position = position;
        this.detail = detail;
        this.image = image;
        this.link = link;
        this.type = type;
        this.status = status;
        this.productId = productId;
        this.activityId = activityId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Advert() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getdetail() {
        return detail;
    }

    public void setdetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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