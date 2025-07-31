package optmizedCode;

import java.io.IOException;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import generateUtility.JavaUtility;
import generateUtility.PropertyFileUtility;
import generateUtility.WebdriverUtility;
import generateUtility.WorkbookFileUtility;
import lisernrsUtility.Listeners;
import pomUtilitys.HomePage;
import pomUtilitys.Loginpage;

public class CreatecontactAndOrgame {

	@Test(retryAnalyzer = lisernrsUtility.Retry.class)
	public void createContactWithnameandorg() throws EncryptedDocumentException, IOException, InterruptedException {

		JavaUtility jutil = new JavaUtility();
		int num = jutil.generateRandomNumber();
		Listeners.test.log(Status.INFO, "Generated random number: " + num);

		// Load test data from Excel file
		WorkbookFileUtility wb = new WorkbookFileUtility();
		wb.openExcel();	
		Listeners.test.log(Status.INFO, "Opened Excel file");

		String orgname = wb.getCellValue("cont_data", 7, 2) + num;
		String conname = wb.getCellValue("cont_data", 7, 3) + num;
		Listeners.test.log(Status.INFO, "Fetched test data: OrgName = " + orgname + ", ContactName = " + conname);

		// Load properties
		PropertyFileUtility prop = new PropertyFileUtility();
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String timeout = prop.getProperty("timesout");
		Listeners.test.log(Status.INFO, "Loaded properties");

		// Setup WebDriver
		WebDriver driver = new ChromeDriver();
		WebdriverUtility WUtil = new WebdriverUtility();
		WUtil.MaximizeTheWindow(driver);
		WUtil.ImplicetWait(driver, timeout);
		WUtil.NavigateToUrl(driver, url);
		Listeners.test.log(Status.INFO, "Launched browser and navigated to URL");

		// Login
		Loginpage log = new Loginpage(driver);
		log.getUsernameTF(id);
		log.getPassnameTF(pass);
		log.getLoginbutton();
		Listeners.test.log(Status.INFO, "Logged in successfully");

		// Navigate to Contacts
		HomePage home = new HomePage(driver);
		String heddertitle = driver.findElement(By.xpath("//tr/td/a[contains(text(), 'Home')]")).getText();
		Assert.assertEquals(heddertitle, "Home");
		Listeners.test.log(Status.INFO, "Verified Home page loaded");

		home.getContacts();
		Listeners.test.log(Status.INFO, "Navigated to Contacts page");

		driver.findElement(By.xpath("//img[@title=\"Create Contact...\"]")).click();
		Listeners.test.log(Status.INFO, "Clicked on Create Contact");

		// Enter contact data
		driver.findElement(By.xpath("//input[@name=\"lastname\"]")).sendKeys(conname);
		driver.findElement(By.id("title")).sendKeys(orgname);
		Listeners.test.log(Status.INFO, "Entered contact last name and organization title");

		// Open Org popup
		driver.findElement(By.xpath("//img[@title=\"Select\"]")).click();
		Listeners.test.log(Status.INFO, "Clicked on Select Org icon");

		// Fetch window handles and switch
		List<String> handles = WUtil.FetchWindowHandles(driver);
		WUtil.SwitchWindowChidWindow(driver, handles);
		Listeners.test.log(Status.INFO, "Switched to child window");

		driver.findElement(By.xpath("//tr/td/a[text()='wv']")).click();
		Listeners.test.log(Status.INFO, "Selected Organization 'wv'");

		WUtil.SwitchToParentWindow(driver, handles);
		Listeners.test.log(Status.INFO, "Switched back to parent window");

		// Click Save
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();
		Listeners.test.log(Status.INFO, "Clicked Save button");

		// Go back to Contacts
		driver.findElement(By.linkText("Contacts")).click();
		Listeners.test.log(Status.INFO, "Navigated back to Contacts");

		// Verify contact
		String contact = driver.findElement(By.xpath("//a[@title=\"Contacts\" and text() = \"" + conname + "\"]")).getText();

		if (contact.contains(conname)) {
			driver.findElement(By.xpath("//a[@title=\"Contacts\" and text() = \"" + conname
					+ "\"]/../following-sibling::td/child::a[text()=\"del\"]")).click();
			Listeners.test.log(Status.PASS, "Contact created with name: " + conname + " and Org: " + orgname);
			wb.setCellValue("cont_data", 7, 4, "true");

			// Accept alert using your utility
			WUtil.HandlePopUpAccept(driver);
			Listeners.test.log(Status.INFO, "Contact deleted and popup accepted");

		} else {
			Listeners.test.log(Status.FAIL, "Contact not found with name: " + conname);
			wb.setCellValue("cont_data", 7, 4, "false");
		}

		// Wait before sign out
		Thread.sleep(3000);

		// Sign out
		Thread.sleep(3000);
		home.getsignout(driver);
		Listeners.test.log(Status.INFO, "Signed out from application");

		// Save Excel
		wb.saveAndClose();
		Listeners.test.log(Status.INFO, "Saved and closed Excel");

		// Quit browser using your utility
		WUtil.quit(driver);
		Listeners.test.log(Status.INFO, "Closed browser session");
	}
}
