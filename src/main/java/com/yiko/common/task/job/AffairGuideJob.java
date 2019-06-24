package com.yiko.common.task.job;

import com.yiko.ss.service.AffairGuideService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class AffairGuideJob implements Job {
    @Autowired
    AffairGuideService affairGuideService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        affairGuideService.pullAffairGuide();
    }
}
