package com.yiko.converter;

import com.yiko.common.utils.StringUtils;
import com.yiko.system.domain.UserDO;
import com.yiko.system.vo.CommonUserVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserDO2CommonUserVO {
    public static CommonUserVO convert(UserDO UserDO) {
        CommonUserVO commonUserVO = new CommonUserVO();
        BeanUtils.copyProperties(UserDO, commonUserVO);
        return commonUserVO;
    }

    public static List<CommonUserVO> convert(List<UserDO> userDOS) {
        return userDOS.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }


    public static CommonUserVO convertAndUpdatePwd(UserDO UserDO) {
        CommonUserVO commonUserVO = new CommonUserVO();
        BeanUtils.copyProperties(UserDO, commonUserVO);
        String mobile = commonUserVO.getMobile();
        char[] chrs = new char[0];
        if (StringUtils.isNotBlank(mobile)) {
            chrs = mobile.toCharArray();
            if (chrs.length == 11) {
                for (int i = 5; i < 9; i++) {
                    chrs[i] = 'x';
                }
            }
        }
        commonUserVO.setMobile(String.valueOf(chrs));
        return commonUserVO;
    }

    public static List<CommonUserVO> convertAndUpdatePwd(List<UserDO> userDOS) {
        return userDOS.stream().map(e ->
                convertAndUpdatePwd(e)
        ).collect(Collectors.toList());
    }
}
