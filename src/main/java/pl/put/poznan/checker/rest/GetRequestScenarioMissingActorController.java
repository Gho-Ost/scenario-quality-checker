package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioMissingActorVisitor;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.Arrays;
import java.util.List;

/**
 * REST controller for handling GET requests to perform search for missing actors.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestScenarioMissingActorController extends ScenarioController{
    GetRequestScenarioMissingActorController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/missingactor" endpoint
     * and returns a list of steps with missing actors for the scenario
     * provided in the request body.
     * Produces application/JSON response
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return a list of steps with missing actors for the provided scenario
     */
    @GetMapping(value="/scenario/missingactor", produces = "application/JSON")
    public List<Step> getRequestScenarioMissingActor(@RequestBody String scenarioContent) {
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
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        newScenario.accept(visitor);
        super.logger.logSteps(visitor.getNoActorSteps());
        return visitor.getNoActorSteps();
    }

}
