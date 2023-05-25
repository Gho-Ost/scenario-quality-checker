package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class implemented to test ScenarioMissingActorVisitor class
 * functionality with the use of unit tests performed with JUnit library.
 */
public class ScenarioMissingActorVisitorTest {

    /**
     * Test for the visit() method of the ScenarioMissingActorVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately finds and returns steps containing missing Actors, in this particular
     * case for a single missing actor.
     */
    @Test
    void visitOneMissingActor(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step2 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));
        List<Step> noActorSteps = new ArrayList<>();
        noActorSteps.add(step2);

        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        assertEquals(noActorSteps, visitor.getNoActorSteps());
    }

    /**
     * Test for the visit() method of the ScenarioMissingActorVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately finds and returns steps containing missing Actors, in this particular
     * case for multiple missing actors.
     */
    @Test
    void visitManyMissingActors(){
        Step step2_3_2 = new Step(null, "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step2 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step(null, "keyword0", "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));
        List<Step> noActorSteps = Arrays.asList(step0,step1,step2,step2_3,step2_3_2);

        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        assertEquals(noActorSteps, visitor.getNoActorSteps());
    }


    /**
     * Test for the visit() method of the ScenarioMissingActorVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately finds and returns steps containing missing Actors, in this particular
     * case for no missing actors.
     */
    @Test
    void visitNoMissingActors(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step2 = new Step("actor2", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));
        List<Step> noActorSteps = new ArrayList<>();

        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        assertEquals(noActorSteps, visitor.getNoActorSteps());
    }
}
