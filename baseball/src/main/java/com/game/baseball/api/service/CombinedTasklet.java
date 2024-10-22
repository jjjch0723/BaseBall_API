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
    private SchedulePyRunnerImpl schedulePyRunner;

    @Autowired
    private ResultPyRunnerImpl resultPyRunner;

    @Autowired
    private GptPyRunnerImpl gptPyRunner;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            executeScheduleProcessing();
            executeResultProcessing();
            executeGptProcessing();
        } catch (Exception e) {
            logger.error("Error occurred during combined tasklet execution", e);
        }
        return RepeatStatus.FINISHED;
    }

    private void executeScheduleProcessing() {
        try {
            logger.info("Starting schedule processing");
            schedulePyRunner.pyRunner();
            logger.info("Schedule processing completed");
        } catch (Exception e) {
            logger.error("Failed during schedule processing", e);
        }
    }

    private void executeResultProcessing() {
        try {
            logger.info("Starting result processing");
            resultPyRunner.pyRunner();
            logger.info("Result processing completed");
        } catch (Exception e) {
            logger.error("Failed during result processing", e);
        }
    }

    private void executeGptProcessing() {
        try {
            logger.info("Starting GPT processing");
            gptPyRunner.pyRunner();
            logger.info("GPT processing completed");
        } catch (Exception e) {
            logger.error("Failed during GPT processing", e);
        }
    }
}
