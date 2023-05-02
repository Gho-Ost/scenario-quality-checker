package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

//TODO: Test and review the implementation
public class ScenarioKeyWordCountVisitor implements Visitor{
    private int keyWordCount=0;

    public void visit(Scenario scenario) {
    }

    public void visit(Step step) {
        if(step.getKeyword()!=null){
            keyWordCount++;
        }
    }

    public int getKeyWordCount() {
        return keyWordCount;
    }
}
