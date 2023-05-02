package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

public interface Visitor {
    void visit(Scenario scenario);
    void visit(Step step);
}
