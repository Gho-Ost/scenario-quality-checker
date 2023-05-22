package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioKeyWordCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

@RestController
public class GetScenarioKeywordCount extends ScenarioController{
    GetScenarioKeywordCount(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }


    /**
     * Handles GET requests to the "/scenarios/{title}/keywordcount" endpoint and
     * returns the keyword count for the stored scenario with the given title by using the visitor.
     * Produces application/JSON response
     * @param title the title of the scenario as a path variable
     * @return the keyword count for the scenario with the given title
     */
    @GetMapping(value="/scenarios/{title}/keywordcount", produces = "application/JSON")
    public String getScenarioKeywordCount(@PathVariable("title")String title) {
        ScenarioCheckerLogger.logger.debug("Getting keyword count for scenario with title: {}", title);
        Scenario scenario = storage.scenarios.get(title);
        ScenarioCheckerLogger.logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        scenario.accept(visitor);
        Integer keywordCount = visitor.getKeyWordCount();
        ScenarioCheckerLogger.logger.info("Keyword count for scenario with title {}: {}", title, keywordCount);
        return "{\"Keyword count\": " + keywordCount + "}";
    }

}
