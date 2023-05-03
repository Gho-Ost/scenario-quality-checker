package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

/**
 * Class implemented as part of the Visitor design pattern. Includes 2 methods visit()
 * which takes Scenario as argument and visit() which takes Step as an argument. This interface
 * is meant allow to provide the Visitor with the means of performing operations when
 * visiting the class implementing Visitable interface and to be accepted by those class
 * instances when attempting to visit.
 */
public interface Visitor {
    /**
     * Responsible for performing operations when visiting a Scenario
     * object instance by class object instance implementing Visitor interface.
     * @param scenario - Scenario to be visited
     */
    void visit(Scenario scenario);

    /**
     * Responsible for performing operations when visiting a Step
     * object instance by class object instance implementing Visitor interface.
     * @param step
     */
    void visit(Step step);
}
