package com.yiko.common.domain;

import java.util.Date;

public abstract class BaseDO {
	
	public abstract String getId();
	public abstract void setId(String id);
	public abstract String getCreator();
	public abstract void setCreator(String creator);
	public abstract Date getCreateTime();
	public abstract void setCreateTime(Date date);
	public abstract String getUpdater();
	public abstract void setUpdater(String updater);
	public abstract Date getUpdateTime();
	public abstract void setUpdateTime(Date date);
	public abstract String getDataState();
	public abstract void setDataState(String datestate);
}
