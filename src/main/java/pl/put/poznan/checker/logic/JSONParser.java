package pl.put.poznan.checker.logic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//TODO: Fix bug that occurs when length of array with FOR EACH is equal to 1
public class JSONParser {
    public static void main(String[] args) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get("C:\\Users\\Komputer\\scenario-quality-checker\\src\\main\\java\\pl\\put\\poznan\\checker\\logic\\file.JSON")));
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
}