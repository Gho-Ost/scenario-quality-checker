package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioActorRenameVisitor;
import pl.put.poznan.checker.model.Scenario;

@RestController
public class GetScenarioRenameActorController extends ScenarioController {
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
