package pl.put.poznan.checker.logic;

import java.util.ArrayList;

public class Scenario implements Visitable{
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
        //TODO: Implement further logic here
        visitor.visit(this);
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
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
