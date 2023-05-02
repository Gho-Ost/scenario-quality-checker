package pl.put.poznan.checker.logic;

import java.util.ArrayList;

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

        printNumberedSteps(scenario.getSteps(), 1, 1);
    }

    private void printNumberedSteps(ArrayList<Step> steps, int parentStep, int level){
        for (Step step : steps){

            ArrayList<Step> substeps = step.getSubsteps();
            if (substeps != null){
                printNumberedSteps(substeps, parentStep, level++);
            }

            parentStep++;
        }
    }

    @Override
    public void visit(Step step) {

    }
}
