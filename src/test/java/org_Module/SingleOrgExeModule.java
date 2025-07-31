package org_Module;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SingleOrgExeModule {
	@Parameters("browser")
	@Test(groups = "smoke")
	public void createNewOrg(String browser) throws InterruptedException {

		// Initialize the Chrome browser
		WebDriver driver = switch (browser) {
		case "chrome" -> new ChromeDriver();
		case "firefox" -> new FirefoxDriver();
		case "edge" -> new EdgeDriver();
		default -> new ChromeDriver();
		};

		// Maximize the browser window
		driver.manage().window().maximize();

		// Set implicit wait time for all element interactions
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Open the login page of the application
		driver.get("http://localhost:8888/index.php?action=Login&module=Users");

		// Enter username in the login form
		driver.findElement(By.name("user_name")).sendKeys("admin");

		// Enter password in the login form
		driver.findElement(By.name("user_password")).sendKeys("tiger");

		// Click the login button
		driver.findElement(By.id("submitButton")).click();

		// Navigate to "Organizations" section
		driver.findElement(By.linkText("Organizations")).click();

		// Pause to let the page load
		Thread.sleep(3000);

		// Click the "Create Organization" icon (plus symbol)
		driver.findElement(By.xpath("//img[@title=\"Create Organization...\"]")).click();
		Thread.sleep(3000);

		// Enter organization name into the form field
		driver.findElement(By.name("accountname")).sendKeys("hello");
		Thread.sleep(3000);

		// Click the Save button to create the organization
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		Thread.sleep(3000);

		// Return to the Organizations list to verify creation
		driver.findElement(By.linkText("Organizations")).click();
		Thread.sleep(3000);

		// Capture the name of the newly created organization for validation
		String ogname = driver.findElement(By.xpath("(//a[text()=\"hello\"])[2]")).getText();
		Thread.sleep(3000);

		// If the organization name matches, proceed to delete it
		if (ogname.equals("hello")) {

			// Click on the delete (del) link for the created organization
			driver.findElement(By.xpath("//a[text()=\"hello\"]/../following-sibling::td/child::a[text()=\"del\"]"))
					.click();

			// Handle the alert popup confirmation for deletion
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}

		Thread.sleep(8000); // Pause before logout

		// Locate the logout area using class and valign attributes
		WebElement mouceonlogout = driver.findElement(By.xpath("//td[@class=\"small\" and @valign=\"bottom\"]"));

		// Create Actions class object to handle mouse events
		Actions actions = new Actions(driver);

		// Perform mouse hover to reveal logout option
		actions.moveToElement(mouceonlogout).perform();

		// Locate the "Sign Out" link from the expanded menu
		WebElement signout = driver.findElement(By.xpath("//*[@id=\"ondemand_sub\"]/table/tbody/tr[2]/td/a"));

		Thread.sleep(3000);

		// Click on the "Sign Out" link to logout
		signout.click();

		Thread.sleep(3000);

		// Close the browser
		driver.quit();
	}

	@Parameters("browser")
	@Test(groups = "reg")
	public void CreateOrgnWithDdt(String browser) throws InterruptedException, IOException {

		// Create Properties object to load configuration from .properties file
		Properties prop = new Properties();

		// Read the properties file as a stream
		FileInputStream fis = new FileInputStream("./src/test/resources/commondata.properties");

		// Load properties into the Properties object
		prop.load(fis);

		// Retrieve values from the properties file
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String time = prop.getProperty("timesout");

		// Step 1: Load the Excel file containing test data
		FileInputStream efis = new FileInputStream("./src/test/resources/testdata.xlsx");

		// Step 2: Create a Workbook object from the file stream
		Workbook wb = WorkbookFactory.create(efis);

		// Step 3: Read organization name from sheet "org_data", row 2, column 3
		String orgname = wb.getSheet("org_data").getRow(1).getCell(2).toString();

		// Launch Chrome browser
		// Initialize the Chrome browser
		WebDriver driver = switch (browser) {
		case "chrome" -> new ChromeDriver();
		case "firefox" -> new FirefoxDriver();
		case "edge" -> new EdgeDriver();
		default -> new ChromeDriver();
		};

		// Maximize the browser window
		driver.manage().window().maximize();

		// Convert timeout value (string) from properties file into long
		long timeout = Long.parseLong(time);

		// Set implicit wait for all findElement calls
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

		// Open the application URL
		driver.get(url);

		// Enter username from properties file
		driver.findElement(By.name("user_name")).sendKeys(id);

		// Enter password from properties file
		driver.findElement(By.name("user_password")).sendKeys(pass);

		// Click the login button
		driver.findElement(By.id("submitButton")).click();

		// Click on the "Organizations" link
		driver.findElement(By.linkText("Organizations")).click();

		Thread.sleep(3000); // Pause to allow the page to load

		// Click on "Create Organization" button (plus icon)
		driver.findElement(By.xpath("//img[@title=\"Create Organization...\"]")).click();
		Thread.sleep(3000);

		// Enter the organization name fetched from Excel
		driver.findElement(By.name("accountname")).sendKeys(orgname);
		Thread.sleep(3000);

		// Click the Save button
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		Thread.sleep(3000);

		// Navigate back to the Organizations list to verify creation
		driver.findElement(By.linkText("Organizations")).click();
		Thread.sleep(3000);

		// Fetch the name of the created organization from the list
		String ogname = driver.findElement(By.xpath("(//a[text()=\"" + orgname + "\"])[2]")).getText();
		Thread.sleep(3000);

		// Compare retrieved name with original to confirm creation
		if (ogname.equals(orgname)) {

			System.out.println("Created a Organization with Organization name");

			// Click on the "del" link next to the organization to delete it
			driver.findElement(
					By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]"))
					.click();

			// Handle confirmation alert popup
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			System.out.println("Not created a Organization with Organization name");
		}

		Thread.sleep(8000); // Wait for deletion to complete

		// Find the element where mouse needs to hover for logout
		WebElement mouceonlogout = driver
				.findElement(By.xpath("/html/body/table[1]/tbody/tr/td[3]/table/tbody/tr/td[2]"));

		// Create Actions object to perform mouse hover
		Actions actions = new Actions(driver);

		// Perform mouse hover on the logout area
		actions.moveToElement(mouceonlogout).perform();

		// Locate the "Sign Out" link
		WebElement signout = driver.findElement(By.xpath("//*[@id=\"ondemand_sub\"]/table/tbody/tr[2]/td/a"));

		// Close the Excel workbook
		wb.close();
		Thread.sleep(3000);

		// Click on "Sign Out"
		signout.click();
		Thread.sleep(3000);

		// Close the browser
		driver.quit();
	}
	

	@Parameters("browser")
	@Test(groups = "reg")
	public void CreateOrgWithDdtNameAndDropdown(String browser) throws Exception {

		// Load Excel file for test data
		FileInputStream fis = new FileInputStream("./src/test/resources/testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet("org_data");

		// Read organization name, industry, and type from row 8 (index 7)
		String orgname = sh.getRow(7).getCell(2).toString(); // Organization Name
		String industry = sh.getRow(7).getCell(3).toString(); // Industry dropdown value
		String orgnatypeme = sh.getRow(7).getCell(4).toString(); // Organization Type dropdown value

		// Load properties file for config (url, id, pass, timeout)
		FileInputStream pfis = new FileInputStream("./src/test/resources/commondata.properties");
		Properties p = new Properties();
		p.load(pfis); // Load from properties file

		// Fetch configuration data
		String url = p.getProperty("url");
		String id = p.getProperty("id");
		String pass = p.getProperty("pass");
		String time = p.getProperty("timesout");

		// Initialize the Chrome browser
		WebDriver driver = switch (browser) {
		case "chrome" -> new ChromeDriver();
		case "firefox" -> new FirefoxDriver();
		case "edge" -> new EdgeDriver();
		default -> new ChromeDriver();
		};

		// Convert timeout value to long and apply implicit wait
		long times = Long.parseLong(time);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(times));
		driver.manage().window().maximize();

		// Open application URL
		driver.get(url);

		// Login with username and password
		driver.findElement(By.xpath("//input[@name=\"user_name\"]")).sendKeys(id);
		driver.findElement(By.xpath("//input[@name=\"user_password\"]")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();

		// Navigate to Organizations module
		driver.findElement(By.linkText("Organizations")).click();

		// Click on 'Create Organization' icon
		driver.findElement(By.xpath("//img[@title=\"Create Organization...\"]")).click();

		// Fill in organization name
		driver.findElement(By.name("accountname")).sendKeys(orgname);

		// Locate and handle the Industry dropdown
		WebElement industryDropdown = driver.findElement(By.name("industry"));
		Select select = new Select(industryDropdown);
		select.selectByVisibleText(industry); // Select industry from dropdown

		// Locate and handle the Type dropdown
		WebElement tdropdown = driver.findElement(By.name("accounttype"));
		Select tsel = new Select(tdropdown);
		tsel.selectByVisibleText(orgnatypeme); // Select organization type from dropdown

		// Save the new organization
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		// Wait for save action to complete
		Thread.sleep(3000);

		// Navigate back to Organizations list
		driver.findElement(By.linkText("Organizations")).click();

		// Verify if the organization was created successfully
		String ogname = driver.findElement(By.xpath("(//a[text()=\"" + orgname + "\"])[2]")).getText();

		// If found, delete the organization
		if (orgname.equals(ogname)) {
			driver.findElement(
					By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]"))
					.click();

			// Handle the alert pop-up for deletion confirmation
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}

		Thread.sleep(3000); // Wait before logout

		// Locate the logout link area
		WebElement mouceonlogout = driver
				.findElement(By.xpath("/html/body/table[1]/tbody/tr/td[3]/table/tbody/tr/td[2]"));

		// Perform mouse hover using Actions class
		Actions actions = new Actions(driver);
		actions.moveToElement(mouceonlogout).perform();

		Thread.sleep(3000);

		// Click on 'Sign Out' from the dropdown
		driver.findElement(By.xpath("//a[@class=\"drop_down_usersettings\" and text()=\"Sign Out\"]")).click();

		// Close Excel workbook
		wb.close();

		// Quit the browser
		Thread.sleep(3000);
		driver.quit();
	}

	@Parameters("browser")
	@Test(groups = "reg")
	public void CreateOrgnWithDdtNameAndPhno(String browser) throws InterruptedException, IOException {
		// Create object of Properties class to read key-value pairs from .properties
		// file
		Properties prop = new Properties();

		// Convert physical properties file into Java-readable stream
		FileInputStream fis = new FileInputStream("./src/test/resources/commondata.properties");

		// Load the properties file into the Properties object
		prop.load(fis);

		// Fetching values from the properties file
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String time = prop.getProperty("timesout");

		// Load Excel file as input stream to fetch test data
		FileInputStream efis = new FileInputStream("./src/test/resources/testdata.xlsx");

		// Create Workbook object from input stream using Apache POI
		Workbook wb = WorkbookFactory.create(efis);

		// Read organization name from Excel sheet "org_data" (row 5, column 3)
		String orgname = wb.getSheet("org_data").getRow(4).getCell(2).toString();

		// Read phone number from same row, column 4
		String phno = wb.getSheet("org_data").getRow(4).getCell(3).toString();

		// Launching Chrome browser
		// Initialize the Chrome browser
		WebDriver driver = switch (browser) {
		case "chrome" -> new ChromeDriver();
		case "firefox" -> new FirefoxDriver();
		case "edge" -> new EdgeDriver();
		default -> new ChromeDriver();
		};
		// Maximize browser window
		driver.manage().window().maximize();

		// Convert timeout value from string to long
		long timeout = Long.parseLong(time);

		// Set implicit wait using converted timeout value
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

		// Open application URL
		driver.get(url);

		// Enter login credentials (username and password)
		driver.findElement(By.name("user_name")).sendKeys(id);
		driver.findElement(By.name("user_password")).sendKeys(pass);

		// Click on login button
		driver.findElement(By.id("submitButton")).click();

		// Navigate to "Organizations" section
		driver.findElement(By.linkText("Organizations")).click();

		// Pause to allow page load
		Thread.sleep(3000);

		// Click on "Create Organization" button
		driver.findElement(By.xpath("//img[@title=\"Create Organization...\"]")).click();
		Thread.sleep(3000);

		// Enter organization name in the input field
		driver.findElement(By.name("accountname")).sendKeys(orgname);

		// Enter phone number
		driver.findElement(By.id("phone")).sendKeys(phno);

		// Click on Save button
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		Thread.sleep(3000);

		// Go back to "Organizations" list
		driver.findElement(By.linkText("Organizations")).click();
		Thread.sleep(3000);

		// Fetch organization name from list for validation
		String ogname = driver.findElement(By.xpath("(//a[text()=\"" + orgname + "\"])[2]")).getText();

		// Fetch phone number for the organization from the list
		String phnum = driver.findElement(By.xpath("//td[text()= \"" + phno + " \"]")).getText();

		Thread.sleep(3000);

		// Validate if both organization name and phone number match the entered values
		if (ogname.equals(orgname) && phnum.equals(phno)) {

			System.out.println("Created a Organization with phone number and Organization name");

			// Click on delete link corresponding to the created organization
			driver.findElement(
					By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]"))
					.click();

			// Handle confirmation alert pop-up
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			System.out.println("Not created a Organization with phone number and Organization name");
		}

		// Wait before logging out
		Thread.sleep(8000);

		// Locate the logout section using XPath
		WebElement mouceonlogout = driver
				.findElement(By.xpath("/html/body/table[1]/tbody/tr/td[3]/table/tbody/tr/td[2]"));

		// Create Actions class object to perform mouse hover action
		Actions actions = new Actions(driver);
		actions.moveToElement(mouceonlogout).perform();

		// Locate and click on "Sign Out" link
		WebElement signout = driver.findElement(By.xpath("//*[@id=\"ondemand_sub\"]/table/tbody/tr[2]/td/a"));

		// Close the workbook
		wb.close();

		Thread.sleep(3000);

		// Click on Sign Out
		signout.click();

		// Close the browser
		Thread.sleep(3000);
		driver.quit();
	}
}
