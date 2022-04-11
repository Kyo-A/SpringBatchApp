package com.example.springbatchapp.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("jobStoreToDb")
    private Job jobStoreToDb;

    @Autowired
    @Qualifier("jobManyFilesToDb")
    private Job jobManyFilesToDb;

    @GetMapping("/loadData")
    public String handleJobStoreToDb() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(jobStoreToDb, jobParameters);
        return "Le job a ete execute";
    }

    @GetMapping("/loadDatas")
    public String handleJobManyFilesToDb() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(jobManyFilesToDb, jobParameters);
        return "Le job a ete execute";
    }
}
