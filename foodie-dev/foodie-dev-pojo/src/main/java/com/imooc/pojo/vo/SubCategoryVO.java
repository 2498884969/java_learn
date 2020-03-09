package com.imooc.pojo.vo;

public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;

    public SubCategoryVO() {
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Integer getSubFatherId() {
        return subFatherId;
    }

    public void setSubFatherId(Integer subFatherId) {
        this.subFatherId = subFatherId;
    }

    public SubCategoryVO(Integer subId, String subName, String subType, Integer subFatherId) {
        this.subId = subId;
        this.subName = subName;
        this.subType = subType;
        this.subFatherId = subFatherId;
    }
}
