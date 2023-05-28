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

/**
 * REST controller for handling GET requests to perform the rename operation on a specified actor.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestScenarioRenameActorController extends ScenarioController{

    /**
     * Constructor for GetRequestScenarioRenameActorController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetRequestScenarioRenameActorController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/renameactor/{oldactor}/{newactor}" endpoint and
     * renames the given actor in the scenario provided in the request body.
     * The updated scenario with the actor renamed is returned inside the response body as a JSON object.
     *
     * @param scenarioContent (String) a JSON representation of the scenario as a request body
     * @param oldactor (String) the name of the actor to rename, as a path variable
     * @param newactor (String) the new name for the actor, as a path variable
     * @return (Scenario) the updated scenario with the actor renamed
     */
    @GetMapping(value="/scenario/renameactor/{oldactor}/{newactor}", produces = "application/JSON")
    public Scenario getRequestScenarioRenameActor(@RequestBody String scenarioContent, @PathVariable("oldactor")String oldactor, @PathVariable("newactor")String newactor) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
            ScenarioCheckerLogger.logger.info("Parsing Object");
            ScenarioCheckerLogger.logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            super.logger.logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ScenarioActorRenameVisitor actorRenameVisitor = new ScenarioActorRenameVisitor(oldactor, newactor);
        ScenarioCheckerLogger.logger.info("Renaming the specified Actor");
        newScenario.accept(actorRenameVisitor);
        ScenarioCheckerLogger.logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                newScenario.getSystemActor());
        super.logger.logSteps(newScenario.getSteps());

        return actorRenameVisitor.getScenario();
    }
}
