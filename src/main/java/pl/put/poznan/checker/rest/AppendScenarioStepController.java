package pl.put.poznan.checker.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioAppendStepVisitor;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

public class AppendScenarioStepController extends ScenarioController {

    AppendScenarioStepController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles POST requests to the append step to an existing scenario
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

        return newScenario;
    }
}
