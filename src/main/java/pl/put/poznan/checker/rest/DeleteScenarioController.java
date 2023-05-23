package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.model.Scenario;

import java.util.Map;

/**
 * REST controller responsible for deleting a scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class DeleteScenarioController extends ScenarioController{

    /**
     * Constructor for DeleteScenarioStepController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    DeleteScenarioController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles DELETE requests to the "/scenario/{title}" endpoint and
     * removes the scenario with the given title from the storage.
     * Produces application/JSON response
     * @param title the title of the scenario to remove as a path variable
     * @return a map representing the updated storage after removing the scenario
     */
    @DeleteMapping(value="/scenarios/{title}", produces="application/JSON")
    public Map<String, Scenario> deleteScenario(@PathVariable("title")String title) {
        storage.scenarios.remove(title);
        ScenarioCheckerLogger.logger.info("Deleted Scenario with title {}", title);
        return storage.scenarios;
    }
}
