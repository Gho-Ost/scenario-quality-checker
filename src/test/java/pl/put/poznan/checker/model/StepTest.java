package pl.put.poznan.checker.model;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import pl.put.poznan.checker.logic.Visitor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testing class implemented to test Step class functionality
 * with the use of unit tests performed with JUnit library and Mockito libraries.
 */
class StepTest {

    /**
     * Testing method written to assert the correctness of the implementation of the "accept()"
     * method in the Step. Tested by creating a Mock visitor and checking the order
     * of visited steps.
     */
    @Test
    void accept() {
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step1 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step0)), "0");
        Step step2 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2)), "0");
        Step step4 = new Step("actor0", "keyword0", "action0", null, "0");
        Step step5 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(
                step4, step3, step1
        )), "0");

        Visitor visitor = mock(Visitor.class);

        step5.accept(visitor);

        verify(visitor, times(6)).visit(Mockito.any(Step.class));

        InOrder inOrder = inOrder(visitor);
        inOrder.verify(visitor).visit(step5);
        inOrder.verify(visitor).visit(step4);
        inOrder.verify(visitor).visit(step3);
        inOrder.verify(visitor).visit(step2);
        inOrder.verify(visitor).visit(step1);
        inOrder.verify(visitor).visit(step0);


    }

    /**
     * Testing method written to assert the correctness of the implementation of the "accept()"
     * method in the Step. In this case testing behaviour for an empty step.
     * Tested by creating a Mock visitor and checking the order of visited steps.
     */
    @Test
    void acceptEmptySteps(){
        Step step = new Step(null,null,null,null,null);
        Visitor visitor = mock(Visitor.class);
        step.accept(visitor);
        verify(visitor, times(1)).visit(Mockito.any(Step.class));
    }
}