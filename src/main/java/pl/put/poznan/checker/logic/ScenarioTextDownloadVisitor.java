package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class responsible for downloading a scenario via the implementation of a Visitor interface,
 * as a means of implementing the Visitor design pattern. The class instance is meant to
 * traverse the Scenario, save it as a String type object and help user save it onto their
 * computer. The class contains result and downloadPath fields, for storing the String object
 * representing a Scenario and the path for the object to be saved respectively. The default
 * downloadPath should lead to Window's "Downloads" folder.
 *
 * <p>Example usage of the class to download a Scenario for
 * some previously created Scenario called my_scenario:
 * <pre>
 * {@code
 * ScenarioTextDownloadVisitor visitor = new ScenarioTextDownloadVisitor();
 * my_scenario.accept(visitor);
 * visitor.download();
 *  }
 * </pre>
 */
public class ScenarioTextDownloadVisitor implements Visitor {
    private StringBuilder result = new StringBuilder();
    private String downloadPath;

    /**
     * Constructor responsible for setting the downlaodPath field to the default value
     * of "Downloads" folder in a Windows machine.
     */
    public ScenarioTextDownloadVisitor(){
        String homeDir = System.getProperty("user.home");
        Path downloadsDir = Paths.get(homeDir, "Downloads");
        Path filePath = downloadsDir.resolve("Scenario.txt");
        downloadPath = filePath.toString();
    }

    /**
     * Returns the saved Scenario as a String.
     * The result is saved as (*text* represents where variable values would be put): <br>
     * Title: *Scenario Title* <br>
     * Actors: [*names of actors divided by , *] <br>
     * System actor: *System actor name* <br>
     * Steps: <br>
     *  *Steps written with numbers at the front like: <br>
     *  1. Step1 <br>
     *  2.Step2 <br>
     *      2.1 Step2.1* <br>
     * @return result - String object representing the Scenario
     */
    public String getResult() {
        return result.toString();
    }

    /**
     * Allows the user the set the download path for the file. After setting the path
     * a download can be triggered by using the download() method.
     * @param downloadPath String representing where the file is meant to be saved
     */
    public void setDownloadPath(String downloadPath){
        this.downloadPath=downloadPath;
    }

    /**
     * Method responsible fo downloading/saving a file. The file is saved to the
     * path specified by downloadPath private field and can be changed by calling
     * the setDownloadPath() method. The default place for the file to be saved should be
     * the "Downloads" folder on a Windows machine.
     * @throws IOException triggered for incorrect filePath
     */
    public void download() throws IOException {
        Path path = Paths.get(downloadPath);
        byte[] bytes = result.toString().getBytes();
        Files.write(path, bytes);
    }

    /**
     * Concrete implementation for Visitor's visit() method
     * responsible for adding the Title, Actors, SystemActors and "Steps" header parts of the
     * Scenario.
     * @param scenario - Scenario to be visited
     */
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

    /**
     * Concrete implementation of visit() method present in the Visitor interface,
     * responsible for adding the Steps to the String.
     * @param step Step to be read
     */
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
