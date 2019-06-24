package com.yiko.common.task.pullBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 获取部门 的 bean
 * 所对应表 ss_base_dic
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullDepart {
    //部门id
    private String id;

    //部门名称
    private String depart_name;
}
