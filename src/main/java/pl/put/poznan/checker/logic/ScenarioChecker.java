package pl.put.poznan.checker.logic;

/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public class ScenarioChecker {

    private final String[] checks;

    public ScenarioChecker(String[] checks){
        this.checks = checks;
    }

    public String check(String text){
        // of course, normally it would do something based on the transforms
        return text.toUpperCase();
    }
}
