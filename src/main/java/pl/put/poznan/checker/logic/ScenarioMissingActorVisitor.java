package pl.put.poznan.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class ScenarioMissingActorVisitor {
    public class NoActorStepVisitor implements Visitor {
        private List<Step> noActorSteps = new ArrayList<>();

        public void visit(Scenario scenario) {
        }

        public void visit(Step step) {
            if (step.getActor() == null) {
                noActorSteps.add(step);
            }
        }

        public List<Step> getNoActorSteps() {
            return noActorSteps;
        }
    }
}
