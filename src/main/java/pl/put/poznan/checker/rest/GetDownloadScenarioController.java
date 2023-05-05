package pl.put.poznan.checker.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import pl.put.poznan.checker.logic.ScenarioTextDownloadVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

@RestController
public class GetDownloadScenarioController extends ScenarioController{
    GetDownloadScenarioController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios/{title}/download" endpoint and returns a downloadable text file containing the scenario with the given title.
     * Produces a plain text response.
     * @param title the title of the scenario as a path variable
     * @return a ResponseEntity containing a Resource representing the downloadable text file
     */
    @GetMapping(value="/scenarios/{title}/download", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> getDownloadScenario(@PathVariable("title")String title) {
        ScenarioCheckerLogger.logger.debug("Getting download for scenario with title: {}", title);
        Scenario scenario=getScenario(title);
        ScenarioCheckerLogger.logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
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
        ScenarioCheckerLogger.logger.info("Returning download for scenario with title: {}", title);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }


    /**
     * Handles GET requests to the "/scenario/{title}" endpoint and
     * returns the scenario with the given title from the storage.
     * Produces application/JSON response
     * @param title the title of the scenario as a path variable
     * @return the scenario with the given title
     */
    @GetMapping(value = "/scenarios/{title}", produces = "application/JSON")
    public Scenario getScenario(@PathVariable("title")String title) {
        Scenario scenario=storage.scenarios.get(title);
        ScenarioCheckerLogger.logger.info("Getting scenario of title{}", scenario.getTitle());
        ScenarioCheckerLogger.logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        super.logger.logSteps(scenario.getSteps());
        return scenario;
    }
}
