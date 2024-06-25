package com.game.baseball.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.game.baseball.api.service.CombinedTasklet;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CombinedTasklet combinedTasklet;

    @Bean
    public Job job() {
        Step step = stepBuilderFactory.get("step")
                .tasklet(combinedTasklet)
                .build();

        return jobBuilderFactory.get("job")
                .start(step)
                .build();
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void perform() {
        try {
            logger.info("Batch Job is started at {}", System.currentTimeMillis());

            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job(), params);

            logger.info("Batch Job is start in {}", System.currentTimeMillis());
        } catch (Exception e) {
            logger.error("Batch job start failed", e);
        }
    }
}
