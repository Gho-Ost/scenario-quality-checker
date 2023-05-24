package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class implemented to test ScenarioAppendStepVisitor class
 * functionality with the use of unit tests performed with JUnit library.
 */
class ScenarioAppendStepVisitorTest {

    /**
     * Test for the visit() method of the ScenarioAppendStepVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately appends a Step to an existing Scenario.
     */
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

    /**
     * Test for the visit() method of the ScenarioAppendStepVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately appends a Step to an existing Scenario with an empty
     * list of steps.
     */
    @Test
    void visitEmptyScenario() {
        String[] emptyArray = new String[0];
        ArrayList<Step> emptyArrayList = new ArrayList<Step>();
        Scenario scenario = new Scenario("emptyScenario",emptyArray, null,emptyArrayList);

        Step newStep = new Step("actor0", "keyword0", "action0", null, "0");

        ScenarioAppendStepVisitor visitor = new ScenarioAppendStepVisitor(newStep);

        scenario.accept(visitor);

        ArrayList<Step> expectedList = new ArrayList<>(Arrays.asList(newStep));
        ArrayList<Step> actualList = visitor.getScenario().getSteps();

        assertEquals(expectedList, actualList);
    }
}