package com.game.baseball.api.service;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CombinedTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(CombinedTasklet.class);

    @Autowired
    private ResultPyRunnerImpl resultPyRunner;

    @Autowired
    private SchedulePyRunnerImpl schedulePyRunner;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Starting schedule processing");
        schedulePyRunner.pyRunner();
        logger.info("Schedule processing completed");

        logger.info("Starting result processing");
        resultPyRunner.pyRunner();
        logger.info("Result processing completed");

        return RepeatStatus.FINISHED;
    }
}
