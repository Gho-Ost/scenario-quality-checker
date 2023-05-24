package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class implemented to test ScenarioKeyWordCountVisitor class
 * functionality with the use of unit tests performed with JUnit library.
 */
public class ScenarioKeyWordCountVisitorTest {

    /**
     * Test for the visit() method of the ScenarioKeyWordCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts number of key words present in a Scenario.
     */
    @Test
    void visit() {
        Step step2_3_2 = new Step("actor0", "IF", "action0", null, "0");
        Step step2_3_1 = new Step("actor0", null, "action0", null, "0");
        Step step2_3 = new Step("actor0", "ELSE", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor0", "ELSE", "action0", null, "0");
        Step step2_1 = new Step("actor0", "IF", "action0", null, "0");
        Step step2 = new Step("actor0", "IF", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor0", null, "action0", null, "0");
        Step step1_1 = new Step("actor0", null, "action0", null, "0");
        Step step1 = new Step("actor0", null, "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", null, "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();

        scenario.accept(visitor);
        assertEquals(5, visitor.getKeyWordCount());

    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts number of steps present in a Scenario. In this particular
     * case for a Scenario containing no key words.
     */
    @Test
    void visitEmptyScenario(){
        Step step2_3_2 = new Step("actor0", null, "action0", null, "0");
        Step step2_3_1 = new Step("actor0", null, "action0", null, "0");
        Step step2_3 = new Step("actor0", null, "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor0", null, "action0", null, "0");
        Step step2_1 = new Step("actor0", null, "action0", null, "0");
        Step step2 = new Step("actor0", null, "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor0", null, "action0", null, "0");
        Step step1_1 = new Step("actor0", null, "action0", null, "0");
        Step step1 = new Step("actor0", null, "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", null, "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();

        scenario.accept(visitor);
        assertEquals(0, visitor.getKeyWordCount());

    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts number of steps present in a Scenario. In this particular
     * case for an empty Scenario.
     */
    @Test
    void visitNoKeyWords(){
        String[] emptyArray = new String[0];
        ArrayList<Step> emptyArrayList = new ArrayList<Step>();
        Scenario scenario = new Scenario("emptyScenario",emptyArray, null,emptyArrayList);
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();

        scenario.accept(visitor);
        assertEquals(0, visitor.getKeyWordCount());

    }

}
