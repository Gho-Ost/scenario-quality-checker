package pl.put.poznan.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class ScenarioMissingActorVisitor implements Visitor{
    private List<Step> noActorSteps = new ArrayList<>();

    public void visit(Scenario scenario) {}

    public void visit(Step step) {
        //ELSE and FOR EACH key-words by definition do not require an actor immediately afterwords
        if (step.getActor() == null) {
            if(step.getKeyword()==null || (!step.getKeyword().equals("ELSE") && !step.getKeyword().equals("FOR EACH"))){
                noActorSteps.add(step);
            }
        }
    }

    public List<Step> getNoActorSteps() {
        return noActorSteps;
    }
}
