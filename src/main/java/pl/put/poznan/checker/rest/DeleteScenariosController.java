package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.model.Scenario;

import java.util.Map;

/**
 * REST controller responsible for deleting scenarios.
 * Implemented in accordance with REST framework.
 */
@RestController
public class DeleteScenariosController extends ScenarioController{

    /**
     * Constructor for DeleteScenariosController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    DeleteScenariosController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles DELETE requests to the "/scenarios" endpoint and
     * removes all scenarios from the storage.
     * Produces application/JSON response
     * @return an empty map representing the cleared storage
     */
    @DeleteMapping(value="/scenarios", produces="application/JSON")
    public Map<String, Scenario> deleteScenarios() {
        storage.scenarios.clear();
        ScenarioCheckerLogger.logger.info("Deleting Scenarios");
        return storage.scenarios;
    }
}
