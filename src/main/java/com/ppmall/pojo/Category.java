package com.ppmall.pojo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Category implements Comparable,Serializable {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
    
    private String mainImage;


    public Category(Integer id, Integer parentId, String name, Boolean status, Integer sortOrder, Date createTime, Date updateTime, String mainImage) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.status = status;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.mainImage = mainImage;
    }

    public Category() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        if (this.getId() > ((Category) o).getId())
            return 1;
        return -1;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	 ObjectMapper mapper = new ObjectMapper();
         try {
             String returnString =  mapper.writeValueAsString(this);
             return returnString;
         } catch (IOException e) {
             e.printStackTrace();
         }
         return super.toString();
    }
}