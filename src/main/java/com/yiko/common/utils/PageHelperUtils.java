package com.yiko.common.utils;

import org.apache.fop.fo.flow.PageNumber;

import java.util.Map;

public class PageHelperUtils {
    private  int pageNumber ;
    private  int pageSize;
    public void stringToNumber(Map<String, Object> params){
        pageNumber=Integer.parseInt(params.get("PageNumber").toString());
        pageSize =Integer.parseInt(params.get("pageSize").toString());
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
