package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioStepCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller for handling GET requests to perform step count for the Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestScenarioStepCountController extends  ScenarioController{

    /**
     * Constructor for GetRequestScenarioStepCountController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetRequestScenarioStepCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/stepcount" endpoint and returns
     * the step count for the scenario provided in the request body.
     * Produces application/JSON response.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return the step count for the provided scenario
     */
    @GetMapping(value = "/scenario/stepcount", produces = "application/JSON")
    public String getRequestScenarioStepCount(@RequestBody String scenarioContent) {
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
        ScenarioStepCountVisitor visitor = new ScenarioStepCountVisitor();
        newScenario.accept(visitor);
        ScenarioCheckerLogger.logger.info("Returning step count {}",visitor.getStepCount());

        return "{\"Step count\": " + visitor.getStepCount() + "}";
    }
}
