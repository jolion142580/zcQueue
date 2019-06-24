package com.yiko.common.task.job;

import com.yiko.ss.service.AffairsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class AffairJob implements Job {
    @Autowired
    AffairsService affairsService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        affairsService.pullAffairs();
    }
}
