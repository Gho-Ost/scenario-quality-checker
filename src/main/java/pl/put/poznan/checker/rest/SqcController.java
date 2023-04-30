package pl.put.poznan.checker.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@RestController
public class SqcController {

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/transform")
    public String transformScenario(@RequestBody String json) throws IOException {
        // Parse the input JSON data into an object model
        Map<String, Object> scenario = mapper.readValue(json, Map.class);

        //TODO:
        // Perform any currently implemented functions on the scenario object model


        // Return the transformed scenario as JSON
        return mapper.writeValueAsString(scenario);
    }
}
