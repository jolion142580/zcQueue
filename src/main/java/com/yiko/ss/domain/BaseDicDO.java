package com.yiko.ss.domain;

import javax.persistence.Transient;

public class BaseDicDO {
    private String id;

    private String baseDicId;

    private String baseDicType;

    private String cName;

    private String iconPath;

    private String valid;


    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Transient
    //判断是否选中该部门
    private String isCheck;

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseDicId() {
        return baseDicId;
    }

    public void setBaseDicId(String baseDicId) {
        this.baseDicId = baseDicId;
    }

    public String getBaseDicType() {
        return baseDicType;
    }

    public void setBaseDicType(String baseDicType) {
        this.baseDicType = baseDicType;
    }


    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Override
    public String toString() {
        return "BaseDicDO{" +
                "id='" + id + '\'' +
                ", baseDicId='" + baseDicId + '\'' +
                ", baseDicType='" + baseDicType + '\'' +
                ", cName='" + cName + '\'' +
                ", iconPath='" + iconPath + '\'' +
                '}';
    }


}