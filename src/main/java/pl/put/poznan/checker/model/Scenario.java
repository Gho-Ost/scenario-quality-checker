package pl.put.poznan.checker.model;

import pl.put.poznan.checker.logic.Visitable;
import pl.put.poznan.checker.logic.Visitor;

import java.util.ArrayList;

/**
 * Class representing the Scenario to be evaluated. It is an implementation of the
 * Visitable interface and as such it can be visited by Visitor implementations as
 * a part of Visitor design pattern. This class is meant to contain the Scenario's
 * title, present actors, systemActor and steps present in the scenario. It also
 * allows for implementation of keywords within the Scenario such as: "IF", "ELSE" and "FOR EACH".
 */
public class Scenario implements Visitable {
    private String title;
    private String[] actors;
    private String systemActor;
    private ArrayList<Step> steps;

    /**
     * Constructs a new Scenario object with the specified title, actors, system actor, and steps.
     *
     * @param title String title of the scenario
     * @param actors an array Strings, representing actors in the scenario
     * @param systemActor String system actor in the scenario
     * @param steps an ArrayList of Step objects representing the steps in the scenario
     */
    public Scenario(String title, String[] actors, String systemActor, ArrayList<Step> steps) {
        this.title = title;
        this.actors = actors;
        this.systemActor = systemActor;
        this.steps = steps;
    }

    /**
     * Accepts a Visitor object and allows it to visit this Scenario object and its Step objects.
     * Implementation required by the Visitable() interface.
     *
     * @param visitor the Visitor object to be accepted
     */
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if(steps != null){
            for(Object step: steps){
                ((Step)step).accept(visitor);
            }
        }
    }

    /**
     * Returns the ArrayList of Step objects in this Scenario object.
     *
     * @return the ArrayList of Step objects in this Scenario object
     */
    public ArrayList<Step> getSteps() {
        return steps;
    }

    /**
     * Allows for setting the ArrayList of Step objects.
     *
     * @param steps the new ArrayList of Step objects
     */
    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    /**
     * Prints the scenario, more specifically the Title, Actors and System actors
     * and the steps present in the Scenario. Calls on the printStep() method in the
     * Step class to perform the printing of the steps.
     */
    public void printScenario(){
        System.out.println("Title: " + this.title);
        for (String actor : this.actors) {
            System.out.println("Actor: " + actor);
        }
        System.out.println("System actor: " + this.systemActor);

        if (this.steps != null){
            for (Step step : this.steps){
                step.printStep();
            }
        }
    }

    /**
     * Returns the title of this Scenario object.
     *
     * @return the title of this Scenario object
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this Scenario object.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns an array of actors in this Scenario object.
     *
     * @return an array of actors in this Scenario object
     */
    public String[] getActors() {
        return actors;
    }

    /**
     * Sets the array of actors in this Scenario object.
     *
     * @param actors the new array of actors
     */
    public void setActors(String[] actors) {
        this.actors = actors;
    }

    /**
     * Returns the system actor in this Scenario object.
     *
     * @return the system actor in this Scenario object
     */
    public String getSystemActor() {
        return systemActor;
    }

    /**
     * Sets the system actor in this Scenario object.
     *
     * @param systemActor the new system actor
     */
    public void setSystemActor(String systemActor) {
        this.systemActor = systemActor;
    }
}
