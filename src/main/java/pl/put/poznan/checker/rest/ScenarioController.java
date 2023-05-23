package pl.put.poznan.checker.rest;

/**
 * Base class for controllers that handle requests related to scenarios.
 */
public class ScenarioController {
    /**
     * The storage used to store and retrieve scenarios.
     */
    ScenarioStorage storage;
    /**
     * The logger used to log messages related to scenario checking.
     */
    ScenarioCheckerLogger logger;
    /**
     * Constructs a new ScenarioController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    ScenarioController(ScenarioStorage storage, ScenarioCheckerLogger logger){
        this.storage = storage;
        this.logger = logger;
    }
}
