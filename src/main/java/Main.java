import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader("input.json");
            Object objects = parser.parse(reader);
            JSONObject inputs = (JSONObject) objects;
            parseInputData(inputs);
        } catch (FileNotFoundException e) {
            System.out.println("File missing!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File error!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error parsing file!");
            e.printStackTrace();
        }
    }

    private static void parseInputData(JSONObject object) {
        JSONObject data = (JSONObject) object.get("data");
        String name = (String)data.get("name");
        String lastname = (String)data.get("lastname");
        Long id = (Long)data.get("id");
        System.out.println("name: " + name + ", lastname: " + lastname + ", id: " + id);
    }
}
