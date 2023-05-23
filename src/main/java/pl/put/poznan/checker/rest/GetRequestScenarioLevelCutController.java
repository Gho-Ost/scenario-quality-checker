package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioLevelVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller for handling GET requests to perform level cutting at a specified level.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestScenarioLevelCutController extends ScenarioController{
    GetRequestScenarioLevelCutController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/levelcut/{maxLevel}" endpoint
     * and returns a truncated version of the scenario provided in the request body.
     * Produces application/JSON response
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @param maxLevel the maximum level to include in the level-cut scenario as a path variable
     * @return a level-cut version of the provided scenario. The truncated Scenario
     * will be level-cut with accordance to specified value. For value of maxLevel
     * equal to 1, only the main scenario will be returned.
     */
    @GetMapping(value="/scenario/levelcut/{maxLevel}", produces = "application/JSON")
    public Scenario getRequestScenarioLevelCut(@RequestBody String scenarioContent, @PathVariable Integer maxLevel) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
            ScenarioCheckerLogger.logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            super.logger.logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        newScenario.accept(visitor);
        Scenario result=visitor.getScenario();
        ScenarioCheckerLogger.logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                result.getTitle(), Arrays.toString(result.getActors()),
                result.getSystemActor());
        super.logger.logSteps(result.getSteps());
        return result;
    }
}
