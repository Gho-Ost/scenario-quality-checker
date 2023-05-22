package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioActorStepCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

@RestController
public class GetScenarioActorStepCountController extends ScenarioController {
    GetScenarioActorStepCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "scenarios/{title}/actorstepcount/{actor}"
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
