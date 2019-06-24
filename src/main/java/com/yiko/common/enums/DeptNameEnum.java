package com.yiko.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum DeptNameEnum {
    ZHANG_CHA_JIE_DAO("张槎街道行政服务中心"),;
    private String deptName;


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
