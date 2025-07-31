package generateUtility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;                    // Represents a JSON object
import org.json.simple.parser.JSONParser;            // Used to parse JSON text into Java objects
import org.json.simple.parser.ParseException;        // Exception thrown if JSON is malformed

public class JsonFileUtility {

    // Method to fetch a value from a JSON file based on a given key
    public String fetchDataFromJsonFile(String key) throws FileNotFoundException, IOException, ParseException {

        // Create a JSONParser object to parse the JSON file
        JSONParser parser = new JSONParser();

        // Read and parse the JSON file located at the given path
        Object obj = parser.parse(new FileReader("./test/resources/vtigerdata.json"));

        // Convert parsed object into JSONObject for key-value access
        JSONObject js = (JSONObject) obj;

        // Retrieve the value associated with the provided key and convert it to String
        String data = js.get(key).toString();

        // Return the value found
        return data;
    }
}
