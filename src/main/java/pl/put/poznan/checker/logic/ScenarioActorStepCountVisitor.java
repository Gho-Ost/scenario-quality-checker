package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

public class ScenarioActorStepCountVisitor implements Visitor {

    private String selectedActor;
    private int actorStepCount=0;

    public ScenarioActorStepCountVisitor(String selectedActor) {
        this.selectedActor = selectedActor;
    }

    /**
     * @param scenario - Scenario to be visited
     */
    @Override
    public void visit(Scenario scenario) {

    }

    /**
     * @param step
     */
    @Override
    public void visit(Step step) {
        if (step.getActor() != null && step.getActor().equals(this.selectedActor)){
            actorStepCount ++;
        }
    }

    public int getActorStepCount() {
        return actorStepCount;
    }
}
