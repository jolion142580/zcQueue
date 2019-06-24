package com.yiko.common.task.pullBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
  同步事项对象所需bean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullAffairObject {
    //对象ID
    private String OBJID;
    //对象名称
    private String OBJNAME;
    //对象索引
    private String OBJINDEX;
}
