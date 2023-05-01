package pl.put.poznan.checker.logic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//TODO: Fix bug that occurs when length of array with FOR EACH is equal to 1
public class JSONParser {
    public static void main(String[] args) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get("C:\\Users\\barte\\scenario-quality-checker\\src\\main\\java\\pl\\put\\poznan\\checker\\logic\\file.JSON")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(content);
            String title = obj.getString("title");
            JSONArray actors = obj.getJSONArray("actors");
            String systemActor = obj.getString("systemActor");
            JSONArray steps = obj.getJSONArray("steps");

            System.out.println("Title: " + title);
            System.out.print("Actors: ");
            for (int i = 0; i < actors.length(); i++) {
                System.out.print(actors.getString(i) + " ");
            }
            System.out.println("\nSystem Actor: " + systemActor);
            System.out.println("Steps: ");
            parseSteps(steps);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void parseSteps(JSONArray steps) {
        for (int i = 0; i < steps.length(); i++) {
            Object step = null;
            try {
                step = steps.get(i);
                if (step instanceof JSONObject) {
                    JSONObject stepObj = (JSONObject) step;
                    String key = stepObj.keys().next().toString();
                    if (key.equals("IF")){
                        JSONObject conditionObj = stepObj.getJSONObject(key);
                        String conditionKey = conditionObj.keys().next().toString();
                        String conditionValue = conditionObj.getString(conditionKey);
                        String condition = conditionKey + ": " + conditionValue;

                        System.out.println("Key: " + key);
                        System.out.println("Condition: " + condition);

                    } else if (key.equals("ELSE")){
                        JSONObject conditionObj = stepObj.getJSONObject(key);
                        String conditionKey = conditionObj.keys().next().toString();
                        String conditionValue = conditionObj.getString(conditionKey);
                        String condition = conditionKey + ": " + conditionValue;

                        System.out.println("Key: " + key);
                        System.out.println("Condition: " + condition);
                    }
                    else if (key.equals("FOR EACH")){
                        System.out.println(key + ": " + stepObj.getString(key));
                    }
                    else {
                        System.out.println(key + ": " + stepObj.getString(key));
                    }
                } else if (step instanceof JSONArray) {
                    parseSteps((JSONArray) step);
                } else {
                    System.out.println(step.toString());
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static Scenario parseScenarioObject(JSONObject obj) throws JSONException {
        String title = obj.getString("title");
        JSONArray actorsArray = obj.getJSONArray("actors");
        String systemActor = obj.getString("systemActor");
        JSONArray scenarioSteps = obj.getJSONArray("steps");

        String [] actors = new String[actorsArray.length()];
        for (int i = 0; i < actorsArray.length(); i++) {
            actors[i] = actorsArray.getString(i);
        }

        ArrayList<Step> steps = parseScenarioSteps(scenarioSteps);

        return new Scenario(title, actors, systemActor, steps);
    }

    private static ArrayList<Step> parseScenarioSteps(JSONArray steps){
        ArrayList<Step> newSteps = new ArrayList<Step>();

        for (int i = 0; i < steps.length(); i++) {
            Object stepObject = null;
            String actor = null;
            String keyword = null;
            String action = null;
            ArrayList<Step> substeps = null;

            try {
                stepObject = steps.get(i);
                if (stepObject instanceof JSONObject) {
                    JSONObject stepObj = (JSONObject) stepObject;
                    String key = stepObj.keys().next().toString();

                    // if one of keyword statements
                    if (key.equals("IF") || key.equals("ELSE")){
                        JSONObject conditionObj = stepObj.getJSONObject(key);
                        keyword = key;
                        actor = conditionObj.keys().next().toString();
                        action = conditionObj.getString(actor);

                    }
                    // if is an actor action
                    else {
                        actor = key;
                        action = stepObj.getString(key);
                    }
                }
                // is another substep
                else if (stepObject instanceof JSONArray) {
                    substeps = parseScenarioSteps((JSONArray) stepObject);
                }
                // is a lone action
                else {
                    action = stepObject.toString();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            newSteps.add(new Step(actor, keyword, action, substeps));
        }
        return newSteps;
    }


}