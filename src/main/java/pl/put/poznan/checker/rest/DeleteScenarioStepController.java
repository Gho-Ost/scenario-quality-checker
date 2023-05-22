package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioDeleteStepVisitor;
import pl.put.poznan.checker.model.Scenario;

@RestController
public class DeleteScenarioStepController extends ScenarioController {
    DeleteScenarioStepController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }


    /**
     * Handles DELETE requests to the delete step of an existing scenario
     */
    @DeleteMapping(value="/scenarios/{title}/step/{stepLevel:.+}", produces="application/JSON")
    public Scenario deleteStep(@PathVariable("title")String title, @PathVariable("stepLevel")String stepLevel) {
        Scenario scenario = storage.scenarios.get(title);
        ScenarioDeleteStepVisitor deleteStepVisitor = new ScenarioDeleteStepVisitor(stepLevel);
        scenario.accept(deleteStepVisitor);

        return deleteStepVisitor.getScenario();
    }
}
