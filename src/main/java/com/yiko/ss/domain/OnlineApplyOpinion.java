package com.yiko.ss.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ss_onlineapply_opinion")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineApplyOpinion {
    @Id
    private String id;

    @Column(name = "onlineApplyid")
    private String onlineapplyid;

    private String opinion;

    @Column(name = "fristUser")
    private String fristuser;

    private Date createtime;

    private Date uptetime;

    @Column(name = "opinionName")
    private String opinionName;


}