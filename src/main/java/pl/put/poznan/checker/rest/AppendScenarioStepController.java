package pl.put.poznan.checker.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioAppendStepVisitor;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

/**
 * REST controller responsible for appending a step to an existing scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class AppendScenarioStepController extends ScenarioController {

    /**
     * Constructor for AppendScenarioStepController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    AppendScenarioStepController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles POST requests to the "/scenarios/{title}/step" endpoint and appends a new step to the scenario with the given title.
     *
     * @param title (String) the title of the scenario to append the step to, as a path variable
     * @param stepContent (String) the content of the new step, as a JSON object in the request body
     * @return the updated scenario with the new step appended
     */
    @PostMapping(value="/scenarios/{title}/step", produces="application/JSON")
    public Scenario appendScenario(@PathVariable("title")String title, @RequestBody String stepContent) {
        Scenario newScenario = storage.scenarios.get(title);;
        Step newStep = null;
        JSONArray stepArray = new JSONArray();

        try {
            JSONObject stepObj = new JSONObject(stepContent);
            stepArray.put(stepObj);
            newStep = JSONParser.parseScenarioSteps(stepArray, Integer.toString(newScenario.getSteps().size()+1), 0).get(0);
            stepArray.put(newStep);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ScenarioAppendStepVisitor appendStepVisitor = new ScenarioAppendStepVisitor(newStep);
        newScenario.accept(appendStepVisitor);

        return appendStepVisitor.getScenario();
    }
}
