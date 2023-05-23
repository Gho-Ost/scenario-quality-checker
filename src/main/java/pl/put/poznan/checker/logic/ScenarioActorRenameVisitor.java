package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class Responsible for renaming an actor according to a new name
 * specified by the user in accordance with
 * implementation of the Visitor design pattern.In case the name of the actor is invalid,
 * nothing will be returned.
 * <p>Example usage of the class to rename an actor 'Librarian' to
 * 'Employee' assuming the existence of a scenario with such actors
 * called my_scenario.:
 * <pre>
 * {@code
 * ScenarioActorRenameVisitor visitor = new ScenarioActorRenameVisitor("Librarian","Employee");
 * my_scenario.accept(visitor);
 * visitor.getScenario();
 *  }
 * </pre>
 */
public class ScenarioActorRenameVisitor implements Visitor {
    private String oldActor;
    private String newActor;


    private Scenario scenario;
    private ArrayList<Step> steps;

    /**
     * Constructor for the ScenarioActorVisitor class, takes two arguments.
     * @param oldActor (String) the name of the actor to be modified
     * @param newActor (String) the name of the actor to be modified to
     */
    public ScenarioActorRenameVisitor(String oldActor, String newActor) {
        this.oldActor = oldActor;
        this.newActor = newActor;
    }

    /**
     * Visit method implemented on the level of Scenario as part of the Visitor design pattern,
     * required due to implementation of Visitor interface.
     * @param scenario Scenario to be visited
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

    /** Visit method implemented on the level of a Step as part of the Visitor design pattern,
     * required due to implementation of Visitor interface.
     * @param step
     */
    @Override
    public void visit(Step step) {
        if (step.getActor() != null && step.getActor().equals(oldActor)){
            step.setActor(newActor);
        }
    }


    /**
     * Method to get the modified scenario
     * @return (Scenario) Scenario modified by the Visitor
     */
    public Scenario getScenario() {
        return scenario;
    }
}
