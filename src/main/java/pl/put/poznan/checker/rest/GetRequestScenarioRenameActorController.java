package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioActorRenameVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

@RestController
public class GetRequestScenarioRenameActorController extends ScenarioController{
    GetRequestScenarioRenameActorController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/renameactor/{oldactor}/{newactor}" endpoint
     */
    @GetMapping(value="/scenario/renameactor/{oldactor}/{newactor}", produces = "application/JSON")
    public Scenario getRequestScenarioRenameActor(@RequestBody String scenarioContent, @PathVariable("oldactor")String oldactor, @PathVariable("newactor")String newactor) {
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

        ScenarioActorRenameVisitor actorRenameVisitor = new ScenarioActorRenameVisitor(oldactor, newactor);
        newScenario.accept(actorRenameVisitor);
        return actorRenameVisitor.getScenario();
    }
}
