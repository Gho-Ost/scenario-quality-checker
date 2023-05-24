package pl.put.poznan.checker.model;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import pl.put.poznan.checker.logic.Visitable;
import pl.put.poznan.checker.logic.Visitor;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testing class implemented to test Scenario class functionality
 * with the use of unit tests performed with JUnit library and Mockito libraries.
 */
class ScenarioTest {

    /**
     * Testing method written to assert the correctness of the implementation of the "accept()"
     * method in the Scenario. Tested by creating a Mock visitor and checking the order
     * of visited steps.
     */
    @Test
    void accept() {
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3_1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "0");
        Step step2_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2_1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step2 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "0");
        Step step1_2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step1_1 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "0");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        Scenario scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));

        Visitor visitor = mock(Visitor.class);

        scenario.accept(visitor);

        verify(visitor, times(1)).visit(Mockito.any(Scenario.class));
        verify(visitor, times(10)).visit(Mockito.any(Step.class));

        InOrder inOrder = inOrder(visitor);
        inOrder.verify(visitor).visit(scenario);
        inOrder.verify(visitor).visit(step0);
        inOrder.verify(visitor).visit(step1);
        inOrder.verify(visitor).visit(step1_1);
        inOrder.verify(visitor).visit(step1_2);
        inOrder.verify(visitor).visit(step2);
        inOrder.verify(visitor).visit(step2_1);
        inOrder.verify(visitor).visit(step2_2);
        inOrder.verify(visitor).visit(step2_3);
        inOrder.verify(visitor).visit(step2_3_1);
        inOrder.verify(visitor).visit(step2_3_2);

    }

    /**
     * Testing method written to assert the correctness of the implementation of the "accept()"
     * method in the Scenario. In this case testing an empty Scenario.
     * Tested by creating a Mock visitor and checking the order of visited steps.
     */
    @Test
    void acceptEmptyScenario(){
        String[] emptyArray = new String[0];
        ArrayList<Step> emptyArrayList = new ArrayList<Step>();
        Scenario scenario = new Scenario("emptyScenario",emptyArray, null,emptyArrayList);
        Visitor visitor = mock(Visitor.class);
        scenario.accept(visitor);
        verify(visitor, times(1)).visit(Mockito.any(Scenario.class));
        verify(visitor, times(0)).visit(Mockito.any(Step.class));
    }
}