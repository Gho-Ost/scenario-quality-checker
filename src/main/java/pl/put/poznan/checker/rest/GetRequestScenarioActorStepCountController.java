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
    GetRequestScenarioActorStepCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/actorstepcount/{actor}"
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
