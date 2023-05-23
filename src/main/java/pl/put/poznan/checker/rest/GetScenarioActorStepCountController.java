package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioActorStepCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller responsible for performing a counting operation for steps performed by a given actor.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetScenarioActorStepCountController extends ScenarioController {

    /**
     * Constructor for GetScenarioActorStepCountController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetScenarioActorStepCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios/{title}/actorstepcount/{actor}" endpoint
     * and returns the number of steps for the given actor in the stored scenario with the given title.
     * The number of steps for the given actor is returned inside the response body as a JSON object.
     *
     * @param title (String) the title of the scenario to count steps for, as a path variable
     * @param actor (String) the name of the actor to count steps for, as a path variable
     * @return (String) a JSON object containing the number of steps for the given actor
     */
    @GetMapping(value = "scenarios/{title}/actorstepcount/{actor}", produces = "application/JSON")
    public String getScenarioActorStepCount(@PathVariable("title")String title, @PathVariable("actor")String actor) {
        ScenarioCheckerLogger.logger.debug("Getting step count for scenario with title: {}", title);
        Scenario scenario = storage.scenarios.get(title);
        ScenarioCheckerLogger.logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioActorStepCountVisitor actorStepCountVisitor = new ScenarioActorStepCountVisitor(actor);
        scenario.accept(actorStepCountVisitor);
        Integer stepCount = actorStepCountVisitor.getActorStepCount();
        super.logger.logger.info("Step count for scenario with title {} for actor {}: {}", title, actor, stepCount);
        return "{\"" + actor + " step count\": " + actorStepCountVisitor.getActorStepCount() + "}";
    }
}
