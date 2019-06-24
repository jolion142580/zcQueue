package com.yiko.common.task.pullBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步事项的bean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullAffairs {
    private String id;

    //事项编号
    private String code;

    //事项名
    private String affairname;
    //部门id
    private String departid;
    //分类
    private String sortid;
    //层级，1	其他 2区（县） 9村（居）10镇（街）
    private String level;
    /**
     * 是否网上可办  1 可办
     */
    private String isnet;

    /**
     * result_form：结果形式
     * 1：直接转帐到指定帐户
     * 2：邮政速递实体证件
     * 3：可查询电子结果或邮寄纸质
     * 4：信息通知
     */
    private String result_form;

    //是否即办(1 即办 2 流转)
    private String istodo;

    //办事时限
    private String timelimit;

    //时限类型
    private String timelimit_type;


}
