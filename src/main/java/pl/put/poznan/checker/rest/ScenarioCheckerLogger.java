package pl.put.poznan.checker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Component for logging messages related to scenario checking using the SLF4J logging API.
 */
@Component
public class ScenarioCheckerLogger {
    /**
     * A logger used to log messages using the SLF4J logging API.
     */
    static final Logger logger = LoggerFactory.getLogger(ScenarioCheckerLogger.class);

    /**
     * Logs the steps in the given list using the logger.
     * @param steps the ArrayList of steps to log
     */
    void logSteps(ArrayList<Step> steps) {
        for (Step step : steps) {
            logger.debug("Step with level: {} actor: {} keyword: {} action: {}",
                    step.getStepLevel(), step.getActor(), step.getKeyword(), step.getAction());
            if(step.getSubsteps()!=null) {
                logSteps(step.getSubsteps());
            }
        }
    }

    /**
     * Logs the steps in the given list using the logger.
     * @param steps the list of steps to log
     */
    void logSteps(List<Step> steps) {
        for (Step step : steps) {
            logger.debug("Step with level: {} actor: {} keyword: {} action: {}",
                    step.getStepLevel(), step.getActor(), step.getKeyword(), step.getAction());
            if(step.getSubsteps()!=null) {
                logSteps(step.getSubsteps());
            }
        }
    }
}
