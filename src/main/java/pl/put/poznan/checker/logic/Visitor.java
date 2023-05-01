package pl.put.poznan.checker.logic;

public interface Visitor {
    void incrementDepth();
    void decrementDepth();
    void visit(Scenario scenario);
    void visit(Step step);
}
