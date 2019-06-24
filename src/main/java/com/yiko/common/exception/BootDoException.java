package com.yiko.common.exception;

import com.yiko.common.enums.SSEnum;

public class BootDoException extends RuntimeException {
    private Integer code;

    public BootDoException(SSEnum ssEnum) {
        super(ssEnum.getMessage());
        this.code = ssEnum.getCode();
    }

    public BootDoException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
