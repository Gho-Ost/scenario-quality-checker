package pl.put.poznan.checker.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioChecker;

import java.util.Arrays;


@RestController
@RequestMapping("/{text}")
public class ScenarioCheckerController {

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

}


