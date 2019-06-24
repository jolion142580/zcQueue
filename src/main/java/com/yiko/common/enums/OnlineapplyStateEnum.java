package com.yiko.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public enum OnlineapplyStateEnum {
    YI_SHEN_HE("已审核"),
    YU_SHEN_BU_TONG_GUO("预审不通过"),
    DAI_SHEN_HE("待审核"),
    YU_SHEN_ZHONG("预审中"),
    DAI_ZI_LIAO_SHANG_CHUAN("待资料上传"),
    YU_SHEN_TONG_GUO("预审通过"),
    //pc端提交事项默认状态
    STATE("STATE"),

    //审核通过
    APPROVED("1"),
    //审核不通过
    NOAPPROVED("0"),
    ;


    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
