package pl.put.poznan.checker.logic;

import java.util.ArrayList;

//TODO: Test and review this implementation
public class ScenarioLevelVisitor implements Visitor{
    private int currentLevel = 1;
    private int maxLevel=1;
    private Scenario scenario;
    private ArrayList<Step> steps;

    public void setMaxLevel(int maxLevel){
        this.maxLevel=maxLevel;
    }

    public void visit(Scenario scenario){
        this.scenario.setTitle(scenario.getTitle());
        this.scenario.setActors(scenario.getActors());
        this.scenario.setSystemActor(scenario.getSystemActor());
    }

    public void visit(Step step){
        if(this.currentLevel<=this.maxLevel){
            if(this.currentLevel+1>this.maxLevel){
                step.setSubsteps(null);
            }
            this.steps.add(step);
        }
    }

    public Scenario getScenario(){
        scenario.setSteps(this.steps);
        return scenario;
    }

    public void incrementDepth(){this.currentLevel+=1;}

    public void decrementDepth(){this.currentLevel-=1;}
}
