package pl.put.poznan.checker.rest;

import org.json.JSONArray;
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
    public Scenario testVisitors(@RequestBody String scenarioContent) throws JSONException {
        Scenario newScenario = null;
        Step newStep = null;
        JSONArray stepArray = new JSONArray();

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
            JSONObject stepObj = new JSONObject("{\"IF\": {\"Librarian\": \"wishes to add copies of the book\"}}");
            stepArray.put(stepObj);
            newStep = JSONParser.parseScenarioSteps(stepArray, Integer.toString(newScenario.getSteps().size()+1), 0).get(0);
            stepArray.put(newStep);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//         newScenario.printScenario();
//
//        ScenarioTextDownloadVisitor textVisitor = new ScenarioTextDownloadVisitor();
//        ScenarioStepCountVisitor countVisitor = new ScenarioStepCountVisitor();
//        ScenarioKeyWordCountVisitor keyWordCountVisitor = new ScenarioKeyWordCountVisitor();
//        ScenarioLevelVisitor levelVisitor = new ScenarioLevelVisitor(2);
//        ScenarioMissingActorVisitor missingActorVisitor = new ScenarioMissingActorVisitor();

//        ScenarioActorStepCountVisitor actorStepCountVisitor = new ScenarioActorStepCountVisitor("Tes");

        String deleted = "0";
        ScenarioDeleteStepVisitor deleteStepVisitor = new ScenarioDeleteStepVisitor(deleted);
        ScenarioAppendStepVisitor appendStepVisitor = new ScenarioAppendStepVisitor(newStep);
        ScenarioActorRenameVisitor actorRenameVisitor = new ScenarioActorRenameVisitor("Librarian", "NEWLibrarian");

//        newScenario.accept(actorStepCountVisitor);
//        System.out.println("\nScenario Actor count:" + actorStepCountVisitor.getActorStepCount());

        newScenario.accept(deleteStepVisitor);
        System.out.println("\nDeleted " + deleted);
        deleteStepVisitor.getScenario().printScenario();

//        newScenario.accept(appendStepVisitor);
//        System.out.println("\nAppended:");
//        appendStepVisitor.getScenario().printScenario();

//        newScenario.accept(actorRenameVisitor);
//        System.out.println("\nRenamed Librarian:");
//        actorRenameVisitor.getScenario().printScenario();


//        newScenario.accept(textVisitor);
//
//        newScenario.accept(countVisitor);
//        System.out.println("\nCounted steps: " + countVisitor.getStepCount());
//
//        newScenario.accept(keyWordCountVisitor);
//        System.out.println("\nKeyword count: " + keyWordCountVisitor.getKeyWordCount());
//
//        newScenario.accept(levelVisitor);
//        System.out.println("\nScenario level 2:");
//        levelVisitor.getScenario().printScenario();
//
//        System.out.println("\nMissing actor steps:");
//        newScenario.accept(missingActorVisitor);
//        for (Step step : missingActorVisitor.getNoActorSteps()){
//            step.printStep();
//        }

        return newScenario;
    }
}
