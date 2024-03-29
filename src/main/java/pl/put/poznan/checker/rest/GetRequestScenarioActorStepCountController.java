package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioActorStepCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller for handling GET requests to perform count on the number of steps performed
 * by a given actor. Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestScenarioActorStepCountController extends ScenarioController {

    /**
     * Constructor for GetRequestScenarioActorStepCountController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetRequestScenarioActorStepCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/actorstepcount/{actor}" endpoint and
     * returns the number of steps for the given actor in the
     * scenario provided in the request body.
     * The number of steps for the given actor is returned inside the response body as a JSON object.
     *
     * @param actor (String) the name of the actor to count steps for, as a path variable
     * @param scenarioContent (String) a JSON representation of the scenario as a request body
     * @return (String) a JSON object containing the number of steps for the given actor
     */
    @GetMapping(value = "/scenario/actorstepcount/{actor}", produces = "application/JSON")
    public String getRequestScenarioActorStepCount(@PathVariable("actor")String actor, @RequestBody String scenarioContent) {
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

        ScenarioActorStepCountVisitor actorStepCountVisitor = new ScenarioActorStepCountVisitor(actor);
        newScenario.accept(actorStepCountVisitor);
        ScenarioCheckerLogger.logger.info("Returning step count {}", actorStepCountVisitor.getActorStepCount());

        return "{\"" + actor + " step count\": " + actorStepCountVisitor.getActorStepCount() + "}";
    }
}
