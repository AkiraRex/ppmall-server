package com.ppmall.vo;

import java.math.BigDecimal;

import com.ppmall.pojo.Product;
import com.ppmall.util.DateUtil;

public class ProductVo {
	private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String createTime;

    private String updateTime;
    
    private String imageHost;

    public ProductVo(Integer id, Integer categoryId, String name, String subtitle, String mainImage, String subImages, String detail, BigDecimal price, Integer stock, Integer status, String createTime, String updateTime,String imageHost) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.subtitle = subtitle;
        this.mainImage = mainImage;
        this.subImages = subImages;
        this.detail = detail;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.imageHost = imageHost;
    }

    public ProductVo(Product product) {
    	this.id = product.getId();
        this.categoryId = product.getCategoryId();
        this.name = product.getName();
        this.subtitle = product.getSubtitle();
        this.mainImage = product.getMainImage();
        this.subImages = product.getSubImages();
        this.detail = product.getDetail();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.createTime = DateUtil.getDateString(product.getCreateTime());
        this.updateTime = DateUtil.getDateString(product.getUpdateTime());
    }
    
    public ProductVo() {
		// TODO Auto-generated constructor stub
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages == null ? null : subImages.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
