package com.yiko.system.service.impl;

import com.yiko.common.utils.Query;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.domain.Tree;
import com.yiko.common.utils.BuildTree;
import com.yiko.system.dao.DeptDao;
import com.yiko.system.dao.UserDao;
import com.yiko.system.domain.DeptDO;
import com.yiko.system.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yiko.system.service.DeptService;


@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao sysDeptMapper;

    @Autowired
    private UserDao userDao;

    @Override
    public DeptDO get(Long deptId) {
        return sysDeptMapper.get(deptId);
    }

    @Override
    public List<DeptDO> list(Map<String, Object> map) {
        if (map.get("offset") != null && map.get("limit") != null) {
            Query query = new Query(map);
            PageHelper.startPage(query.getPageNumber(), query.getPageSize());
            List<DeptDO> list = sysDeptMapper.list(map);
            PageInfo<DeptDO> pageInfo = new PageInfo<>(list);
            return list;
        }
        return sysDeptMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return sysDeptMapper.count(map);
    }

    @Override
    public int save(DeptDO sysDept) {
        return sysDeptMapper.save(sysDept);
    }

    @Override
    public int update(DeptDO sysDept) {
        return sysDeptMapper.update(sysDept);
    }

    @Override
    public int remove(Long deptId) {
        return sysDeptMapper.remove(deptId);
    }

    @Override
    public int batchRemove(Long[] deptIds) {
        return sysDeptMapper.batchRemove(deptIds);
    }

    @Override
    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
        List<DeptDO> sysDepts = sysDeptMapper.list(new HashMap<String, Object>(16));
        for (DeptDO sysDept : sysDepts) {
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(sysDept.getDeptId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setText(sysDept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DeptDO> t = BuildTree.build(trees);
        return t;
    }


    @Override
    public Tree<Object> getdeptAndUsertree() {
        List<Tree<Object>> trees = new ArrayList<Tree<Object>>();
        List<DeptDO> sysDepts = sysDeptMapper.list(new HashMap<String, Object>(16));

        Map<String, Object> userMap = new HashMap<>();
        for (DeptDO sysDept : sysDepts) {
            userMap.put("deptId", sysDept.getDeptId());
            List<UserDO> userDOList = userDao.list(userMap);

            List<Tree<Object>> childrenTreeList = new ArrayList<>();
            for (UserDO userDO : userDOList) {
                Tree tree = new Tree();
                tree.setId(String.valueOf(userDO.getUserId()));
                tree.setText(userDO.getName());
                childrenTreeList.add(tree);
            }

            Tree<Object> tree = new Tree<Object>();

            tree.setId(sysDept.getDeptId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setChildren(childrenTreeList);
            tree.setText(sysDept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Object> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public boolean checkDeptHasUser(Long deptId) {
        // TODO Auto-generated method stub
        //查询部门以及此部门的下级部门
        int result = sysDeptMapper.getDeptUserNumber(deptId);
        return result == 0 ? true : false;
    }

}
