package pl.put.poznan.checker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ScenarioCheckerApplication class responsible for running the
 * created Spring application. Contains the main() method of the program.
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.checker.rest"})
public class ScenarioCheckerApplication {

    /**
     * Main method of the class, responsible for running the
     * Spring Application
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ScenarioCheckerApplication.class, args);
    }
}
