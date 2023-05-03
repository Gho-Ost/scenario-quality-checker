package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Responsible for finding steps with missing actors in accordance with
 * implementation of the Visitor design pattern. Contains a private field
 * as a means of keeping track of the discovered steps with no actors. The field
 * is implemented as a List of instances of "Step" class.
 * <p>Example usage of the class to retrieve all steps with missing Actors for
 * some previously created Scenario called my_scenario:
 * <pre>
 * {@code
 * ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
 * my_scenario.accept(visitor);
 * List<Step> result = visitor.getNoActorSteps();
 *  }
 * </pre>
 */
public class ScenarioMissingActorVisitor implements Visitor{
    private List<Step> noActorSteps = new ArrayList<>();

    /**
     * Method responsible for performing the visit() operation on the
     * Scenario level. Performs no action, as the necessary data regarding lack of actors
     * is found at the Step level.
     * @param scenario Scenario to be visited
     */
    public void visit(Scenario scenario) {}

    /**
     * Method responsible for performing the visit() operation on the
     * Step level. For every step checks if the actor field is equal to null,
     * except steps containing ELSE and FOR EACH key-words, as those
     * by definition do not require an actor.
     * @param step Step to be visited
     */
    public void visit(Step step) {
        //ELSE and FOR EACH key-words by definition do not require an actor immediately afterwords
        if (step.getActor() == null) {
            if(step.getKeyword()==null ||
                    (!step.getKeyword().equals("ELSE") && !step.getKeyword().equals("FOR EACH"))){
                noActorSteps.add(step);
            }
        }
    }

    /**
     * Returns the found steps without a valid actor.
     * @return noActorSteps - List of Step objects containing steps without an actor.
     */
    public List<Step> getNoActorSteps() {
        return noActorSteps;
    }
}
