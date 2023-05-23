package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioAppendStepVisitorTest {

    @Test
    void visit() {
        Step step2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        Step step3 = new Step("actor0", "keyword0", "action0", null, "0");

        ScenarioAppendStepVisitor visitor = new ScenarioAppendStepVisitor(step3);

        scenario.accept(visitor);

        ArrayList<Step> expectedList = new ArrayList<>(Arrays.asList(step0, step1, step2, step3));
        ArrayList<Step> actualList = visitor.getScenario().getSteps();

        assertEquals(expectedList, actualList);
    }
}