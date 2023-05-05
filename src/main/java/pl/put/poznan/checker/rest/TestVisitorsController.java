package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.*;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

@RestController
public class TestVisitorsController extends ScenarioController{
    TestVisitorsController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Test of visitor functionality
     * @param scenarioContent String containing contents of the scenario.
     * @return new Scenario after tests.
     */
    @PostMapping("/test")
    public Scenario testVisitors(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//         newScenario.printScenario();

        ScenarioTextDownloadVisitor textVisitor = new ScenarioTextDownloadVisitor();
        ScenarioCountVisitor countVisitor = new ScenarioCountVisitor();
        ScenarioKeyWordCountVisitor keyWordCountVisitor = new ScenarioKeyWordCountVisitor();
        ScenarioLevelVisitor levelVisitor = new ScenarioLevelVisitor(2);
        ScenarioMissingActorVisitor missingActorVisitor = new ScenarioMissingActorVisitor();

        newScenario.accept(textVisitor);

        newScenario.accept(countVisitor);
        System.out.println("\nCounted steps: " + countVisitor.getStepCount());

        newScenario.accept(keyWordCountVisitor);
        System.out.println("\nKeyword count: " + keyWordCountVisitor.getKeyWordCount());

        newScenario.accept(levelVisitor);
        System.out.println("\nScenario level 2:");
        levelVisitor.getScenario().printScenario();

        System.out.println("\nMissing actor steps:");
        newScenario.accept(missingActorVisitor);
        for (Step step : missingActorVisitor.getNoActorSteps()){
            step.printStep();
        }

        return newScenario;
    }
}
