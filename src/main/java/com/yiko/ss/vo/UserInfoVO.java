package com.yiko.ss.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoVO {
    private String id;

    private String address;

    private String idcard;

    private String name;

    private String phone;

}
