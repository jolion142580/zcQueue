package com.yiko.common.task.job;

import com.yiko.ss.service.AffairObjectService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class AffairObjectJob implements Job {
    @Autowired
    AffairObjectService affairObjectService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        affairObjectService.pullAffairObject();
    }
}
