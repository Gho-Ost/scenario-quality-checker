package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioDeleteStepVisitorTest {
    Scenario scenario;
    Step step3_3_2;
    Step step3_3_1;
    Step step3_3;
    Step step3_2;
    Step step3_1;
    Step step3;
    Step step2_2;
    Step step2_1;
    Step step2;
    Step step1;

    @BeforeEach
    void setUp(){
        step3_3_2 = new Step("actor10", "keyword10", "action10", null, "3.3.2");
        step3_3_1 = new Step("actor9", "keyword9", "action9", null, "3.3.1");
        step3_3 = new Step("actor8", "keyword8", "action8", new ArrayList<>(Arrays.asList(step3_3_1, step3_3_2)), "3.3");
        step3_2 = new Step("actor7", "keyword7", "action7", null, "3.2");
        step3_1 = new Step("actor6", "keyword6", "action6", null, "3.1");
        step3 = new Step("actor5", "keyword5", "action5", new ArrayList<>(Arrays.asList(step3_1, step3_2, step3_3)), "3");
        step2_2 = new Step("actor4", "keyword4", "action4", null, "2.2");
        step2_1 = new Step("actor3", "keyword3", "action3", null, "2.1");
        step2 = new Step("actor2", "keyword2", "action2", new ArrayList<>(Arrays.asList(step2_1, step2_2)), "2");
        step1 = new Step("actor1", "keyword1", "action1", null, "1");
        scenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step1, step2, step3)));

    }

    @Test
    void visitFirstLevel() {
        ScenarioDeleteStepVisitor visitor = new ScenarioDeleteStepVisitor("1");

        scenario.accept(visitor);

        ArrayList<Step> expectedList = new ArrayList<>(Arrays.asList(step2, step3));
        ArrayList<Step> actualList = visitor.getScenario().getSteps();

        assertEquals("1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNotNull(actualList.get(0).getSubsteps());

        assertEquals("2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNotNull(actualList.get(1).getSubsteps());

        expectedList = new ArrayList<>(Arrays.asList(step2_1, step2_2));
        actualList = visitor.getScenario().getSteps().get(0).getSubsteps();

        assertEquals("1.1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("1.2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNull(actualList.get(1).getSubsteps());

        expectedList = new ArrayList<>(Arrays.asList(step3_1, step3_2, step3_3));
        actualList = visitor.getScenario().getSteps().get(1).getSubsteps();

        assertEquals("2.1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("2.2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNull(actualList.get(1).getSubsteps());

        assertEquals("2.3", actualList.get(2).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNotNull(actualList.get(2).getSubsteps());

        expectedList = new ArrayList<>(Arrays.asList(step3_3_1, step3_3_2));
        actualList = visitor.getScenario().getSteps().get(1).getSubsteps().get(2).getSubsteps();

        assertEquals("2.3.1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("2.3.2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNull(actualList.get(1).getSubsteps());

    }

    @Test
    void visitFirstLevelWithChildren(){
        ScenarioDeleteStepVisitor visitor = new ScenarioDeleteStepVisitor("3");

        scenario.accept(visitor);

        ArrayList<Step> expectedList = new ArrayList<>(Arrays.asList(step1, step2));
        ArrayList<Step> actualList = visitor.getScenario().getSteps();

        assertEquals("1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNotNull(actualList.get(1).getSubsteps());

        expectedList = new ArrayList<>(Arrays.asList(step2_1, step2_2));
        actualList = visitor.getScenario().getSteps().get(1).getSubsteps();

        assertEquals("2.1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("2.2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNull(actualList.get(1).getSubsteps());
    }

    @Test
    void visitSubstep(){
        ScenarioDeleteStepVisitor visitor = new ScenarioDeleteStepVisitor("3.3");

        scenario.accept(visitor);

        ArrayList<Step> expectedList = new ArrayList<>(Arrays.asList(step1, step2, step3));
        ArrayList<Step> actualList = visitor.getScenario().getSteps();

        assertEquals("1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNotNull(actualList.get(1).getSubsteps());

        expectedList = new ArrayList<>(Arrays.asList(step2_1, step2_2));
        actualList = visitor.getScenario().getSteps().get(1).getSubsteps();

        assertEquals("2.1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("2.2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNull(actualList.get(1).getSubsteps());

        expectedList = new ArrayList<>(Arrays.asList(step3_1, step3_2));
        actualList = visitor.getScenario().getSteps().get(2).getSubsteps();

        assertEquals("3.1", actualList.get(0).getStepLevel());
        assertEquals(expectedList.get(0).getActor(), actualList.get(0).getActor());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertEquals(expectedList.get(0).getKeyword(), actualList.get(0).getKeyword());
        assertNull(actualList.get(0).getSubsteps());

        assertEquals("3.2", actualList.get(1).getStepLevel());
        assertEquals(expectedList.get(1).getActor(), actualList.get(1).getActor());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertEquals(expectedList.get(1).getKeyword(), actualList.get(1).getKeyword());
        assertNull(actualList.get(1).getSubsteps());
    }
}