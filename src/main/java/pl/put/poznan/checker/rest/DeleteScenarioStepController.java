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
     * Handles DELETE requests to the "/scenarios/{title}/step/{stepLevel}" endpoint and deletes a step from the scenario with the given title.
     *
     * The step to delete is identified by provided String type stepLevel variable.
     *
     * @param title (String) the title of the scenario to delete the step from, as a path variable
     * @param stepLevel (String) the level of the step to delete, as a path variable
     * @return (Scenario) the updated scenario with the step deleted
     */
    @DeleteMapping(value="/scenarios/{title}/step/{stepLevel:.+}", produces="application/JSON")
    public Scenario deleteStep(@PathVariable("title")String title, @PathVariable("stepLevel")String stepLevel) {
        Scenario scenario = storage.scenarios.get(title);
        ScenarioDeleteStepVisitor deleteStepVisitor = new ScenarioDeleteStepVisitor(stepLevel);
        scenario.accept(deleteStepVisitor);

        return deleteStepVisitor.getScenario();
    }
}
