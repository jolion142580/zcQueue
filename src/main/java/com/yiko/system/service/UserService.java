package com.yiko.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.User;
import com.yiko.system.vo.UserVO;
import com.yiko.common.domain.Tree;
import org.springframework.stereotype.Service;

import com.yiko.system.domain.DeptDO;
import com.yiko.system.domain.UserDO;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
	UserDO get(Long id);

	PageUtils list(Map<String, Object> map);



	int count(Map<String, Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(Long userId);

	int batchremove(Long[] userIds);

	boolean exit(Map<String, Object> params);

	Set<String> listRoles(Long userId);

	int resetPwd(UserVO userVO,UserDO userDO) throws Exception;

	int adminResetPwd(UserVO userVO) throws Exception;
	Tree<DeptDO> getTree();

	int updateFirstLogin(UserDO user);

	/**
	 * 更新个人信息
	 * @param userDO
	 * @return
	 */
	int updatePersonal(UserDO userDO);

	/**
	 * 更新个人图片
	 * @param file 图片
	 * @param avatar_data 裁剪信息
	 * @param userId 用户ID
	 * @throws Exception
	 */
    Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;

	public R sendShortMessage(String userName, String ipAddress);
}
