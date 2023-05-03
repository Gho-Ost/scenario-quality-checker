package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

/**
 * Class Responsible for finding and counting steps containing keyWords in accordance with
 * implementation of the Visitor design pattern. Contains private field
 * as a means of counting the number of steps containing the keyWords.
 * <p>Example usage of the class to count the number of keyWords
 * for some previously created Scenario class instance called my_scenario:
 * <pre>
 * {@code
 * ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
 * my_scenario.accept(visitor);
 * int result = visitor.getKeyWordCount();
 *  }
 * </pre>
 */
public class ScenarioKeyWordCountVisitor implements Visitor{
    private int keyWordCount=0;

    /**
     * Method for visiting Scenario class instances, performs no action since
     * keyWords are present at the level of steps.
     * @param scenario Scenario to be visited
     */
    public void visit(Scenario scenario) {
    }

    /**
     * Method for visiting Steps. For every step where the keyWord is not equal to
     * null, the count increases.
     * @param step
     */
    public void visit(Step step) {
        if(step.getKeyword()!=null){
            keyWordCount++;
        }
    }

    /**
     * Returns the number of found keyWords
     * @return keyWordCount
     */
    public int getKeyWordCount() {
        return keyWordCount;
    }
}
