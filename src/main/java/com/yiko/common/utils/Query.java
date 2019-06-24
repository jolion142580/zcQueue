package com.yiko.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//
	/*private int offset;
	// 每页条数
	private int limit;*/

    /**
     *
     * @param params
     */
    private int pageNumber;
    private int pageSize;

	public 	Query(Map<String, Object> params) {
		this.putAll(params);
        if(params.get("pageNumber")!=null &&params.get("pageSize")!=null) {
            this.pageNumber = Integer.parseInt(params.get("pageNumber").toString());
            this.pageSize = Integer.parseInt(params.get("pageSize").toString());
        }


	}

	/*public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}*/

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
