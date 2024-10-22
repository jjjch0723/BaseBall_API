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
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.game.baseball.api.service.CombinedTasklet;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    // 일정 및 경기 기록을 들고옴
    @Autowired
    private CombinedTasklet combinedTasklet;

    @Autowired
    private ExternalConfig externalConfig;

    @Bean
    public Job job() {
        Step step = stepBuilderFactory.get("step")
                .tasklet(combinedTasklet)
                .build();

        return jobBuilderFactory.get("job")
                .start(step)
                .build();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        String cronExpression = externalConfig.getScheduleCron();
        if (cronExpression == null || cronExpression.isEmpty()) {
            logger.warn("Cron expression is not defined. Scheduling will not be enabled.");
            return;
        }
        taskRegistrar.addCronTask(this::perform, cronExpression);
    }

    public void perform() {
        logger.info("Batch will start.");
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Batch Job is started at {}", startTime);

            JobParameters params = new JobParametersBuilder()
                    .addLong("time", startTime)
                    .toJobParameters();

            jobLauncher.run(job(), params);

            long endTime = System.currentTimeMillis();
            logger.info("Batch Job completed in {} ms", (endTime - startTime));
        } catch (Exception e) {
            logger.error("Batch job start failed", e);
        }
    }
}