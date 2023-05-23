package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioMissingActorVisitor;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.Arrays;
import java.util.List;

/**
 * REST controller responsible for finding missing actors in a Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetScenarioMissingActorController extends ScenarioController{


    GetScenarioMissingActorController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios/{title}/missingactor" endpoint and returns
     * a list of steps with missing actors for the stored scenario with the given title.
     * Produces application/JSON response.
     * Finding of missing actors is accomplished via the Visitor design pattern.
     * @param title the title of the scenario as a path variable
     * @return a list of steps with missing actors for the scenario with the given title
     */
    @GetMapping(value="/scenarios/{title}/missingactor", produces = "application/JSON")
    public List<Step> getScenarioMissingActor(@PathVariable("title")String title) {
        ScenarioCheckerLogger.logger.info("Getting download for scenario with title: {}", title);
        Scenario scenario = super.storage.scenarios.get(title);
        ScenarioCheckerLogger.logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
        scenario.accept(visitor);
        super.logger.logSteps(visitor.getNoActorSteps());
        return visitor.getNoActorSteps();
    }
}
