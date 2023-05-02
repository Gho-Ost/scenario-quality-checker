package pl.put.poznan.checker.rest;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.*;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.*;


@RestController
public class ScenarioCheckerController {
    private Map<String, Scenario> scenarioStorage = new HashMap<String, Scenario>();

    private static final Logger logger = LoggerFactory.getLogger(ScenarioCheckerController.class);

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String get(@PathVariable String text,
                              @RequestParam(value= "checks", defaultValue="upper,escape") String[] checks) {

        // log the parameters
        logger.debug(text);
        logger.debug(Arrays.toString(checks));

        // perform the transformation, you should run your logic here, below is just a silly example
        ScenarioChecker checker = new ScenarioChecker(checks);
        return checker.check(text);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String post(@PathVariable String text,
                      @RequestBody String[] checks) {

        // log the parameters
        logger.debug(text);
        logger.debug(Arrays.toString(checks));

        // perform the transformation, you should run your logic here, below is just a silly example
        ScenarioChecker checker = new ScenarioChecker(checks);
        return checker.check(text);
    }

    /**
     * Stores a scenario
     * @param scenarioContent
     * @return
     */
    @PostMapping("/scenario")
    public Scenario addScenario(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // TODO check if title occupied?

        scenarioStorage.put(newScenario.getTitle(), newScenario);

        return newScenario;
    }

    /**
     * Returns a chosen scenario
     * @return
     */
    @GetMapping("/scenario/{title}")
    public Scenario getScenario(@PathVariable("title")String title) {
        return scenarioStorage.get(title);
    }

    /**
     * Returns all scenarios
     * @return
     */
    @GetMapping("/scenarios")
    public Map<String, Scenario> getScenarios() {
        return scenarioStorage;
    }

    /**
     * Test of visitor functionality
     * @param scenarioContent
     * @return
     */
    @PostMapping("/test")
    public Scenario testVisitors(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//         newScenario.printScenario();

        ScenarioTextDownloadVisitor textVisitor = new ScenarioTextDownloadVisitor();
        ScenarioCountVisitor countVisitor = new ScenarioCountVisitor();
        ScenarioKeyWordCountVisitor keyWordCountVisitor = new ScenarioKeyWordCountVisitor();
        ScenarioLevelVisitor levelVisitor = new ScenarioLevelVisitor(2);
        ScenarioMissingActorVisitor missingActorVisitor = new ScenarioMissingActorVisitor();

        newScenario.accept(textVisitor);

        newScenario.accept(countVisitor);
        System.out.println("\nCounted steps: " + countVisitor.getStepCount());

        newScenario.accept(keyWordCountVisitor);
        System.out.println("\nKeyword count: " + keyWordCountVisitor.getKeyWordCount());

        newScenario.accept(levelVisitor);
        System.out.println("\nScenario level 2:");
        levelVisitor.getScenario().printScenario();

        System.out.println("\nMissing actor steps:");
        newScenario.accept(missingActorVisitor);
        for (Step step : missingActorVisitor.getNoActorSteps()){
            step.printStep();
        }

        return newScenario;
    }
}


