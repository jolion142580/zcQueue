package com.yiko.converter;

import com.yiko.common.utils.StringUtils;
import com.yiko.ss.domain.UserInfo;
import com.yiko.ss.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDO2UserInfoVO {
    public static UserInfoVO convert(UserInfo userInfo) {
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }

    public static List<UserInfoVO> convert(List<UserInfo> userInfoList) {
        return userInfoList.stream().map(e -> convert(e)).collect(Collectors.toList());
    }

    public static UserInfoVO convertAndUpdate(UserInfo userInfo) {

        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        String phone = userInfoVO.getPhone();
        String idCard = userInfoVO.getIdcard();
        char[] phones = new char[0];
        char[] idCards = new char[0];
        if (StringUtils.isNotBlank(phone)) {
            if (phone.length() == 11) {
                phones = phone.toCharArray();
                for (int i = 5; i < 9; i++) {
                    phones[i] = 'x';
                }
            }
        }
        if (StringUtils.isNotBlank(idCard)) {
            if (idCard.length() > 16) {
                idCards = idCard.toCharArray();
                for (int i = 7; i < 13; i++) {
                    idCards[i] = 'x';
                }
            }
        }
        userInfoVO.setPhone(String.valueOf(phones));
        userInfoVO.setIdcard(String.valueOf(idCards));

        return userInfoVO;
    }

    public static List<UserInfoVO> convertAndUpdate(List<UserInfo> userInfoList) {
        return userInfoList.stream().map(e -> convertAndUpdate(e)).collect(Collectors.toList());
    }


}
