package com.yiko.common.enums;

public enum SSEnum {
    SAVE_ERROR(1, "保存失败"),
    FIND_ERROR(2,"数据不存在"),
    ObjectDeTAIL_ERROR(3,"事项对象不存在"),
    ;
    private int code;
    private String message;

    SSEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
