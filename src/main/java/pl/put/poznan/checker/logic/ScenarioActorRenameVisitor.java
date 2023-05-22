package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MODIFIES EXISTING SCENARIO
 */
public class ScenarioActorRenameVisitor implements Visitor {
    private String oldActor;
    private String newActor;


    private Scenario scenario;
    private ArrayList<Step> steps;

    public ScenarioActorRenameVisitor(String oldActor, String newActor) {
        this.oldActor = oldActor;
        this.newActor = newActor;
    }

    /**
     * @param scenario - Scenario to be visited
     */
    @Override
    public void visit(Scenario scenario) {
        this.scenario = scenario;

        String[] oldActors = this.scenario.getActors();
        String[] newActors = new String[oldActors.length];
        for (int i = 0; i < oldActors.length; i++){
            if(oldActors[i].equals(this.oldActor)) {
                newActors[i] = this.newActor;
            }
            else{
                newActors[i] = oldActors[i];
            }
        }
        this.scenario.setActors(newActors);
    }

    /**
     * @param step
     */
    @Override
    public void visit(Step step) {
        if (step.getActor() != null && step.getActor().equals(oldActor)){
            step.setActor(newActor);
        }
    }


    public Scenario getScenario() {
        return scenario;
    }
}
