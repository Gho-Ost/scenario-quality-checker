package pl.put.poznan.checker.logic;

import java.util.ArrayList;

//TODO: Test and review this implementation
public class ScenarioLevelVisitor implements Visitor{
    private int maxLevel=1;
    private Scenario scenario;
    private ArrayList<Step> steps;

    public ScenarioLevelVisitor(int maxLevel){
        this.maxLevel = maxLevel;
    }

    public ScenarioLevelVisitor(){
    }

    public void setMaxLevel(int maxLevel){
        this.maxLevel=maxLevel;
    }

    public void visit(Scenario scenario){
        this.scenario = new Scenario(scenario.getTitle(), scenario.getActors(),
                scenario.getSystemActor(), null);
        this.steps = new ArrayList<Step>();
    }

    public void visit(Step step){
        int currentLevel=step.getStepLevel().split("\\.").length;
        if(currentLevel<=this.maxLevel){
            if(currentLevel+1>this.maxLevel){
                step.setSubsteps(null);
            }
            this.steps.add(step);
        }
    }

    public Scenario getScenario(){
        scenario.setSteps(this.steps);
        return scenario;
    }
}