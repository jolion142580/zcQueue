package com.yiko.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "yiko")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootdoConfig {
    //上传路径
    private String uploadPath;
    //微信上传路径
    private String wechatfile;
    //一门式地址
    private String ymsPath;
    //templatefile上传路径
    private String templatefile;

    private String wxServerPath;

    private String uploadMateriasTemplate;

    private String pullDepart;

    private String pullAffairs;

    private String pullAffairGuide;

    private String pullAffairObject;

    private String pullAffairMaterials;

}
