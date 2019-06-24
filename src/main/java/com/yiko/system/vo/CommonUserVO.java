package com.yiko.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonUserVO implements Serializable {
    
    private static final long serialVersionUID = 8290165199882856111L;

    private Long userId;
    // 用户名
    private String username;
    // 用户真实姓名
    private String name;

    // 部门
    private Long deptId;

    private String deptName;
    // 邮箱
    private String email;
    // 手机号
    private String mobile;
    // 状态 0:禁用，1:正常
    private Integer status;

    //角色
    private List<Long> roleIds;
    //性别
    private Long sex;


}
