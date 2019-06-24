package com.yiko.ss.service;


import com.yiko.common.service.BaseService;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.vo.OnlineApplyCountVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OnlineApplyService extends BaseService<OnlineApplyDO> {

    List<OnlineApplyDO> list(Map<String, Object> map);

    int updateByCode(OnlineApplyDO onlineApplyDO);


    /**
     * 根据日期查询提交办事人数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<OnlineApplyDO> queryCommitAffairCount(String startTime, String endTime);

    /**
     * 前五办事数量
     *
     * @param startTime
     * @param endTime
     * @return
     */

    List<OnlineApplyCountVo> calculateDateTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);


    void affairsLimitDate();


    PageUtils selectPages(Map<String, Object> map);


    //根据网上办事id查询出办事人名称以及事项名称
    OnlineApplyDO queryUserNameAndAffairNameByOnlineId(String id);


    //查询编辑页面
    Map<String, Object> showEditDetail(String id);

    //修改办事
    R updateOnlineApply(OnlineApplyDO onlineApplyDO);

    int word2Pdf(String bookmarkjson, String tablejson, OnlineApplyDO onlineApplyDO, TemplateFile templateFile);


    //提交办事
    R submitAffair(String id, String[] fileIds) throws Exception;

    //一门式返回预审结果
    R pushResult(String result);

    R sendWXFailMsg(OnlineApplyDO onlineApplyDO);

    /**
     * @param isChecked checked 代表已审核
     *                  unChecked 未审核
     * @param params
     * @return
     *
     * 根据isChecked查询全部审核或者未审核事项
     */
    PageUtils OnlineAffairsIsChecked(String isChecked, Map<String, Object> params);


}
