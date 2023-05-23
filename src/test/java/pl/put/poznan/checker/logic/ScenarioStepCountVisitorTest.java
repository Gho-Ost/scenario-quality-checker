package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioStepCountVisitorTest {

    @Test
    void visit() {
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

        ScenarioStepCountVisitor visitor = new ScenarioStepCountVisitor();

        scenario.accept(visitor);
        assertEquals(10, visitor.getStepCount());

    }

    @Test
    void twoVisits(){
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

        ScenarioStepCountVisitor visitor = new ScenarioStepCountVisitor();

        scenario.accept(visitor);
        scenario.accept(visitor);

        assertEquals(10, visitor.getStepCount());
    }
}