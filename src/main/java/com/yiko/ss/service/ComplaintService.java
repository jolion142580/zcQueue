package com.yiko.ss.service;

import com.yiko.common.utils.PageUtils;
import com.yiko.ss.domain.Complaint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ComplaintService {
    public PageUtils queryList(Map<String, Object> map);

    public Complaint selectById(String id);

    int update(Complaint complaint);

    int remove(String cId);

    int batchRemove(String []cId);
}
