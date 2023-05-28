package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class implemented to test ScenarioLevelVisitor class
 * functionality with the use of unit tests performed with JUnit library.
 */
public class ScenarioLevelVisitorTest {
    private Scenario testScenario;
    private int maxDepth=-999;
    private int minDepth=999;

    private void getMaxSubsteps(ArrayList<Step> steps){
        for(Step testStep: steps){
            if(testStep.getStepLevel().length()>=maxDepth){
                this.maxDepth=testStep.getStepLevel().length();
            }
            if(testStep.getSubsteps()!=null){
                getMaxSubsteps(testStep.getSubsteps());
            }
        }
    }

    private void getMinSubsteps(ArrayList<Step> steps){
        for(Step testStep: steps){
            if(testStep.getStepLevel().length()<=minDepth){
                this.minDepth=testStep.getStepLevel().length();
            }
            if(testStep.getSubsteps()!=null){
                getMaxSubsteps(testStep.getSubsteps());
            }
        }
    }

    /**
     * Method responsible for setting up the appropriate variables for future tests.
     * In this case the method creates a test Scenario that the future tests will use to
     * evaluate if the tested implementations are working correctly.
     */
    @BeforeEach
    void setUp(){
        Step step2_3_2 = new Step("actor0", "keyword0", "action0", null, "2.3.2");
        Step step2_3_1 = new Step("actor0", "keyword0", "action0", null, "2.3.1");
        Step step2_3 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_3_1, step2_3_2)), "2.3");
        Step step2_2 = new Step("actor1", "keyword0", "action0", null, "2.2");
        Step step2_1 = new Step("actor2", "keyword0", "action0", null, "2.1");
        Step step2 = new Step(null, "keyword0", "action0", new ArrayList<>(Arrays.asList(step2_1, step2_2, step2_3)), "2");
        Step step1_2 = new Step("actor2", "keyword0", "action0", null, "1.2");
        Step step1_1 = new Step("actor2", "keyword0", "action0", null, "1.1");
        Step step1 = new Step("actor0", "keyword0", "action0", new ArrayList<>(Arrays.asList(step1_1, step1_2)), "1");
        Step step0 = new Step("actor0", "keyword0", "action0", null, "0");
        testScenario = new Scenario("scenario0", new String[]{"actor0, actor1"}, "actor2",new ArrayList<>(Arrays.asList(step0, step1, step2)));
    }

    /**
     * Test for the visit() method of the ScenarioLevelVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately truncates a Scenario according to specified depth.
     * In this case, the default depth of one is being tested.
     */
    @Test
    void visitDepthOne(){
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor();
        testScenario.accept(visitor);
        Scenario newScenario = visitor.getScenario();
        for(Step testStep: newScenario.getSteps()){
            assertTrue(testStep.getStepLevel().length()==1);
        }
    }

    /**
     * Test for the visit() method of the ScenarioLevelVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately truncates a Scenario according to specified depth.
     * In this case, the depth of two is being tested by setting it manually.
     */
    @Test
    void visitDepthTwo() {
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(2);
        testScenario.accept(visitor);
        Scenario newScenario = visitor.getScenario();
        for (Step testStep : newScenario.getSteps()) {
            assertTrue(testStep.getStepLevel().length() >= 1);
            assertTrue(testStep.getStepLevel().length() <= 3);
            if (testStep.getSubsteps() != null) {
                this.maxDepth = -999;
                this.minDepth = 999;
                this.getMaxSubsteps(testStep.getSubsteps());
                this.getMinSubsteps(testStep.getSubsteps());
                assertTrue(this.maxDepth <= 3);
                assertTrue(this.minDepth >= 1);
            }
        }
    }

    /**
     * Test for the visit() method of the ScenarioLevelVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately truncates a Scenario according to specified depth.
     * In this case, the depth of zero is being selected by setting it
     * manually.
     */

    @Test
    void visitDepthZero(){
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(0);
        testScenario.accept(visitor);
        Scenario newScenario = visitor.getScenario();
        assertEquals(0,newScenario.getSteps().size());
    }

    /**
     * Test for the visit() method of the ScenarioLevelVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately truncates a Scenario according to specified depth.
     * In this case, the depth deeper than any substep in a tested Scenario
     * is being selected by setting it manually.
     */
    @Test
    void visitDepthDeeperThanMaximum(){
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(4);
        testScenario.accept(visitor);
        Scenario newScenario = visitor.getScenario();
        for(Step testStep: newScenario.getSteps()){
            assertTrue(testStep.getStepLevel().length()>=1);
            assertTrue(testStep.getStepLevel().length()<=5);
            if(testStep.getSubsteps()!=null){
                this.maxDepth=-999;
                this.minDepth=999;
                this.getMaxSubsteps(testStep.getSubsteps());
                this.getMinSubsteps(testStep.getSubsteps());
                assertTrue(this.maxDepth<=5);
                assertTrue(this.minDepth>=1);
            }
        }
    }

    /**
     * Test for the visit() method of the ScenarioLevelVisitor class
     * with the assertion testing if the implemented visitor design pattern
     * appropriately truncates a Scenario according to specified depth.
     * In this case, a negative depth is being selected by
     * setting it manually.
     */
    @Test
    void visitDepthNegative(){
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(-1);
        testScenario.accept(visitor);
        Scenario newScenario = visitor.getScenario();
        assertEquals(0,newScenario.getSteps().size());
    }

}
