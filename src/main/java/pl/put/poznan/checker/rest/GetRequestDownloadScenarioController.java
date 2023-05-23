package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioTextDownloadVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller for handling GET requests to download a scenario as a text file.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestDownloadScenarioController extends ScenarioController{

    /**
     * Constructor for GetRequestDownloadScenarioController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetRequestDownloadScenarioController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }
    /**
     * Handles GET requests to the "/scenario/download" endpoint and
     * returns a downloadable text file containing the scenario provided in the request body.
     * Produces a plain text response.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return a ResponseEntity containing a Resource representing the downloadable text file
     */
    @GetMapping(value = "/scenario/download", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> getRequestDownloadScenario(@RequestBody String scenarioContent) {
        Scenario scenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            scenario = JSONParser.parseScenarioObject(scenarioObj);
            ScenarioCheckerLogger.logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    scenario.getTitle(), Arrays.toString(scenario.getActors()),
                    scenario.getSystemActor());
            super.logger.logSteps(scenario.getSteps());
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

        super.logger.logger.info("Returning download for scenario with title: {}",scenario.getTitle());
        // Return the resource as the response with the appropriate headers and content type
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
