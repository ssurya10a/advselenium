package org_Module;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class CreateOrgWithDdtNameAndPhno {

	@Test
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
			driver.findElement(By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]")).click();

			// Handle confirmation alert pop-up
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}else {
			System.out.println("Not created a Organization with phone number and Organization name");
		}

		// Wait before logging out
		Thread.sleep(8000);

		// Locate the logout section using XPath
		WebElement mouceonlogout = driver.findElement(By.xpath("/html/body/table[1]/tbody/tr/td[3]/table/tbody/tr/td[2]"));

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
