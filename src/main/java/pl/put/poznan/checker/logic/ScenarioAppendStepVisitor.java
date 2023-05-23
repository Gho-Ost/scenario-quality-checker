package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;

/**
 * Class responsible for adding a step to the end of a Scenario by the means of
 * implementing the Visitor design pattern with the use of Visitor interface.
 * The class is meant to modify an existing Scenario. Here is an example code,
 * with the assumption that my_scenario is an already existing Scenario and
 * newStep1 is some predefined Step to be added:
 *  <pre>
 *  {@code
 *  ScenarioAppendStepVisitor visitor = new ScenarioAppendStepVisitor(newStep1);
 *  my_scenario.accept(visitor);
 *  visitor.getScenario();
 *   }
 *  </pre>
 */
public class ScenarioAppendStepVisitor implements Visitor {
    private Scenario scenario;
    private ArrayList<Step> steps;
    private Step newStep;

    /**
     * Constructor for ScenarioAppendStepVisitor class. Takes one argument, an instance of Step class.
     * @param newStep (Step) Step to be added at the end of the Scenario.
     */
    public ScenarioAppendStepVisitor(Step newStep) {
        this.newStep = newStep;
    }

    /**
     * Visit method implemented on the level of Scenario as part of the Visitor design pattern,
     * required due to implementation of Visitor interface.
     * @param scenario Scenario to be visited
     */
    @Override
    public void visit(Scenario scenario) {
        this.scenario = scenario;
        this.steps = this.scenario.getSteps();
        this.steps.add(this.newStep);
        scenario.setSteps(this.steps);
    }

    /** Visit method implemented on the level of a Step as part of the Visitor design pattern,
     * required due to implementation of Visitor interface.
     * @param step
     */
    @Override
    public void visit(Step step) {
    }

    /**
     * Getter, returns modified Scenario with the additional specified Step append at the end.
     * @return (Scenario) Scenario modified by the Visitor
     */
    public Scenario getScenario(){
        return scenario;
    }
}
