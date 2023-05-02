package pl.put.poznan.checker.rest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     * Scenario step count visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("scenarios/{title}/stepcount")
    public Integer getScenarioStepCount(@PathVariable("title")String title) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioCountVisitor visitor = new ScenarioCountVisitor();
        scenario.accept(visitor);
        return visitor.getStepCount();
    }

    /**
     * Scenario keyword count visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("/scenarios/{title}/keywordcount")
    public Integer getScenarioKeywordCount(@PathVariable("title")String title) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        scenario.accept(visitor);
        return visitor.getKeyWordCount();
    }

    /**
     * Scenario level cutting visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("/scenarios/{title}/levelcut/{maxLevel}")
    public Scenario getScenarioLevelCut(@PathVariable String title, @PathVariable Integer maxLevel) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        scenario.accept(visitor);
        return visitor.getScenario();
    }

    /**
     * Scenario missing actor visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("/scenarios/{title}/missingactor")
    public List<Step> getScenarioMissingActor(@PathVariable("title")String title) {
        Scenario scenario = scenarioStorage.get(title);
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        return visitor.getNoActorSteps();
    }

    /**
     * Download method through title
     * @param title
     * @return
     */
    @GetMapping("/scenarios/{title}/download")
    public ResponseEntity<Resource> downloadScenario(@PathVariable("title")String title) {
        Scenario scenario=getScenario(title);
        ScenarioTextDownloadVisitor scenarioTextDownloadVisitor= new ScenarioTextDownloadVisitor();
        scenario.accept(scenarioTextDownloadVisitor);
        String scenarioText = scenarioTextDownloadVisitor.getResult();
        ByteArrayResource resource = new ByteArrayResource(scenarioText.getBytes());

        // Set the content disposition header to trigger a download in the browser
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=scenario.txt");

        // Return the resource as the response with the appropriate headers and content type
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    /**
     * Scenario step count visitor based on request body
     * by title
     * @return
     */
    @GetMapping("/scenario/stepcount")
    public Integer getRequestScenarioStepCount(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioCountVisitor visitor = new ScenarioCountVisitor();
        newScenario.accept(visitor);
        return visitor.getStepCount();
    }

    /**
     * Scenario keyword count visitor based on request body
     * by title
     * @return
     */
    @GetMapping("/scenario/keywordcount")
    public Integer getRequestScenarioKeywordCount(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        newScenario.accept(visitor);
        return visitor.getKeyWordCount();
    }

    /**
     * Scenario level cutting visitor based on request body
     * by title
     * @return
     */
    @GetMapping("/scenario/levelcut/{maxLevel}")
    public Scenario getRequestScenarioLevelCut(@RequestBody String scenarioContent, @PathVariable Integer maxLevel) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        newScenario.accept(visitor);
        return visitor.getScenario();
    }

    /**
     * Scenario missing actor visitor based on request body
     * by title
     * @return
     */
    @GetMapping("/scenario/missingactor")
    public List<Step> getRequestScenarioMissingActor(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        newScenario.accept(visitor);
        return visitor.getNoActorSteps();
    }

    /**
     * Download method through request body
     * @return
     */
    @GetMapping("/scenario/download")
    public ResponseEntity<Resource> downloadRequestScenario(@RequestBody String scenarioContent) {
        Scenario scenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            scenario = JSONParser.parseScenarioObject(scenarioObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioTextDownloadVisitor scenarioTextDownloadVisitor= new ScenarioTextDownloadVisitor();
        scenario.accept(scenarioTextDownloadVisitor);

        String scenarioText = scenarioTextDownloadVisitor.getResult();
        ByteArrayResource resource = new ByteArrayResource(scenarioText.getBytes());

        // Set the content disposition header to trigger a download in the browser
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=scenario.txt");

        // Return the resource as the response with the appropriate headers and content type
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
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


