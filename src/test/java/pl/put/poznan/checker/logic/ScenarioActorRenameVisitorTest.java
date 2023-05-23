package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class implemented to test ScenarioActorRenameVisitor class functionality
 * with the use of unit tests performed with JUnit library.
 */
class ScenarioActorRenameVisitorTest {
    /**
     * Method used to test the implementation of ScenarioActorRenameVisitor's
     * visit() method and the resulting change of names of specified actors.
     * Testing functionality was implemented with the use of JUnit
     * library.
     */
    @Test
    void visit(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step("old actor", "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("old actor", "keyword0", "action0", null, "0");
        Step step2 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("old actor", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("old actor", "keyword0", "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        Visitor visitor = new ScenarioActorRenameVisitor("old actor", "new actor");

        scenario.accept(visitor);


        assertEquals("actor0", step2_3_2.getActor());
        assertEquals("new actor", step2_3_1.getActor());
        assertEquals("actor0", step2_3.getActor());
        assertEquals("actor0", step2_2.getActor());
        assertEquals("new actor", step2_1.getActor());
        assertEquals("actor0", step2.getActor());
        assertEquals("actor0", step1_2.getActor());
        assertEquals("new actor", step1_1.getActor());
        assertEquals("actor0", step1.getActor());
        assertEquals("new actor", step0.getActor());


    }

}