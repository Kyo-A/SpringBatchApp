package com.example.springbatchapp.batch.listener;

import com.example.springbatchapp.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobResultListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobResultListener.class);
    private final PersonRepository personRepository;

    @Autowired
    public JobResultListener(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("!!! BEFORE STARTING ! Wait the results");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("!!! JOB COMPLETED! Verify the results");
            personRepository.findAll().forEach(person -> log.info("Found -> " + person + "in the database"));
        } else{
            log.error("!!! JOB Failure! Verify the source");
        }
    }
}
