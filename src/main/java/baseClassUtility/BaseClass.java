package baseClassUtility;

import java.io.IOException;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Reporter;
import org.testng.annotations.*;

import generateUtility.DatabaseUtility;
import generateUtility.PropertyFileUtility;
import generateUtility.WebdriverUtility;
import pomUtilitys.HomePage;
import pomUtilitys.Loginpage;

public class BaseClass {
	DatabaseUtility dutil = new DatabaseUtility();
	PropertyFileUtility prop = new PropertyFileUtility();
	WebdriverUtility wutil = new WebdriverUtility(); 
	
	public static WebDriver driver = null; 
	public static WebDriver sdriver;  // Fixed: should not initialize with 'driver' which is null
	
	@BeforeSuite
	public void coonectToDatabase() throws SQLException {
		dutil.openDataBase();
	}
	
	@BeforeTest
	public void conParellelConnection() {
		Reporter.log("configure parallel execution", true);
	}
	
	@Parameters("browser")
	@BeforeClass
	public void launtchTheBrowser(String browser) throws IOException {
		String Browser = prop.getProperty("browser");

		// Prefer parameter from XML if passed; fallback to property file
		if (browser != null && !browser.isEmpty()) {
			Browser = browser;
		}

		if (Browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (Browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else {
			driver = new ChromeDriver(); // Default
		}
		
		sdriver = driver;  // Fixed: assigning to static driver properly
	}
	
	@BeforeMethod
	public void login() throws IOException {
		String user = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String timesout = prop.getProperty("timesout");
		String url = prop.getProperty("url");

		wutil.MaximizeTheWindow(driver);
		
		// Convert timeout to integer
		wutil.ImplicetWait(driver, timesout);
		
		wutil.NavigateToUrl(driver, url);
		
		Loginpage log = new Loginpage(driver);
		log.getUsernameTF(user);      // Fixed: call sendKeys on the WebElement
		log.getPassnameTF(pass);      // Fixed: call sendKeys
		log.getLoginbutton();            // Fixed: click the login button
	}
	
	@AfterMethod
	public void logout() {
		HomePage home = new HomePage(driver);
		home.getsignout(driver);
	}
	
	@AfterClass
	public void closeTheBrowser() {
		if (driver != null) {
			wutil.quit(driver);
		}
	}
	
	@AfterTest
	public void disconParellelConnection() {
		Reporter.log("Close The Parellel Execution", true);
	}
	
	@AfterSuite
	public void closeDatabaseConnection() throws SQLException {
		dutil.CloseDataBase();
	}
}
