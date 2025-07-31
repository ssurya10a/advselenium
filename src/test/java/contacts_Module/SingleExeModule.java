package contacts_Module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SingleExeModule {
	@Parameters("browser")
	@Test(groups = "smoke")
	public void Createcontact() throws EncryptedDocumentException, IOException, InterruptedException {

		// 🔹 Step 1: Load contact data from Excel
		FileInputStream fis = new FileInputStream("./src/test/resources/testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		String conname = wb.getSheet("cont_data").getRow(1).getCell(2).toString();

		// 🔹 Step 2: Load configuration from properties file
		FileInputStream pfis = new FileInputStream("./src/test/resources/commondata.properties");
		Properties prop = new Properties();
		prop.load(pfis);

		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		long timeout = Long.parseLong(prop.getProperty("timesout"));

		// 🔹 Step 3: Set up WebDriver
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

		// 🔹 Step 4: Navigate to application and log in
		driver.get(url);
		driver.findElement(By.name("user_name")).sendKeys(id);
		driver.findElement(By.name("user_password")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();

		// 🔹 Step 5: Navigate to "Contacts" module
		driver.findElement(By.linkText("Contacts")).click();

		// 🔹 Step 6: Create new contact
		driver.findElement(By.xpath("//img[@title='Create Contact...']")).click();
		driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(conname);
		driver.findElement(By.xpath("//input[@title='Save [Alt+S]']")).click();

		// 🔹 Step 7: Go back to Contacts list
		driver.findElement(By.linkText("Contacts")).click();

		// 🔹 Step 8: Verify if contact is created
		String contact = driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname + "']")).getText();

		if (contact.contains(conname)) {
			// 🔸 Contact found: delete it
			driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname
					+ "']/../following-sibling::td/child::a[text()='del']")).click();
			System.out.println("Contact created with Contact name: " + conname);

			// 🔹 Mark result as success in Excel
			wb.getSheet("cont_data").getRow(1).createCell(3).setCellValue(true);

			// 🔹 Confirm deletion in alert
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			// 🔸 Contact not found
			System.out.println("Contact not created with Contact name: " + conname);

			// 🔹 Mark result as success in Excel
			wb.getSheet("cont_data").getRow(1).createCell(3).setCellValue(false);
		}

		// 🔹 Step 9: Write result back to Excel
		FileOutputStream fos = new FileOutputStream("./src/test/resources/testdata.xlsx");
		wb.write(fos);

		// 🔹 Step 10: Log out from the application
		Thread.sleep(3000);
		WebElement signout = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
		new Actions(driver).moveToElement(signout).perform();

		Thread.sleep(3000); // Allow dropdown to load
		driver.findElement(By.linkText("Sign Out")).click();

		// 🔹 Step 11: Cleanup
		wb.close();
		Thread.sleep(3000);
		driver.quit();
	}
	
	@Parameters("browser")
	@Test(groups = "reg")
	public void CreateContactAndDate() throws EncryptedDocumentException, IOException, InterruptedException {

		// 🔹 Step 1: Load contact data from Excel
		FileInputStream fis = new FileInputStream("./src/test/resources/testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		String conname = wb.getSheet("cont_data").getRow(1).getCell(2).toString();

		// 🔹 Step 2: Load configuration from properties file
		FileInputStream pfis = new FileInputStream("./src/test/resources/commondata.properties");
		Properties prop = new Properties();
		prop.load(pfis);

		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		long timeout = Long.parseLong(prop.getProperty("timesout"));

		// 🔹 Step 3: Set up WebDriver
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

		// 🔹 Step 4: Navigate to application and log in
		driver.get(url);
		driver.findElement(By.name("user_name")).sendKeys(id);
		driver.findElement(By.name("user_password")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();

		// 🔹 Step 5: Navigate to "Contacts" module
		driver.findElement(By.linkText("Contacts")).click();

		// 🔹 Step 6: Create new contact
		driver.findElement(By.xpath("//img[@title='Create Contact...']")).click();
		driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(conname);

		// add start date to the contact
		WebElement sdate = driver.findElement(By.name("support_start_date"));
		sdate.clear();

		// fetch todays date
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String sformattedDate = today.format(formatter);
		sdate.sendKeys(sformattedDate);

		// add end date to the contact
		WebElement edate = driver.findElement(By.name("support_end_date"));
		edate.clear();

		// fetch the date after 30 days
		LocalDate futureDate = today.plusDays(30);
		String eformattedDate = futureDate.format(formatter);
		edate.sendKeys(eformattedDate);

		driver.findElement(By.xpath("//input[@title='Save [Alt+S]']")).click();

		// 🔹 Step 7: Go back to Contacts list
		driver.findElement(By.linkText("Contacts")).click();

		// 🔹 Step 8: Verify if contact is created
		String contact = driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname + "']")).getText();

		if (contact.contains(conname)) {
			// 🔸 Contact found: delete it
			driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname
					+ "']/../following-sibling::td/child::a[text()='del']")).click();
			System.out.println("Contact created with Contact name: " + conname);

			// 🔹 Mark result as success in Excel
			wb.getSheet("cont_data").getRow(1).createCell(3).setCellValue(true);

			// 🔹 Confirm deletion in alert
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			// 🔸 Contact not found
			System.out.println("Contact not created with Contact name: " + conname);

			// 🔹 Mark result as success in Excel
			wb.getSheet("cont_data").getRow(1).createCell(3).setCellValue(false);
		}

		// 🔹 Step 9: Write result back to Excel
		FileOutputStream fos = new FileOutputStream("./src/test/resources/testdata.xlsx");
		wb.write(fos);

		// 🔹 Step 10: Log out from the application
		Thread.sleep(3000);
		WebElement signout = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
		new Actions(driver).moveToElement(signout).perform();

		Thread.sleep(3000); // Allow dropdown to load
		driver.findElement(By.linkText("Sign Out")).click();

		// 🔹 Step 11: Cleanup
		wb.close();
		Thread.sleep(3000);
		driver.quit();
	}

	@Parameters("browser")
	@Test(groups = "reg")
	public void CreatecontactAndOrgame() throws EncryptedDocumentException, IOException, InterruptedException {

		// Load test data from Excel file
		FileInputStream fis = new FileInputStream("./src/test/resources/testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);

		// Get contact name from sheet "cont_data", row 2 (index 1), column 3 (index 2)
		String orgname = wb.getSheet("cont_data").getRow(7).getCell(2).toString();
		String conname = wb.getSheet("cont_data").getRow(7).getCell(3).toString();

		// Load application configuration from properties file
		FileInputStream pfis = new FileInputStream("./src/test/resources/commondata.properties");
		Properties prop = new Properties();
		prop.load(pfis);

		// Get properties like URL, user ID, password, and timeout
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String times = prop.getProperty("timesout");

		// Launch Chrome browser
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();

		// Convert timeout to long and apply implicit wait
		long time = Long.parseLong(times);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));

		// Navigate to the application login page
		driver.get(url);

		// Perform login
		driver.findElement(By.name("user_name")).sendKeys(id);
		driver.findElement(By.name("user_password")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();

		// Click on "Contacts" tab to navigate to the contacts module
		driver.findElement(By.linkText("Contacts")).click();

		// Click on the "Create Contact..." button
		driver.findElement(By.xpath("//img[@title=\"Create Contact...\"]")).click();

		// Enter contact's last name (required field)
		driver.findElement(By.xpath("//input[@name=\"lastname\"]")).sendKeys(conname);

		// Enter Org name (required field)
		driver.findElement(By.id("title")).sendKeys(orgname);

		driver.findElement(By.xpath("//img[@title=\"Select\"]")).click();

		Thread.sleep(3000);

		/*
		 * // other way to handle the windows String parentHandle =
		 * driver.getWindowHandle(); Set<String> allHandles = driver.getWindowHandles();
		 * 
		 * for (String handle : allHandles) { if (!handle.equals(parentHandle)) {
		 * driver.switchTo().window(handle); break; } }
		 */

		// Get all window handles and convert to a List
		List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());

		driver.switchTo().window(windowHandles.get(1));

		driver.findElement(By.linkText("vtigeruser")).click();

		driver.switchTo().window(windowHandles.get(0));

		// Click Save to create the contact
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		// Navigate back to Contacts list
		driver.findElement(By.linkText("Contacts")).click();

		// Get the created contact name from the list
		String contact = driver.findElement(By.xpath("//a[@title=\"Contacts\" and text() = \"" + conname + "\"]"))
				.getText();

		// If contact is present, delete it
		if (contact.contains(conname)) {
			driver.findElement(By.xpath("//a[@title=\"Contacts\" and text() = \"" + conname
					+ "\"]/../following-sibling::td/child::a[text()=\"del\"]")).click();
			System.out.println("Contact created with Contact name and Org name ");
			wb.getSheet("cont_data").getRow(7).createCell(4).setCellValue(true);
			// Accept the alert that pops up after clicking delete
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			System.out.println("Contact not created with Contact name and Org name ");
			wb.getSheet("cont_data").getRow(7).createCell(4).setCellValue(false);
		}

		// Wait before sign out
		Thread.sleep(3000);

		// Use Actions class to perform mouse hover for logout
		Actions act = new Actions(driver);

		// Locate the user icon for hover (top-right)
		WebElement signout = driver.findElement(By.xpath("//img[@src=\"themes/softed/images/user.PNG\"]"));

		// Alternate working XPath if needed:
		// //td[@class="genHeaderSmall"]/following-sibling::td[@class="small"]/child::img[@src="themes/softed/images/user.PNG"]

		FileOutputStream fos = new FileOutputStream("./src/test/resources/testdata.xlsx");

		wb.write(fos);

		// Perform mouse hover on the user icon
		act.moveToElement(signout).perform();

		Thread.sleep(3000); // Wait for dropdown to show

		// Click on "Sign Out"
		driver.findElement(By.linkText("Sign Out")).click();

		// Close the workbook
		wb.close();

		// Wait before closing the browser
		Thread.sleep(3000);

		// Quit the browser
		driver.quit();
	}

	@Parameters("browser")
	@Test(groups = "reg")
	public void CreateOrgnWithDdtNameAndPhno() throws InterruptedException, IOException {
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
		WebDriver driver = new ChromeDriver();

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
