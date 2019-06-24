package com.yiko.ss.domain;

public class TemplateFile {
    private String id;

    private String name;

    private String filename;

    private String localpath;

    private String remark;

    private String createtime;

    private String departid;

    private String isCheck;

    private String templateId1IsChecked;

    public String getTemplateId1IsChecked() {
        return templateId1IsChecked;
    }

    public void setTemplateId1IsChecked(String templateId1IsChecked) {
        this.templateId1IsChecked = templateId1IsChecked;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getLocalpath() {
        return localpath;
    }

    public void setLocalpath(String localpath) {
        this.localpath = localpath == null ? null : localpath.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid == null ? null : departid.trim();
    }
}