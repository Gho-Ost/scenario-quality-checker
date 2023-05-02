package pl.put.poznan.checker.model;

import pl.put.poznan.checker.logic.Visitable;
import pl.put.poznan.checker.logic.Visitor;

import java.util.ArrayList;

public class Scenario implements Visitable {
    private String title;
    private String[] actors;
    private String systemActor;
    private ArrayList<Step> steps;

    public Scenario(String title, String[] actors, String systemActor, ArrayList<Step> steps) {
        this.title = title;
        this.actors = actors;
        this.systemActor = systemActor;
        this.steps = steps;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
        if(steps != null){
            for(Object step: steps){
                ((Step)step).accept(visitor);
            }
        }
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void printScenario(){
        System.out.println("Title: " + this.title);
        for (String actor : this.actors) {
            System.out.println("Actor: " + actor);
        }
        System.out.println("System actor: " + this.systemActor);

        for (Step step : this.steps){
            step.printStep();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    public String getSystemActor() {
        return systemActor;
    }

    public void setSystemActor(String systemActor) {
        this.systemActor = systemActor;
    }
}
