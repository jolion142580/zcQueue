package com.yiko.ss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.StringUtils;
import com.yiko.ss.dao.ComplaintMapper;
import com.yiko.ss.domain.Complaint;
import com.yiko.ss.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Autowired
    ComplaintMapper complaintMapper;

    @Override
    public PageUtils queryList(Map<String, Object> params) {
        Query query = new Query(params);

        //查询是否已经回复的留言。c.complaint_Reply
        String reply = (String) params.get("reply");
        if (StringUtils.isNotBlank(reply)) {

            //replied ->已回复
            if (reply.equals("replied")) {
                //sql语句
                params.put("reply", "and c.complaint_Reply is not null and c.complaint_Reply != ''");
            }
            //noreply ->未回复
            else if (reply.equals("noreply")) {
                params.put("reply", "and c.complaint_Reply is null ");
            }
        }
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<Complaint> complaintList = complaintMapper.list(params);
        PageInfo<Complaint> pageInfo = new PageInfo<>(complaintList);
        return new PageUtils(complaintList, new Long(pageInfo.getTotal()).intValue());
    }

    @Override
    public Complaint selectById(String id) {

        return complaintMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Complaint complaint) {
        return complaintMapper.updateByPrimaryKeySelective(complaint);
    }

    @Override
    public int remove(String cId) {
        return complaintMapper.deleteByPrimaryKey(cId);
    }

    @Override
    public int batchRemove(String[] cId) {
        return complaintMapper.batchRemove(cId);
    }
}
