package com.example.springbatchapp;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
public class SpringBatchAppApplication {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchAppApplication.class, args);
    }

    // Cron expression -> Seconde, minute, heure, jour(mois), mois, jour(semaine)
    // @Scheduled(cron = "*/10 * * * * *") // toutes les 5 minutes
    // @Scheduled(cron = "0 */30 8-10 * * MON-FRI") // du lundi au vendredi, a 8h, 8h30, 9h, 9h30, 10h
    // @Scheduled(cron = "0 0 0 25 12 *") // jour de noel a minuit
    // public void perform() throws Exception{
    //JobParameters jobParameters = new JobParametersBuilder()
    //.addLong("time", System.currentTimeMillis())
    //.toJobParameters();
    //jobLauncher.run(job, jobParameters);
    //}
}
