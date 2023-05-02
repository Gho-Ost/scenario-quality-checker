package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

//TODO change to new step level format
public class ScenarioTextDownloadVisitor implements Visitor{
    @Override
    public void visit(Scenario scenario) {
        String title = scenario.getTitle();
        String[] actors = scenario.getActors();
        String systemActor = scenario.getSystemActor();

        System.out.println("Scenario TEXT");
        System.out.println("Title: " + title);
        System.out.println("Actor: " + actors);
        System.out.println("System actor: " + systemActor);
        System.out.println("Steps:");
    }

    @Override
    public void visit(Step step) {
        String stepLevel = step.getStepLevel();
        String keyword = step.getKeyword();
        String actor = step.getActor();

        System.out.print(String.format("%" + stepLevel.length()*2 + "s", stepLevel));

        if (keyword != null){
            System.out.print(" " + keyword);
        }
        if (actor != null){
            System.out.print(" " + actor);
        }
        System.out.println(" " + step.getAction());
    }
}
