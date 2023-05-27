package pl.put.poznan.checker.rest;

import org.springframework.stereotype.Component;
import pl.put.poznan.checker.model.Scenario;

import java.util.HashMap;
import java.util.Map;

/**
 * Component for storing and retrieving Scenario objects.
 */
@Component
public class ScenarioStorage {
    /**
     * A map used to store Scenario objects with a String key.
     * The key for each Scenario object is the scenario's title. The value for each key is the corresponding Scenario object.
     */
    Map<String, Scenario> scenarios = new HashMap<String, Scenario>();

    public void setScenarios(HashMap<String, Scenario> scenarios){
        this.scenarios=scenarios;
    }
}
