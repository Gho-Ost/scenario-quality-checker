package pl.put.poznan.checker.model;

import pl.put.poznan.checker.logic.Visitable;
import pl.put.poznan.checker.logic.Visitor;

import java.util.ArrayList;

// TEST template for filling steps - idk if this automatic parsing can even work like that
public class Step implements Visitable {
    private String actor;
    private String keyword;
    private String action;
    private String stepLevel;

    private ArrayList<Step> substeps;

    public Step(String actor, String keyword, String action, ArrayList<Step> substeps, String stepLevel) {
        this.actor = actor;
        this.keyword = keyword;
        this.action = action;
        this.substeps = substeps;
        this.stepLevel = stepLevel;
    }

    public void printStep(){
        System.out.println("======NEW STEP=======");
        System.out.println("Level: " + this.stepLevel);
        System.out.println("Actor: " + this.actor);
        System.out.println("Keyword: " + this.keyword);
        System.out.println("Action: " + this.action);
        printSubstep(this.substeps);
    }

    private void printSubstep(ArrayList<Step> substeps){
        if (this.substeps != null){
            for (Step step : this.substeps){
                System.out.println("========NEXT SUBSTEP=======");
                System.out.println("Level: " + step.getStepLevel());
                System.out.println("Actor: " + step.getActor());
                System.out.println("Keyword: " + step.getKeyword());
                System.out.println("Action: " + step.getAction());
                step.printSubstep(step.getSubsteps());
            }
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<Step> getSubsteps() {
        return substeps;
    }


    public String getStepLevel() {
        return stepLevel;
    }

    public void setStepLevel(String stepLevel) {
        this.stepLevel = stepLevel;
    }


    public void setSubsteps(ArrayList<Step> substeps) {
        this.substeps = substeps;
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
        if(substeps!=null){
            for(Step step: substeps){
                step.accept(visitor);
            }
        }
    }
}
