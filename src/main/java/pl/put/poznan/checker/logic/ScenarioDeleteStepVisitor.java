package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;

/**
 * MODIFIES EXISITNG SCENARIO
 */
public class ScenarioDeleteStepVisitor implements Visitor{

    private String selectedStep;
    private Scenario scenario;
    private ArrayList<Step> steps;
    private boolean decreaseSteps = false;
    private boolean decreaseStepsBase = false;

    public ScenarioDeleteStepVisitor(String selectedStep) {
        this.selectedStep = selectedStep;
    }

    /**
     * @param scenario - Scenario to be visited
     */
    @Override
    public void visit(Scenario scenario) {
        this.scenario = scenario;
        this.steps = new ArrayList<Step>();
    }

    /**
     * @param step
     */
    @Override
    public void visit(Step step) {
        Step stepCopy = new Step(step);

        String[] currentLevel = stepCopy.getStepLevel().split("\\.");

        if (this.selectedStep.equals(stepCopy.getStepLevel()) && currentLevel.length==1) {
            this.decreaseStepsBase = true;
        }
        else if(currentLevel[0].equals(this.selectedStep.split("\\.")[0]) && currentLevel.length==1) {
            if (this.selectedStep.length() == 3){
                stepCopy.setSubsteps(modifySubsteps(stepCopy.getSubsteps()));
            }
            else{
                stepCopy.setSubsteps(findSubsteps(stepCopy.getSubsteps()));
            }
            this.steps.add(stepCopy);
        }
        else if(currentLevel.length==1 && this.decreaseStepsBase){
            int newStepLevel = Integer.parseInt(currentLevel[0]) - 1;
            currentLevel[0] = Integer.toString(newStepLevel);
            stepCopy.setStepLevel(String.join(".", currentLevel));

            stepCopy.setSubsteps(decreaseSubsteps(stepCopy.getSubsteps(), 0));
            this.steps.add(stepCopy);
        }
        else if(currentLevel.length==1){
            this.steps.add(stepCopy);
        }
    }

    /**
     * Reduces the step level at each substep at a correct position
     * @param substeps
     * @param position
     * @return
     */
    private ArrayList<Step> decreaseSubsteps(ArrayList<Step> substeps, int position) {
        ArrayList<Step> newSubsteps = new ArrayList<Step>();

        if (substeps!=null){
            for (Step step : substeps){
                String[] currentLevel = step.getStepLevel().split("\\.");
                int newStepLevel = Integer.parseInt(currentLevel[position]) - 1;
                currentLevel[position] = Integer.toString(newStepLevel);
                step.setStepLevel(String.join(".", currentLevel));

                if (step.getSubsteps() != null){
                    step.setSubsteps(decreaseSubsteps(step.getSubsteps(), position));
                }
                newSubsteps.add(step);
            }
            return newSubsteps;
        }
        return null;
    }

    /**
     * Finds the substeps of the same level as the selected step
     * @param substeps
     * @return
     */
    private ArrayList<Step> findSubsteps(ArrayList<Step> substeps) {
        ArrayList<Step> substitute = new ArrayList<Step>();

        for (Step step : substeps){
            String stepLevel = step.getStepLevel();

            // If selected step in the substeps of this step
            if (stepLevel.equals(this.selectedStep.substring(0, selectedStep.length()-2))){
                step.setSubsteps(modifySubsteps(step.getSubsteps()));
            }
            substitute.add(step);
        }

        return substitute;
    }

    /**
     * Modifies the substeps of the same level as the selected step
     * @param substeps
     * @return
     */
    private ArrayList<Step> modifySubsteps(ArrayList<Step> substeps) {
        ArrayList<Step> substitute = new ArrayList<Step>();
        int position = 0;

        for (Step step : substeps){
            if (step.getStepLevel().equals(this.selectedStep)){
                this.decreaseSteps=true;
            }
            else if (this.decreaseSteps){
                position = this.selectedStep.split("\\.").length - 1;
                String[] currentLevel = step.getStepLevel().split("\\.");
                int newStepLevel = Integer.parseInt(currentLevel[position]) - 1;
                currentLevel[position] = Integer.toString(newStepLevel);
                step.setStepLevel(String.join(".", currentLevel));

                if (step.getSubsteps() != null){
                    step.setSubsteps(decreaseSubsteps(step.getSubsteps(), position));
                }
                substitute.add(step);
            }
            else{
                substitute.add(step);
            }
        }

        return substitute;
    }

    public Scenario getScenario(){
        scenario.setSteps(this.steps);
        return scenario;
    }
}
