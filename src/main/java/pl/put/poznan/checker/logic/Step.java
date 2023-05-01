package pl.put.poznan.checker.logic;

import java.util.ArrayList;

// TEST template for filling steps - idk if this automatic parsing can even work like that
public class Step implements Visitable{
    private String actor;
    private String keyword;
    private String action;
    private ArrayList<Step> substeps;

    public Step(String actor, String keyword, String action, ArrayList<Step> substeps) {
        this.actor = actor;
        this.keyword = keyword;
        this.action = action;
        this.substeps = substeps;
    }

    public void printSubsteps(){
        if (this.substeps != null){
            for (Step step : this.substeps){
                System.out.println("========NEXT SUBSTEP=======");
                System.out.println("Actor: " + step.getActor());
                System.out.println("Keyword: " + step.getKeyword());
                System.out.println("Action: " + step.getAction());
                step.printSubsteps();
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

    public void setSubsteps(ArrayList<Step> substeps) {
        this.substeps = substeps;
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
        if(substeps!=null){
            visitor.incrementDepth();
            for(Step step: substeps){
                step.accept(visitor);
            }
            visitor.decrementDepth();
        }
    }
}
