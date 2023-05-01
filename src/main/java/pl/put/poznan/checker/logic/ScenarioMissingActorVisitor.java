package pl.put.poznan.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class ScenarioMissingActorVisitor {
    public class NoActorStepVisitor implements Visitor {
        private List<Step> noActorSteps = new ArrayList<>();
        private List<Integer> missingActorsIndexes = new ArrayList<>();
        private int currentIndex=0;

        public void visit(Scenario scenario) {
        }

        public void visit(Step step) {
            currentIndex+=1;
            if (step.getActor() == null) {
                noActorSteps.add(step);
                missingActorsIndexes.add(currentIndex);
            }
        }

        public List<Step> getNoActorSteps() {
            return noActorSteps;
        }

        public List<Integer> getMissingActorsIndexes(){
            return missingActorsIndexes;
        }
    }
}
