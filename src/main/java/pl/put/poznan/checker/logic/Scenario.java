package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;

public class Scenario implements Visitable{
    private String title;
    private String[] actors;
    private String systemActor;

    // TEST steps as list of step objects idk if this would work
    private ArrayList<Object> steps;

    public Scenario(String title, String[] actors, String systemActor, ArrayList<Object> steps) {
        this.title = title;
        this.actors = actors;
        this.systemActor = systemActor;
        this.steps = steps;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
        for(Object step: steps){
            ((Step)step).accept(visitor);
        }
    }

    public ArrayList<Object> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Object> steps) {
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
