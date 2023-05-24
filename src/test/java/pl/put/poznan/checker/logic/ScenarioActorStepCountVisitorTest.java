package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class implemented to test ScenarioActorStepCountVisitor class
 * functionality with the use of unit tests performed with JUnit library.
 */
class ScenarioActorStepCountVisitorTest {
    Scenario scenario;

    /**
     * Method responsible for setting up the appropriate variables for future tests.
     * In this case the method creates a test Scenario that the future tests will use to
     * evaluate if the tested implementations are working correctly.
     */
    @BeforeEach
    void setup(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step2 = new Step("actor1", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor1", "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));
    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts the ActorStepCount.
     */
    @Test
    void visit() {
        ScenarioActorStepCountVisitor visitor = new ScenarioActorStepCountVisitor("actor0");

        scenario.accept(visitor);

        assertEquals(3, visitor.getActorStepCount());
    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts the ActorStepCount for the case of testing multiple
     * the presence of multiple actors.
     */
    @Test
    void multipleVisits(){

        ScenarioActorStepCountVisitor visitor = new ScenarioActorStepCountVisitor("actor0");

        scenario.accept(visitor);
        assertEquals(3, visitor.getActorStepCount());

        visitor.setSelectedActor("actor1");
        scenario.accept(visitor);
        assertEquals(4, visitor.getActorStepCount());

        visitor.setSelectedActor("actor2");
        scenario.accept(visitor);
        assertEquals(3, visitor.getActorStepCount());
    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts the ActorStepCount for the case of testing for an
     * actor that is not in the Scenario.
     */
    @Test
    void unknownActor(){
        ScenarioActorStepCountVisitor visitor = new ScenarioActorStepCountVisitor("unknown actor");

        scenario.accept(visitor);
        assertEquals(0, visitor.getActorStepCount());
    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts the ActorStepCount for the case of handling a Scenario with
     * null values for some actors.
     */

    @Test
    void scenarioWithMissingActors(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step(null, "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step2 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        ScenarioActorStepCountVisitor visitor = new ScenarioActorStepCountVisitor("actor0");

        scenario.accept(visitor);

        assertEquals(3, visitor.getActorStepCount());
    }

    /**
     * Test for the visit() method of the ScenarioActorStepCountVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately counts the ActorStepCount for the case of handling a Scenario with
     * null values for some actors and null passed as an argument to the visitor.
     */
    @Test
    void scenarioWithNullArgument(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step(null, "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step2 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        ScenarioActorStepCountVisitor visitor = new ScenarioActorStepCountVisitor(null);

        scenario.accept(visitor);

        assertEquals(0, visitor.getActorStepCount());
    }
}