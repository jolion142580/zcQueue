package com.yiko.common.task.pullBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullAffairMaterials {
    private String ID;
    //事项ID
    private String AFFAIRID;

    private String EMPTYTABLEPATH;

    private String MATERIALCODE;

    private String VALIDDATE;
    //对象序号
    private String MATINDEX;
    //是否置顶
    private String ISTOP;
    //是否必须提交
    private String ISMUST;
    //材料名称
    private String MATNAME;
    //收件类型
    private String MATTYPE;
    //材料份数
    private String MATNUMBER;
    //是否需要提交
    private String REQUIRED;
    //复用详情
    private String ReuseDetail;
    //复用类型
    private String ReuseTypeID;
    //材料组合
    private String MATGROUP;
    //备注
    private String REMARKS;
    //样表路径
    private String EXAMPLEPATH;
    //状态
    private String VALID;
    //对应网上办事的表格
    private String TABLEID;
}
