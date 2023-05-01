package pl.put.poznan.checker.logic;

public class ScenarioLevelVisitor implements Visitor{
    private int currentLevel = 1;
    private int maxLevel=1;
    private Scenario scenario;

    public void setMaxLevel(int maxLevel){
        this.maxLevel=maxLevel;
    }

    public void visit(Scenario scenario){
        this.scenario.setTitle(scenario.getTitle());
        this.scenario.setActors(scenario.getActors());
        this.scenario.setSystemActor(scenario.getSystemActor());
    }

    public void visit(Step step){
    }

    public Scenario getScenario(){
        return scenario;
    }

    public void incrementDepth(){this.currentLevel+=1;}

    public void decrementDepth(){this.currentLevel-=1;}
}
