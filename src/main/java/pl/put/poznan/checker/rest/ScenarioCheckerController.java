package pl.put.poznan.checker.rest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.*;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * Scenario step count visitor
     * by title
     * @return
     */
    @GetMapping("/stepcount/{title}")
    public Integer getScenarioStepCount(@PathVariable("title")String title) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioCountVisitor visitor = new ScenarioCountVisitor();
        scenario.accept(visitor);
        return visitor.getStepCount();
    }

    /**
     * Scenario keyword count visitor
     * by title
     * @return
     */
    @GetMapping("/keywordcount/{title}")
    public Integer getScenarioKeywordCount(@PathVariable("title")String title) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        scenario.accept(visitor);
        return visitor.getKeyWordCount();
    }

    /**
     * Scenario level cutting visitor
     * by title
     * @return
     */
    @GetMapping("/levelcut/{title}/{maxLevel}")
    public Scenario getScenarioLevelCut(@PathVariable String title, @PathVariable Integer maxLevel) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        scenario.accept(visitor);
        return visitor.getScenario();
    }

    /**
     * Scenario missing actor visitor
     * by title
     * @return
     */
    @GetMapping("/missingactor/{title}")
    public List<Step> getScenarioMissingActor(@PathVariable("title")String title) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        return visitor.getNoActorSteps();
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

        scenarioStorage.put(newScenario.getTitle(), newScenario);

        return newScenario;
    }

    /**
     * Returns a scenario selected by title
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
     * Delete all scenarios
     * @return
     */
    @DeleteMapping("/scenarios")
    public Map<String, Scenario> deleteScenarios() {
        scenarioStorage.clear();
        return scenarioStorage;
    }

    /**
     * Delete scenario selected by title
     * @return
     */
    @DeleteMapping("/scenario/{title}")
    public Map<String, Scenario> deleteScenario(@PathVariable("title")String title) {
        scenarioStorage.remove(title);
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


