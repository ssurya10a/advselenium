package contacts_Module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
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
import org.testng.annotations.Test;

public class CreatecontactAndOrgame {

	@Test
	public void test1() throws EncryptedDocumentException, IOException, InterruptedException {

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
		 // other way to handle the windows 
		 String parentHandle = driver.getWindowHandle(); 
		 Set<String> allHandles = driver.getWindowHandles();
		 
		 for (String handle : allHandles) {
		 	if (!handle.equals(parentHandle)) {
		 		driver.switchTo().window(handle); break; 
		  		} 
		  	}
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
		String contact = driver.findElement(By.xpath("//a[@title=\"Contacts\" and text() = \"" + conname + "\"]")).getText();

		// If contact is present, delete it
		if (contact.contains(conname)) {
			driver.findElement(By.xpath("//a[@title=\"Contacts\" and text() = \"" + conname + "\"]/../following-sibling::td/child::a[text()=\"del\"]")).click();
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
}
