package pl.put.poznan.checker.rest;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class implemented to test the functionality of GetScenarioKeywordCount.
 * Tested using JUnit and Mockito libraries to simulate behaviour of REST controller
 * class independently of storage and logger class instances.
 */
public class GetScenarioKeywordCountTest {

    /**
     * Testing method meant to test the functionality of calculating Scenario's key word count
     * present in REST implementation. The test is performed with help of JUnit and
     * Mockito libraries.
     */
    @Test
    public void testGetKeyWordCount() {
        String title = "testTitle";
        Step step2_3_2 = new Step("actor0", "IF", "action0", null, "0");
        Step step2_3_1 = new Step("actor1", "ELSE", "action0", null, "0");
        Step step2_3 = new Step("actor0", null, "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "ELSE", "action0", null, "0");
        Step step2_1 = new Step("actor2", "IF", "action0", null, "0");
        Step step2 = new Step("actor1", null, "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", null, "action0", null, "0");
        Step step1_1 = new Step("actor2", null, "action0", null, "0");
        Step step1 = new Step("actor1", null, "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "IF", "action0", null, "0");
        Scenario scenario = new Scenario(title, new String[]{"actor0, actor1"}, "actor2",
                new ArrayList<>(Arrays.asList(step0, step1, step2)));

        ScenarioStorage storage = new ScenarioStorage();
        ScenarioCheckerLogger logger = mock(ScenarioCheckerLogger.class);
        HashMap<String, Scenario> scenarios = mock(HashMap.class);
        when(scenarios.get(title)).thenReturn(scenario);
        storage.setScenarios(scenarios);
        GetScenarioKeywordCount controller = new GetScenarioKeywordCount(storage,logger);

        String result = controller.getScenarioKeywordCount(title);
        assertEquals("{\"Keyword count\": " + 5 + "}", result);
    }
}
