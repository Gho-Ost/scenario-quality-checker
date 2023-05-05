package pl.put.poznan.checker.rest;

import org.springframework.stereotype.Component;
import pl.put.poznan.checker.model.Scenario;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioStorage {
    /**
     * A map used to store Scenario objects with a String key.
     */
    Map<String, Scenario> scenarios = new HashMap<String, Scenario>();

}
