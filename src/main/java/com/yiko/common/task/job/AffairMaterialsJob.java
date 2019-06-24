package com.yiko.common.task.job;

import com.yiko.ss.service.AffairMaterialsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class AffairMaterialsJob implements Job {
    @Autowired
    AffairMaterialsService affairMaterialsService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        affairMaterialsService.pullAffairMaterials();
    }
}
