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

/**
 * This class is a Spring Web MVC controller that handles HTTP requests and returns responses in the form of JSON
 * within the class logging functionality is implemented and all methods allowing for
 * operations on Scenario type objects should be available through this class using the REST API.
 */
@RestController
public class ScenarioCheckerController {
    /**
     * A map used to store Scenario objects with a String key.
     */
    private Map<String, Scenario> scenarioStorage = new HashMap<String, Scenario>();

    /**
     * A logger used to log messages using the SLF4J logging API.
     */
    private static final Logger logger = LoggerFactory.getLogger(ScenarioCheckerController.class);

    /**
     * Logs the steps in the given list using the logger.
     * @param steps the ArrayList of steps to log
     */
    private void logSteps(ArrayList<Step> steps) {
        for (Step step : steps) {
            logger.debug("Step with level: {} actor: {} keyword: {} action: {}",
                    step.getStepLevel(), step.getActor(), step.getKeyword(), step.getAction());
            if(step.getSubsteps()!=null) {
                logSteps(step.getSubsteps());
            }
        }
    }

    /**
     * Logs the steps in the given list using the logger.
     * @param steps the list of steps to log
     */
    private void logSteps(List<Step> steps) {
        for (Step step : steps) {
            logger.debug("Step with level: {} actor: {} keyword: {} action: {}",
                    step.getStepLevel(), step.getActor(), step.getKeyword(), step.getAction());
            if(step.getSubsteps()!=null) {
                logSteps(step.getSubsteps());
            }
        }
    }

    /**
     * Handles GET requests and returns a JSON response.
     * @param text the text to check as a path variable
     * @param checks an array of checks to perform as a request parameter, with default value "upper,escape"
     * @return a JSON response containing the result of the checks
     */
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

    /**
     * Handles POST requests and returns a JSON response.
     * @param text the text to check as a path variable
     * @param checks an array of checks to perform as a request body
     * @return a JSON response containing the result of the checks
     */
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
     * Handles GET requests to the "scenarios/{title}/stepcount" endpoint
     * and returns the step count using visitor for the stored scenario with the given title.
     * @param title the title of the scenario as a path variable
     * @return the step count for the scenario with the given title
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
     * Handles GET requests to the "/scenarios/{title}/keywordcount" endpoint and
     * returns the keyword count for the stored scenario with the given title by using the visitor.
     * @param title the title of the scenario as a path variable
     * @return the keyword count for the scenario with the given title
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
     * Handles GET requests to the "/scenarios/{title}/levelcut/{maxLevel}" endpoint and returns
     * a truncated version of the scenario with the given title for a stored Scenario.
     * Truncating in achieved by using the Visitor design pattern.
     * @param title the title of the scenario as a path variable
     * @param maxLevel the maximum level to include in the level-cut scenario as a path variable
     * @return a level-cut version of the scenario with the given title
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
     * Handles GET requests to the "/scenarios/{title}/missingactor" endpoint and returns
     * a list of steps with missing actors for the stored scenario with the given title.
     * Finding of missing actors is accomplished via the Visitor design pattern.
     * @param title the title of the scenario as a path variable
     * @return a list of steps with missing actors for the scenario with the given title
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
     * Handles GET requests to the "/scenarios/{title}/download" endpoint and returns a downloadable text file containing the scenario with the given title.
     * @param title the title of the scenario as a path variable
     * @return a ResponseEntity containing a Resource representing the downloadable text file
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
     * Handles GET requests to the "/scenario/stepcount" endpoint and returns
     * the step count for the scenario provided in the request body.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return the step count for the provided scenario
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
     * Handles GET requests to the "/scenario/keywordcount" endpoint
     * and returns the keyword count for the scenario provided in the request body.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return the keyword count for the provided scenario
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
     * Handles GET requests to the "/scenario/levelcut/{maxLevel}" endpoint
     * and returns a truncated version of the scenario provided in the request body.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @param maxLevel the maximum level to include in the level-cut scenario as a path variable
     * @return a level-cut version of the provided scenario. The truncated Scenario
     * will be level-cut with accordance to specified value. For value of maxLevel
     * equal to 1, only the main scenario will be returned.
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
     * Handles GET requests to the "/scenario/missingactor" endpoint
     * and returns a list of steps with missing actors for the scenario
     * provided in the request body.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return a list of steps with missing actors for the provided scenario
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
     * Handles GET requests to the "/scenario/download" endpoint and
     * returns a downloadable text file containing the scenario provided in the request body.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return a ResponseEntity containing a Resource representing the downloadable text file
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
     * Handles POST requests to the "/scenario" endpoint and adds a new scenario
     * to the storage based on the JSON representation provided in the request body.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return the added scenario
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
     * Handles GET requests to the "/scenario/{title}" endpoint and
     * returns the scenario with the given title from the storage.
     * @param title the title of the scenario as a path variable
     * @return the scenario with the given title
     */
    @GetMapping("/scenario/{title}")
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
     * Handles GET requests to the "/scenarios" endpoint and returns all scenarios in the storage.
     * @return a map containing all scenarios in the storage
     */
    @GetMapping("/scenarios")
    public Map<String, Scenario> getScenarios() {
        logger.info("Getting Scenarios");
        return scenarioStorage;
    }

    /**
     * Handles DELETE requests to the "/scenarios" endpoint and
     * removes all scenarios from the storage.
     * @return an empty map representing the cleared storage
     */
    @DeleteMapping("/scenarios")
    public Map<String, Scenario> deleteScenarios() {
        scenarioStorage.clear();
        logger.info("Deleting Scenarios");
        return scenarioStorage;
    }

    /**
     * Handles DELETE requests to the "/scenario/{title}" endpoint and
     * removes the scenario with the given title from the storage.
     * @param title the title of the scenario to remove as a path variable
     * @return a map representing the updated storage after removing the scenario
     */
    @DeleteMapping("/scenario/{title}")
    public Map<String, Scenario> deleteScenario(@PathVariable("title")String title) {
        scenarioStorage.remove(title);
        logger.info("Deleted Scenario with title {}", title);
        return scenarioStorage;
    }

    /**
     * Test of visitor functionality
     * @param scenarioContent String containing contents of the scenario.
     * @return new Scenario after tests.
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


