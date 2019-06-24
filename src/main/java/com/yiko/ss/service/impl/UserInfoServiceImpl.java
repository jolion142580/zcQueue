package com.yiko.ss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.config.Constant;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.ShiroUtils;
import com.yiko.converter.UserInfoDO2UserInfoVO;
import com.yiko.ss.dao.UserInfoMapper;
import com.yiko.ss.domain.UserInfo;
import com.yiko.ss.service.UserInfoService;
import com.yiko.ss.vo.UserInfoVO;
import com.yiko.system.dao.UserRoleDao;
import com.yiko.system.domain.UserDO;
import com.yiko.system.domain.UserRoleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserRoleDao userRoleDao;

    @Override
    public PageUtils list(Map<String, Object> map) {

        Query query = new Query(map);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<UserInfo> userInfoList = userInfoMapper.selectList(map);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoList);

        if (hasRole()) {
            return new PageUtils(userInfoList, new Long(pageInfo.getTotal()).intValue());
        }
        List<UserInfoVO> userInfoVOList = UserInfoDO2UserInfoVO.convertAndUpdate(userInfoList);
        return new PageUtils(userInfoVOList, new Long(pageInfo.getTotal()).intValue());
    }

    public boolean hasRole() {
        UserDO userDO = ShiroUtils.getUser();
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("userId", userDO.getUserId());
        roleMap.put("roleId", Constant.hasRole.ADMIN);
        List<UserRoleDO> userRoleDOS = userRoleDao.list(roleMap);
        if (null != userRoleDOS && userRoleDOS.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public UserInfo getById(Map<String, Object> map) {
        return userInfoMapper.getById(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateById(UserInfo userInfo) {
        return userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int remove(String id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchRemove(String[] id) {
        return userInfoMapper.batchRemove(id);
    }

    @Override
    public List<UserInfo> queryUserByCreateDate(String startTime, String endTime) {
        return userInfoMapper.queryUserByCreateDate(startTime, endTime);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserInfo selectByPrimaryKey(String id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }
}
