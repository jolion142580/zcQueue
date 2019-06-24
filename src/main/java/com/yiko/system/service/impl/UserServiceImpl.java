package com.yiko.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.config.Constant;
import com.yiko.common.domain.FileDO;
import com.yiko.common.domain.Tree;
import com.yiko.common.service.FileService;
import com.yiko.common.utils.*;
import com.yiko.converter.UserDO2CommonUserVO;
import com.yiko.system.dao.DeptDao;
import com.yiko.system.dao.ShortMessageMapper;
import com.yiko.system.dao.UserDao;
import com.yiko.system.dao.UserRoleDao;
import com.yiko.system.domain.DeptDO;
import com.yiko.system.domain.ShortMessage;
import com.yiko.system.domain.UserDO;
import com.yiko.system.domain.UserRoleDO;
import com.yiko.system.service.UserService;
import com.yiko.system.vo.CommonUserVO;
import com.yiko.system.vo.UserVO;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;
    @Autowired
    DeptDao deptMapper;
    @Autowired
    private FileService sysFileService;
    @Autowired
    private BootdoConfig bootdoConfig;

    @Autowired
    private ShortMessageMapper shortMessageMapper;

    @Override
    public UserDO get(Long id) {
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        UserDO user = userMapper.get(id);
        user.setDeptName(deptMapper.get(user.getDeptId()).getName());
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    public PageUtils list(Map<String, Object> map) {
        Query query = new Query(map);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<UserDO> sysUserList = userMapper.list(query);
        PageInfo<UserDO> pageInfo = new PageInfo<>(sysUserList);
        List<CommonUserVO> commonUserVOS = UserDO2CommonUserVO.convert(sysUserList);
        return new PageUtils(commonUserVOS, new Long(pageInfo.getTotal()).intValue());
    }

    @Override
    public int count(Map<String, Object> map) {
        return userMapper.count(map);
    }

    @Transactional
    @Override
    public int save(UserDO user) {
        int count = userMapper.save(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleMapper.batchSave(list);
        }
        return count;
    }

    @Override
    public int update(UserDO user) {
        int r = userMapper.update(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleMapper.batchSave(list);
        }
        return r;
    }

    @Override
    public int remove(Long userId) {
        userRoleMapper.removeByUserId(userId);
        return userMapper.remove(userId);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userMapper.list(params).size() > 0;
        return exit;
    }

    @Override
    public Set<String> listRoles(Long userId) {
        return null;
    }

    @Override
    public int resetPwd(UserVO userVO, UserDO userDO) throws Exception {
        String decOldPwd = AesEncryptUtils.decrypt(userVO.getPwdOld(), Constant.SERCURITY_LEY);
        String decNewPwd = AesEncryptUtils.decrypt(userVO.getPwdNew(), Constant.SERCURITY_LEY);
        userVO.setPwdOld(decOldPwd);
        userVO.setPwdNew(decNewPwd);

        if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
                userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
                return userMapper.update(userDO);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号！");
        }
    }

    @Override
    public int adminResetPwd(UserVO userVO) throws Exception {
        UserDO userDO = get(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
        return userMapper.update(userDO);


    }

    @Transactional
    @Override
    public int batchremove(Long[] userIds) {
        int count = userMapper.batchRemove(userIds);
        userRoleMapper.batchRemoveByUserId(userIds);
        return count;
    }

    @Override
    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
        List<DeptDO> depts = deptMapper.list(new HashMap<String, Object>(16));
        Long[] pDepts = deptMapper.listParentDept();
        Long[] uDepts = userMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
        for (DeptDO dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<UserDO> users = userMapper.list(new HashMap<String, Object>(16));
        for (UserDO user : users) {
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DeptDO> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public int updatePersonal(UserDO userDO) {
        return userMapper.update(userDO);
    }

    @Override
    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
        //获取图片后缀
        String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
        String[] str = avatar_data.split(",");
        //获取截取的x坐标
        int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
        //获取截取的y坐标
        int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
        //获取截取的高度
        int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
        //获取截取的宽度
        int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
        //获取旋转的角度
        int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
        try {
            BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
            BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(rotateImage, prefix, out);
            //转换后存入数据库
            byte[] b = out.toByteArray();
            FileUtil.uploadFile(b, bootdoConfig.getUploadPath(), fileName);
        } catch (Exception e) {
            throw new Exception("图片裁剪错误！！");
        }
        Map<String, Object> result = new HashMap<>();
        if (sysFileService.save(sysFile) > 0) {
            UserDO userDO = new UserDO();
            userDO.setUserId(userId);
            userDO.setPicId(sysFile.getId());
            if (userMapper.update(userDO) > 0) {
                result.put("url", sysFile.getUrl());
            }
        }
        return result;
    }

    @Override
    public int updateFirstLogin(UserDO user) {
        int result = userMapper.update(user);
        return result;
    }

    @Override
    public R sendShortMessage(String userName, String ipAddress) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strToday = format.format(today);
        int result = shortMessageMapper.sameDayCount(ipAddress);
        if (result > 5) {
            return R.error("当天发送短信次数过多");
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", userName);
        UserDO userDO = userMapper.list(userMap).get(0);
        if (null == userDO) {
            return R.error("用户名错误");
        }
        String phone = userDO.getMobile();
        Long userId = userDO.getUserId();

        if (StringUtils.isNotBlank(phone)) {

            int codeLength = 6;
            String smscode = "";
            for (int i = 0; i < codeLength; i++) {
                smscode += (int) (Math.random() * 9);
            }
            String txt = "欢迎使用张槎街道行政服务中心微信平台!帐号认证验证码：" + smscode + ",1分钟内有效。";
            String r = SendMsgUtil.send(txt, phone);
            if (r.equals("true")) {
                String shortMessageId = KeyUtil.genUniqueKey();
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String stDate = formatter.format(currentTime);
                ShortMessage shortMessage = ShortMessage.builder().id(shortMessageId).
                        userId("" + userId).messageCode(smscode).createTime(stDate).ipAddress(ipAddress).build();
                shortMessageMapper.insertSelective(shortMessage);
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("smscode", smscode);
                return R.ok(resultMap);

            }

        }
        return R.error("发送失败");

    }


}
