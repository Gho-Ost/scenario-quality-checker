package pl.put.poznan.checker.logic;

import java.util.ArrayList;

// TEST template for filling steps - idk if this automatic parsing can even work like that
public class Step implements Visitable{
    private String actor;
    private String keyword;
    private ArrayList<Step> substeps;

    public Step(String actor) {
        this.actor = actor;
    }

    public Step(String actor, String keyword, ArrayList<Step> substeps) {
        this.actor = actor;
        this.keyword = keyword;
        this.substeps = substeps;
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
        //TODO: fill
    }
}
