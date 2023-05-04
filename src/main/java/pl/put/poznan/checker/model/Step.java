package pl.put.poznan.checker.model;

import pl.put.poznan.checker.logic.Visitable;
import pl.put.poznan.checker.logic.Visitor;

import java.util.ArrayList;

/**
 * Class representing a single step of a Scenario. It can contain the information about the actor
 * performing the step, keyword present in the step, action taken in the step and
 * the level of the step, meaning what step is, if counted linearly as well as depth
 * of the step in case of subscenarios. As a means of implementing subscenarios
 * each step can contain substeps represented in a form of an ArrayList. The class
 * is an implementation of the Visitable interface and part of the
 * Visitor design pattern
 */
public class Step implements Visitable {
    private String actor;
    private String keyword;
    private String action;
    private String stepLevel;

    private ArrayList<Step> substeps;

    /**
     * Constructs a new Step object with the specified actor, keyword, action, substeps, and step level.
     *
     * @param actor String the actor performing the step
     * @param keyword String the keyword associated with the step
     * @param action String the action performed in the step
     * @param substeps ArrayList of Step an ArrayList of Step objects representing the substeps of this step
     * @param stepLevel String the level of this step in the hierarchy of steps.
     *                  Written in the form like for example: "3.5.1.2".
     */
    public Step(String actor, String keyword, String action, ArrayList<Step> substeps, String stepLevel) {
        this.actor = actor;
        this.keyword = keyword;
        this.action = action;
        this.substeps = substeps;
        this.stepLevel = stepLevel;
    }

    /**
     * Constructor for the Step Class, taking step class instance as an argument.
     * Created for the purposes of creating a deep copy of an object.
     * @param step Step taken as an argument, presumably to be copied.
     */
    public Step(Step step){
        this.actor = step.actor;
        this.keyword = step.keyword;
        this.action = step.action;
        this.stepLevel = step.stepLevel;
        this.substeps = new ArrayList<Step>();
        if(step.substeps==null){
            this.substeps=null;
        }
        else {
            for (Step substep : step.substeps) {
                this.substeps.add(new Step(substep));
            }
        }
    }

    /**
     * Prints this Step object and its substeps to the standard output stream.
     */
    public void printStep(){
        System.out.println("======NEW STEP=======");
        System.out.println("Level: " + this.stepLevel);
        System.out.println("Actor: " + this.actor);
        System.out.println("Keyword: " + this.keyword);
        System.out.println("Action: " + this.action);
        printSubstep(this.substeps);
    }

    /**
     * Prints the specified ArrayList of Step objects (substeps) to the standard output stream.
     *
     * @param substeps an ArrayList of Step objects representing the substeps to be printed
     */
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

    /**
     * Returns the action performed in this Step object.
     *
     * @return the action performed in this Step object
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action performed in this Step object.
     *
     * @param action the new action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Returns the actor performing this Step object.
     *
     * @return the actor performing this Step object
     */
    public String getActor() {
        return actor;
    }

    /**
     * Sets the actor performing this Step object.
     *
     * @param actor the new actor
     */
    public void setActor(String actor) {
        this.actor = actor;
    }

    /**
     * Returns the keyword associated with this Step object.
     *
     * @return the keyword associated with this Step object
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets the keyword associated with this Step object.
     *
     * @param keyword the new keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns an ArrayList of Step objects representing the substeps of this Step object.
     *
     * @return an ArrayList of Step objects representing the substeps of this Step object
     */
    public ArrayList<Step> getSubsteps() {
        return substeps;
    }


    /**
     * Returns stepLevel representing both the order of the step determined linearly
     * and depth of the step.
     * @return stepLevel - String representing the order and depth of the step
     * written in the format like for example: "1.3.5" showing in this example
     * the depth of 3 and the 5th step as substep
     * of 3 which is a substep of step 1.
     */
    public String getStepLevel() {
        return stepLevel;
    }

    /**
     * Sets StepLevel representing both the order and the depth of a Step.
     * @param stepLevel String representing the order and depth of the step
     *      * written in the format like for example: "1.3.5" showing in this example
     *      * the depth of 3 and the 5th step as substep
     *      * of 3 which is a substep of step 1.
     */
    public void setStepLevel(String stepLevel) {
        this.stepLevel = stepLevel;
    }


    /**
     * Sets Substeps for the present Step.
     * @param substeps ArrayList of Step objects
     */
    public void setSubsteps(ArrayList<Step> substeps) {
        this.substeps = substeps;
    }

    /**
     * Method for accepting the Visitor class instance at the Step level.
     * @param visitor - Visitor object that can be accepted by class instance
     */
    public void accept(Visitor visitor){
        visitor.visit(this);
        if(substeps!=null){
            for(Step step: substeps){
                step.accept(visitor);
            }
        }
    }
}
