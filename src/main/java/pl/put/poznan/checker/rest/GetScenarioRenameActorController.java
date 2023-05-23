package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioActorRenameVisitor;
import pl.put.poznan.checker.model.Scenario;

/**
 * REST controller responsible for renaming an actor in a Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetScenarioRenameActorController extends ScenarioController {

    /**
     * Constructor for GetScenarioRenameActorController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetScenarioRenameActorController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios/{title}/renameactor/{oldactor}/{newactor}"
     */
    @GetMapping(value="/scenarios/{title}/renameactor/{oldactor}/{newactor}", produces = "application/JSON")
    public Scenario getScenariosRenameActor(@PathVariable("title")String title, @PathVariable("oldactor")String oldactor, @PathVariable("newactor")String newactor) {

        Scenario scenario = storage.scenarios.get(title);

        ScenarioActorRenameVisitor actorRenameVisitor = new ScenarioActorRenameVisitor(oldactor, newactor);
        scenario.accept(actorRenameVisitor);
        return actorRenameVisitor.getScenario();
    }
}
