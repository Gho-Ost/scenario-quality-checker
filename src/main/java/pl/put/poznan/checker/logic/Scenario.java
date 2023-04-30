package pl.put.poznan.checker.logic;

import java.util.ArrayList;

public class Scenario {
    private String title;
    private String[] actors;
    private String systemActor;

    // TEST steps as list of step objects idk if this would work
    private ArrayList<Step> steps;

    public Scenario(String title, String[] actors, String systemActor, ArrayList<Step> steps) {
        this.title = title;
        this.actors = actors;
        this.systemActor = systemActor;
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

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }


}
