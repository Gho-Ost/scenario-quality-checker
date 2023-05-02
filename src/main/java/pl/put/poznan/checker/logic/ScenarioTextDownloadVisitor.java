package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScenarioTextDownloadVisitor implements Visitor {
    private StringBuilder result = new StringBuilder();
    private String downloadPath;

    public ScenarioTextDownloadVisitor(){
        String homeDir = System.getProperty("user.home");
        Path downloadsDir = Paths.get(homeDir, "Downloads");
        Path filePath = downloadsDir.resolve("Scenario.txt");
        downloadPath = filePath.toString();
    }

    public String getResult() {
        return result.toString();
    }

    public void setDownloadPath(String downloadPath){
        this.downloadPath=downloadPath;
    }

    public void download() throws IOException {
        Path path = Paths.get(downloadPath);
        byte[] bytes = result.toString().getBytes();
        Files.write(path, bytes);
    }

    @Override
    public void visit(Scenario scenario) {
        String title = scenario.getTitle();
        String[] actors = scenario.getActors();
        String systemActor = scenario.getSystemActor();

        result.append("Title: ").append(title).append("\n");
        result.append("Actors: [");
        for (int i = 0; i < actors.length; i++) {
            result.append(actors[i]);
            if (i < actors.length - 1) {
                result.append(", ");
            }
        }
        result.append("]\n");
        result.append("System actor: ").append(systemActor).append("\n");
        result.append("Steps:\n");
    }

    @Override
    public void visit(Step step) {
        String stepLevel = step.getStepLevel();
        String keyword = step.getKeyword();
        String actor = step.getActor();

        result.append(String.format("%" + stepLevel.length()*2 + "s", stepLevel));

        if (keyword != null){
            result.append(" ").append(keyword);
        }
        if (actor != null){
            result.append(" ").append(actor);
        }
        result.append(" ").append(step.getAction()).append("\n");
    }
}
