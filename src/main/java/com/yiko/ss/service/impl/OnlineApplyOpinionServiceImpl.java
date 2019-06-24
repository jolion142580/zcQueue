package com.yiko.ss.service.impl;

import com.yiko.common.utils.KeyUtil;
import com.yiko.common.utils.R;
import com.yiko.common.utils.SendMsgUtil;
import com.yiko.common.utils.ShiroUtils;
import com.yiko.ss.dao.OnlineApplyDao;
import com.yiko.ss.dao.OnlineApplyOpinionMapper;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.domain.OnlineApplyOpinion;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.OnlineApplyOpinionService;
import com.yiko.system.domain.UserDO;
import com.yiko.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class OnlineApplyOpinionServiceImpl implements OnlineApplyOpinionService {
    @Autowired
    OnlineApplyOpinionMapper onlineApplyOpinionMapper;
    @Autowired
    OnlineApplyDao onlineApplyDao;
    @Autowired
    AffairsService affairsService;
    @Autowired
    UserService userService;

    @Transactional(propagation = Propagation.REQUIRED)
    public R addOpinion(OnlineApplyDO onlineApplyDO) {
//        if (StringUtils.isBlank(onlineApplyDO.getDepartopinion())) {
//            return R.error("请填写审核意见");
//        }
//        if (String.valueOf(ShiroUtils.getUserId()).equals(onlineApplyDO.getUserId())) {
//            return R.error("请选择下一个审核人员");
//        }
//        OnlineApplyDO selectByIdOnline = onlineApplyDao.selectByPrimaryKey(onlineApplyDO.getId());
//
//        OnlineApplyOpinion onlineApplyOpinion = onlineApply2OnlineApplyOpinion(onlineApplyDO);
//        OnlineApplyDO updateOnlineApplyDO = new OnlineApplyDO();
//        updateOnlineApplyDO.setId(onlineApplyDO.getId());
//        updateOnlineApplyDO.setUserId(onlineApplyDO.getUserId());
//
//        AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());
//        if (StringUtils.isBlank(onlineApplyDO.getUserId())) {
//            String msgTxt = String.format("您好，事项：%s已经审核完毕，请查收", affairsDO.getAffairName());
//            updateOnlineApplyDO.setState(OnlineapplyStateEnum.BU_MEN_YI_SHEN_HE.getMessage());
//            String res = sendMsg(Long.parseLong(selectByIdOnline.getFirstUserId()), msgTxt);
//            if (res.equals("false")) {
//                return R.error("短信通知失败");
//            }
//
//        } else {
//            String msgTxt = String.format("您好，您收到一份待审核事项，事项名为：%s", affairsDO.getAffairName() + "。");
//            String res = sendMsg(Long.parseLong(onlineApplyDO.getUserId()), msgTxt);
//            if (res.equals("false")) {
//                return R.error("短信通知失败");
//            }
//        }
//        int result = onlineApplyDao.updateByPrimaryKeySelective(updateOnlineApplyDO);
//        int opinionResult = onlineApplyOpinionMapper.insertSelective(onlineApplyOpinion);
//
//
//        if (result > 0 && opinionResult > 0) {
//            return R.ok();
//        }
//
//        return R.error("修改失败");
        return null;
    }

//    private OnlineApplyOpinion onlineApply2OnlineApplyOpinion(OnlineApplyDO onlineApplyDO) {
//        OnlineApplyOpinion onlineApplyOpinion = OnlineApplyOpinion.builder()
//                .id(KeyUtil.genUniqueKey()).onlineapplyid(onlineApplyDO.getId())
//                .opinion(onlineApplyDO.getDepartopinion()).fristuser(onlineApplyDO.getUserId())
//                .opinionName(ShiroUtils.getUser().getName())
//                .createtime(new Date()).uptetime(new Date()).build();
//        return onlineApplyOpinion;
//    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OnlineApplyOpinion> selectListByOnlineAppId(String onlineAppId) {
        Example example = new Example(OnlineApplyOpinion.class);
        example.orderBy("createtime").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("onlineapplyid", onlineAppId);
        return onlineApplyOpinionMapper.selectByExample(example);

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int countTotalByOnlineAppId(String onlineAppId) {
        Example example = new Example(OnlineApplyOpinion.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("onlineapplyid", onlineAppId);
        return onlineApplyOpinionMapper.selectCountByExample(example);
    }

    public String sendMsg(Long userId, String txt) {
        //短信通知下一位审核人员
        UserDO userDO = userService.get(userId);
        String phone = userDO.getMobile();
        return SendMsgUtil.send(txt, phone);

    }
}
