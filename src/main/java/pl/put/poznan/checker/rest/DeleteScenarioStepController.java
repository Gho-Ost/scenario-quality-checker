package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioDeleteStepVisitor;
import pl.put.poznan.checker.model.Scenario;

/**
 * REST controller responsible for deleting a step. Implemented in accordance with REST framework.
 */
@RestController
public class DeleteScenarioStepController extends ScenarioController {

    /**
     * Constructor for DeleteScenarioStepController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
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
