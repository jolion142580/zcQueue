package com.yiko.system.service;


import com.yiko.common.service.BaseService;
import com.yiko.system.domain.YuyueDO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
public interface YuyuesService extends BaseService<YuyueDO> {


	List<YuyueDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

}
