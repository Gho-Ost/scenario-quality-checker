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

    private void logSteps(ArrayList<Step> steps) {
        for (Step step : steps) {
            logger.debug("Step with level: {} actor: {} keyword: {} action: {}",
                    step.getStepLevel(), step.getActor(), step.getKeyword(), step.getAction());
            if(step.getSubsteps()!=null) {
                logSteps(step.getSubsteps());
            }
        }
    }

    private void logSteps(List<Step> steps) {
        for (Step step : steps) {
            logger.debug("Step with level: {} actor: {} keyword: {} action: {}",
                    step.getStepLevel(), step.getActor(), step.getKeyword(), step.getAction());
            if(step.getSubsteps()!=null) {
                logSteps(step.getSubsteps());
            }
        }
    }

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
        logger.debug("Getting step count for scenario with title: {}", title);
        Scenario scenario = scenarioStorage.get(title);
        logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioCountVisitor visitor = new ScenarioCountVisitor();
        scenario.accept(visitor);
        Integer stepCount = visitor.getStepCount();
        logger.info("Step count for scenario with title {}: {}", title, stepCount);
        return stepCount;
    }

    /**
     * Scenario keyword count visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("/scenarios/{title}/keywordcount")
    public Integer getScenarioKeywordCount(@PathVariable("title")String title) {
        logger.debug("Getting keyword count for scenario with title: {}", title);
        Scenario scenario = scenarioStorage.get(title);
        logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        scenario.accept(visitor);
        Integer keywordCount = visitor.getKeyWordCount();
        logger.info("Keyword count for scenario with title {}: {}", title, keywordCount);
        return keywordCount;
    }

    /**
     * Scenario level cutting visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("/scenarios/{title}/levelcut/{maxLevel}")
    public Scenario getScenarioLevelCut(@PathVariable String title, @PathVariable Integer maxLevel) {
        logger.info("Getting ScenarioLevelCut for scenario with title: {} and cutting-level: {}", title, maxLevel);
        Scenario scenario = scenarioStorage.get(title);
        logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        scenario.accept(visitor);
        Scenario result=visitor.getScenario();
        logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                result.getTitle(), Arrays.toString(result.getActors()),
                result.getSystemActor());
        logSteps(result.getSteps());
        return result;
    }

    /**
     * Scenario missing actor visitor based on stored scenario title
     * by title
     * @return
     */
    @GetMapping("/scenarios/{title}/missingactor")
    public List<Step> getScenarioMissingActor(@PathVariable("title")String title) {
        logger.info("Getting download for scenario with title: {}", title);
        Scenario scenario = scenarioStorage.get(title);
        logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        logSteps(visitor.getNoActorSteps());
        return visitor.getNoActorSteps();
    }

    /**
     * Download method through title
     * @param title
     * @return
     */
    @GetMapping("/scenarios/{title}/download")
    public ResponseEntity<Resource> getDownloadScenario(@PathVariable("title")String title) {
        logger.debug("Getting download for scenario with title: {}", title);
        Scenario scenario=getScenario(title);
        logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioTextDownloadVisitor scenarioTextDownloadVisitor= new ScenarioTextDownloadVisitor();
        scenario.accept(scenarioTextDownloadVisitor);
        String scenarioText = scenarioTextDownloadVisitor.getResult();
        ByteArrayResource resource = new ByteArrayResource(scenarioText.getBytes());

        // Set the content disposition header to trigger a download in the browser
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=scenario.txt");

        // Return the resource as the response with the appropriate headers and content type
        logger.info("Returning download for scenario with title: {}", title);
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
            logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioCountVisitor visitor = new ScenarioCountVisitor();
        newScenario.accept(visitor);
        logger.info("Returning step count {}",visitor.getStepCount());
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
            logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        newScenario.accept(visitor);
        logger.info("Returning keyword count {}",visitor.getKeyWordCount());
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
            logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(maxLevel);
        newScenario.accept(visitor);
        Scenario result=visitor.getScenario();
        logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                result.getTitle(), Arrays.toString(result.getActors()),
                result.getSystemActor());
        logSteps(result.getSteps());
        return result;
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
            logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        newScenario.accept(visitor);
        logSteps(visitor.getNoActorSteps());
        return visitor.getNoActorSteps();
    }

    /**
     * Download method through request body
     * @return
     */
    @GetMapping("/scenario/download")
    public ResponseEntity<Resource> getRequestDownloadScenario(@RequestBody String scenarioContent) {
        Scenario scenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            scenario = JSONParser.parseScenarioObject(scenarioObj);
            logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    scenario.getTitle(), Arrays.toString(scenario.getActors()),
                    scenario.getSystemActor());
            logSteps(scenario.getSteps());
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

        logger.info("Returning download for scenario with title: {}",scenario.getTitle());
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
        logger.info("Adding Scenario of title: {}", newScenario.getTitle());
        logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                newScenario.getSystemActor());
        logSteps(newScenario.getSteps());
        return newScenario;
    }

    /**
     * Returns a scenario selected by title
     * @return
     */
    @GetMapping("/scenarios/{title}")
    public Scenario getScenario(@PathVariable("title")String title) {
        Scenario scenario=scenarioStorage.get(title);
        logger.info("Getting scenario of title{}", scenario.getTitle());
        logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        logSteps(scenario.getSteps());
        return scenario;
    }

    /**
     * Returns all scenarios
     * @return
     */
    @GetMapping("/scenarios")
    public Map<String, Scenario> getScenarios() {
        logger.info("Getting Scenarios");
        return scenarioStorage;
    }

    /**
     * Delete all scenarios
     * @return
     */
    @DeleteMapping("/scenarios")
    public Map<String, Scenario> deleteScenarios() {
        scenarioStorage.clear();
        logger.info("Deleting Scenarios");
        return scenarioStorage;
    }

    /**
     * Delete scenario selected by title
     * @return
     */
    @DeleteMapping("/scenarios/{title}")
    public Map<String, Scenario> deleteScenario(@PathVariable("title")String title) {
        scenarioStorage.remove(title);
        logger.info("Deleted Scenario with title {}", title);
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


