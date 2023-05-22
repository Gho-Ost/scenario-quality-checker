package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;

/**
 * MODIFIES EXISTING SCENARIO
 */
public class ScenarioAppendStepVisitor implements Visitor {
    private Scenario scenario;
    private ArrayList<Step> steps;
    private Step newStep;


    public ScenarioAppendStepVisitor(Step newStep) {
        this.newStep = newStep;
    }

    /**
     * @param scenario - Scenario to be visited
     */
    @Override
    public void visit(Scenario scenario) {
        this.scenario = scenario;
        this.steps = this.scenario.getSteps();
        this.steps.add(this.newStep);
        scenario.setSteps(this.steps);
    }

    /**
     * @param step
     */
    @Override
    public void visit(Step step) {
    }

    public Scenario getScenario(){
        return scenario;
    }
}
