package pl.put.poznan.checker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.put.poznan.checker.rest"})
public class ScenarioCheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScenarioCheckerApplication.class, args);
    }
}
