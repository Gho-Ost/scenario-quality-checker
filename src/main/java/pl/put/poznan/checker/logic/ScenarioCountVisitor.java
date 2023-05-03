package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

/**
 * Class Responsible for finding and counting steps present in the Scenario in accordance with
 * implementation of the Visitor design pattern. Contains private field
 * as a means of counting the number of steps visited so far.
 *
 * <p>Example usage of the class to count the number of steps
 * for some previously created Scenario class instance called my_scenario:
 * <pre>
 * {@code
 * ScenarioCountVisitor visitor = new ScenarioCountVisitor();
 * my_scenario.accept(visitor);
 * int result = visitor.getStepCount();
 *  }
 * </pre>
 */
public class ScenarioCountVisitor implements Visitor{
    private int stepCount=0;

    /**
     * Method for visiting Scenario class instances, performs no action since
     * steps are counted at the Step level
     * @param scenario Scenario to be visited
     */
    public void visit(Scenario scenario) {
    }

    /**
     * Method for visiting Steps. For every step visited the counter for the number
     * of steps gets incremented
     * @param step Step to be visited
     */
    public void visit(Step step) {
        stepCount++;
    }

    /**
     * Returns the number of steps found so far.
     * @return stepCount
     */
    public int getStepCount() {
        return stepCount;
    }

}
