package com.yiko.common.task.job;

import com.yiko.common.config.BootdoConfig;
import com.yiko.ss.service.BaseDicService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartJob implements Job {
    @Autowired
    BaseDicService baseDicService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        baseDicService.pullDepart();
    }

}
