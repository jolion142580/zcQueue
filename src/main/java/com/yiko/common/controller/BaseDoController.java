package com.yiko.common.controller;

import com.yiko.common.domain.BaseDO;
import com.yiko.common.service.BaseService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;

/**
 * 实现controller通用方法
 * 
 * @author wu199406
 *
 * @param <T>
 */

public abstract class BaseDoController<T extends BaseDO> extends BaseController
{
	private String indexPageUrl = null;//主页面
    private String addPageUrl = null;//添加页面
    private String editPageUrl = null;//编辑页面
	
    /**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
    
    /**
     * 日志对象
     */
    protected Logger loggerBase = null;
    
    /**
     * 记录错误信息到日志
     * @param msg
     * @param e
     */
    protected void setLoggerError(String msg,Exception e)
    {
    	if( this.loggerBase != null )
    	{
    		this.loggerBase.error(msg, e);
    	}
    }
    
    /**
     * 用于通用方法调用的service层对象
     */
    protected BaseService<T> baseService;
    
    /**
     * 用于设置通用方法调用的service层对象
     * @param baseService
     * @param loggerBase	logger4日志对象,可以为null,当为null时,不进行日志的记录
     */
    public void setBaseService(BaseService<T> baseService)
    {
    	this.baseService = baseService;
    	//this.loggerBase = loggerBase;
    }
    
    public abstract void setService();
    
    /**
     * 设置页面路径
     * @param indexPageUrl	主页面路径
     * @param addPageUrl	添加页面路径
     * @param editPageUrl	编辑页面路径
     */
    public void setPageUrl(String indexPageUrl,String addPageUrl,String editPageUrl)
    {
    	this.indexPageUrl = indexPageUrl;
    	this.addPageUrl = addPageUrl;
    	this.editPageUrl = editPageUrl;
    }



    


}
