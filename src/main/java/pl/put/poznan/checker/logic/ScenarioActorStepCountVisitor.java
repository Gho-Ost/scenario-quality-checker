package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

/**
 * Class responsible for counting the number steps performed by a given actor for a given
 * Scenario. The class is implemented as an example of the Visitor design pattern through
 * the Visitor interface. The class is meant to calculate the number of steps performed
 * by a given actor but the appropriate method will return 0 in case the actor name is not
 * valid or the actor performed no steps. Here is exemplary code assuming the Scenario is
 * called my_scenario and the chosen actor is called "Librarian":
 *  <pre>
 *  {@code
 *  ScenarioActorStepCountVisitor visitor = new ScenarioActorStepCountVisitor("Librarian");
 *  my_scenario.accept(visitor);
 *  visitor.getActorStepCount();
 *   }
 *  </pre>
 */
public class ScenarioActorStepCountVisitor implements Visitor {

    private String selectedActor;
    private int actorStepCount=0;

    /**
     * Constructor for ScenarioActorStepCountVisitor class. Takes one String argument.
     * @param selectedActor (String) name of the actor whose steps we want to count
     */
    public ScenarioActorStepCountVisitor(String selectedActor) {
        this.selectedActor = selectedActor;
    }

    /**
     * Visit method implemented on the level of Scenario as part of the Visitor design pattern,
     * required due to implementation of Visitor interface.
     * @param scenario Scenario to be visited
     */
    @Override
    public void visit(Scenario scenario) {
        actorStepCount = 0;
    }

    /** Visit method implemented on the level of a Step as part of the Visitor design pattern,
     * required due to implementation of Visitor interface.
     * @param step
     */
    @Override
    public void visit(Step step) {
        if (step.getActor() != null && step.getActor().equals(this.selectedActor)){
            actorStepCount ++;
        }
    }

    /**
     * Setter making it possible to select an actor based on String value specifying
     * the actor's name.
     * @param selectedActor (String) name of the actor whose steps are meant to be counted
     */
    public void setSelectedActor(String selectedActor) {
        this.selectedActor = selectedActor;
    }

    /**
     * Getter returning the number of steps found by the Visitor for the particular actor,
     * set in the constructor or by using the setSelectedActor() method.
     * @return (Integer) number of steps found by the Visitor
     */
    public int getActorStepCount() {
        return actorStepCount;
    }
}
