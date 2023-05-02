package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

//TODO change to new step level format
public class ScenarioTextDownloadVisitor implements Visitor {
    private StringBuilder result = new StringBuilder();

    public String getResult() {
        return result.toString();
    }

    @Override
    public void visit(Scenario scenario) {
        String title = scenario.getTitle();
        String[] actors = scenario.getActors();
        String systemActor = scenario.getSystemActor();

        result.append("Title: ").append(title).append("\n");
        result.append("Actor: ").append(actors).append("\n");
        result.append("System actor: ").append(systemActor).append("\n");
        result.append("Steps:\n");
    }

    @Override
    public void visit(Step step) {
        String stepLevel = step.getStepLevel();
        String keyword = step.getKeyword();
        String actor = step.getActor();

        result.append(String.format("%" + stepLevel.length()*2 + "s", stepLevel));

        if (keyword != null){
            result.append(" ").append(keyword);
        }
        if (actor != null){
            result.append(" ").append(actor);
        }
        result.append(" ").append(step.getAction()).append("\n");
    }
}
