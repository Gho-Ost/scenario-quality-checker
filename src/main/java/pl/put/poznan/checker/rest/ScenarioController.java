package pl.put.poznan.checker.rest;

public class ScenarioController {
    ScenarioStorage storage;
    ScenarioCheckerLogger logger;
    ScenarioController(ScenarioStorage storage, ScenarioCheckerLogger logger){
        this.storage = storage;
        this.logger = logger;
    }
}
