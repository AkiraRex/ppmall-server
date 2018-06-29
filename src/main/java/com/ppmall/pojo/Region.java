package com.ppmall.pojo;

public class Region {
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer type;

    private Integer agencyId;

    public Region(Integer id, Integer parentId, String name, Integer type, Integer agencyId) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.agencyId = agencyId;
    }

    public Region() {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }
}