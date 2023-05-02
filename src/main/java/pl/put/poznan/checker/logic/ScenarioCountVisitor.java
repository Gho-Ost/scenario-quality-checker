package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

public class ScenarioCountVisitor implements Visitor{
    private int stepCount=0;

    public void visit(Scenario scenario) {
    }

    public void visit(Step step) {
        stepCount++;
    }

    public int getStepCount() {
        return stepCount;
    }

}
