package com.yiko.ss.service.impl;

import com.github.pagehelper.PageInfo;
import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.common.utils.KeyUtil;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.common.utils.ShiroUtils;
import com.yiko.ss.dao.BlackWhiteListDao;
import com.yiko.ss.domain.AffairMaterials;
import com.yiko.ss.domain.BlackWhiteList;
import com.yiko.ss.service.BlackWhiteListService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BlackWhiteListServiceImpl extends BaseServiceImpl<BlackWhiteList> implements BlackWhiteListService {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private BlackWhiteListDao blackWhiteListDao;

    @Override
    protected void setMapping() {
        this.setMapper(this.blackWhiteListDao, BlackWhiteList.class);
    }

    @Override
    public BlackWhiteList selectById(String id) {
        return blackWhiteListDao.selectById(id);
    }

    @Override
    public R removeById(String id) {
        int r = blackWhiteListDao.removeById(id);
        if (r > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Override
    public R insert(BlackWhiteList entry) {
        String id = KeyUtil.genUniqueKey();
        entry.setId(id);
        entry.setCreator(ShiroUtils.getUser().getUsername());
        entry.setCreattime(format.format(new Date()));
        int r = blackWhiteListDao.insert(entry);
        if (r > 0) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @Override
    public R updateById(BlackWhiteList entry) {
        entry.setUpdater(ShiroUtils.getUser().getUsername());
        entry.setUpdatetime(format.format(new Date()));
        int r = blackWhiteListDao.updateById(entry);
        if (r > 0) {
            return R.ok();
        } else {
            return R.error();
        }

    }

    @Override
    public PageUtils queryList(Map entry) {
        List<BlackWhiteList> list = blackWhiteListDao.queryList(entry);
        PageInfo<BlackWhiteList> pageInfo = new PageInfo<>(list);
        return new PageUtils(list, new Long(pageInfo.getTotal()).intValue());
    }
}
