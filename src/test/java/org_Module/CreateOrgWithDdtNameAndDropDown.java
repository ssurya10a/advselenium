package org_Module;

import java.io.FileInputStream;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class CreateOrgWithDdtNameAndDropDown {

	@Test
	public void CreateOrgWithDdtNameAndDropdown() throws Exception {

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

		// Launch the Chrome browser
		WebDriver driver = new ChromeDriver();

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

		// Locate the logout section using XPath
		WebElement mouceonlogout = driver
				.findElement(By.xpath("/html/body/table[1]/tbody/tr/td[3]/table/tbody/tr/td[2]"));

		// Create Actions class object to perform mouse hover action
		Actions actions = new Actions(driver);
		actions.moveToElement(mouceonlogout).perform();

		// Locate and click on "Sign Out" link
		WebElement signout = driver.findElement(By.xpath("//*[@id=\"ondemand_sub\"]/table/tbody/tr[2]/td/a"));

		// Close Excel workbook
		wb.close();

		Thread.sleep(3000);

		// Click on Sign Out
		signout.click();
		// Quit the browser
		Thread.sleep(3000);
		driver.quit();
	}
}
