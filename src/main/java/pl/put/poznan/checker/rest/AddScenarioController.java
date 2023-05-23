package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * Controller class created in accordance to REST framework,
 * responsible for handling the implementation of the AddScenario() functionality
 * in the Rest framework.
 */
@RestController
public class AddScenarioController extends ScenarioController {
    AddScenarioController(ScenarioStorage storage, ScenarioCheckerLogger logger){
        super(storage, logger);
    }

    /**
     * Handles POST requests to the "/scenario" endpoint and adds a new scenario
     * to the storage based on the JSON representation provided in the request body.
     * Produces application/JSON response
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return the added scenario
     */
    @PostMapping(value="/scenario", produces="application/JSON")
    public Scenario addScenario(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        storage.scenarios.put(newScenario.getTitle(), newScenario);
        ScenarioCheckerLogger.logger.info("Adding Scenario of title: {}", newScenario.getTitle());
        ScenarioCheckerLogger.logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                newScenario.getSystemActor());
        super.logger.logSteps(newScenario.getSteps());
        return newScenario;
    }
}
