package generateUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileUtility {
	public String getProperty(String data) throws IOException {
		// Create Properties object to load configuration from .properties file
		Properties prop = new Properties();

		// Read the properties file as a stream
		FileInputStream fis = new FileInputStream("./src/test/resources/commondata.properties");

		// Load properties into the Properties object
		prop.load(fis);
		
		String value = prop.getProperty(data);
		
		return value;
	}

}
