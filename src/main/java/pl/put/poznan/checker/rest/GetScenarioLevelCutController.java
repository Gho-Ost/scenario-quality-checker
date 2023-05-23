package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioLevelVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller responsible for performing the level-cutting operation on a Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetScenarioLevelCutController extends ScenarioController{
    /**
     * Constructor for GetScenarioLevelCutController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetScenarioLevelCutController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios/{title}/levelcut/{maxLevel}" endpoint and returns
     * a truncated version of the scenario with the given title for a stored Scenario.
     * Truncating in achieved by using the Visitor design pattern.
     * Produces application/JSON response
     * @param title the title of the scenario as a path variable
     * @param maxLevel the maximum level to include in the level-cut scenario as a path variable
     * @return a level-cut version of the scenario with the given title
     */
    @GetMapping(value="/scenarios/{title}/levelcut/{maxLevel}", produces = "application/JSON")
    public Scenario getScenarioLevelCut(@PathVariable String title, @PathVariable Integer maxLevel) {
        ScenarioCheckerLogger.logger.info("Getting ScenarioLevelCut for scenario with title: {} and cutting-level: {}", title, maxLevel);
        Scenario scenario = storage.scenarios.get(title);
        ScenarioCheckerLogger.logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        scenario.accept(visitor);
        Scenario result=visitor.getScenario();
        ScenarioCheckerLogger.logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                result.getTitle(), Arrays.toString(result.getActors()),
                result.getSystemActor());
        super.logger.logSteps(result.getSteps());
        return result;
    }
}
