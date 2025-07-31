package optmizedCode;

import java.io.IOException;
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
import pomUtilitys.CreateOrg;
import pomUtilitys.HomePage;
import pomUtilitys.Loginpage;
import pomUtilitys.OrganizationsPage;

public class CreateOrgWithDdtNameAndPhno {

	@Test(retryAnalyzer = lisernrsUtility.Retry.class)
	public void test() throws InterruptedException, IOException {
		JavaUtility jutil = new JavaUtility();
		int num = jutil.generateRandomNumber();
		Listeners.test.log(Status.INFO, "Generated random number: " + num);

		// Read common properties
		PropertyFileUtility prop = new PropertyFileUtility();
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String time = prop.getProperty("timesout");
		Listeners.test.log(Status.INFO, "Loaded property file data");

		// Read Excel data
		WorkbookFileUtility wb = new WorkbookFileUtility();
		wb.openExcel();
		Listeners.test.log(Status.INFO, "Opened Excel workbook");

		String orgname = wb.getCellValue("org_data", 4, 2) + num;
		String phno = wb.getCellValue("org_data", 4, 3);
		Listeners.test.log(Status.INFO, "Fetched data from Excel - OrgName: " + orgname + ", Phone: " + phno);

		// WebDriver setup and utility usage
		WebDriver driver = new ChromeDriver();
		WebdriverUtility WUtil = new WebdriverUtility();
		WUtil.MaximizeTheWindow(driver);
		WUtil.ImplicetWait(driver, time);
		WUtil.NavigateToUrl(driver, url);
		Listeners.test.log(Status.INFO, "Browser launched and navigated to URL");

		// Login
		Loginpage log = new Loginpage(driver);
		log.getUsernameTF(id);
		log.getPassnameTF(pass);
		log.getLoginbutton();
		Listeners.test.log(Status.INFO, "Logged into application");

		// Navigate to Organizations
		HomePage home = new HomePage(driver);
		String headerTitle = driver.findElement(By.xpath("//tr/td/a[contains(text(), 'Home')]")).getText();
		Assert.assertEquals(headerTitle, "Home");
		Listeners.test.log(Status.INFO, "Home page verified successfully");

		home.getOrganizations();
		Listeners.test.log(Status.INFO, "Navigated to Organizations module");
		Thread.sleep(3000);

		// Click on "Create Organization"
		OrganizationsPage org = new OrganizationsPage(driver);
		org.getNeworg();
		Listeners.test.log(Status.INFO, "Clicked on 'Create Organization'");
		Thread.sleep(3000);

		// Fill in organization name and phone number
		CreateOrg corg = new CreateOrg(driver);
		corg.getName(orgname);
		corg.getPhone(phno);
		Listeners.test.log(Status.INFO, "Entered Organization name and phone number");

		// Save
		corg.getsave();
		Listeners.test.log(Status.INFO, "Clicked save to create organization");
		Thread.sleep(3000);

		// Back to Organizations list
		home.getOrganizations();
		Listeners.test.log(Status.INFO, "Returned to Organizations list");
		Thread.sleep(3000);

		// Fetch name and phone from list
		String ogname = driver.findElement(By.xpath("(//a[text()=\"" + orgname + "\"])[2]")).getText();
		String phnum = driver.findElement(By.xpath("//td[text()= \"" + phno + " \"]")).getText();
		Thread.sleep(3000);

		if (ogname.equals(orgname) && phnum.equals(phno)) {
			Listeners.test.log(Status.PASS, "Organization created with name: " + orgname + " and phone: " + phno);
			System.out.println("Created an Organization with phone number and name");

			// Click on delete
			driver.findElement(
				By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]"))
				.click();

			// Accept alert using utility
			WUtil.HandlePopUpAccept(driver);
			Listeners.test.log(Status.INFO, "Deleted created organization and handled popup");
		} else {
			Listeners.test.log(Status.FAIL, "Organization creation verification failed");
			System.out.println("Not created an Organization with phone number and name");
		}

		Thread.sleep(8000);

		// Sign out
		Thread.sleep(3000);
		home.getsignout(driver);
		Listeners.test.log(Status.INFO, "Signed out of the application");

		// Close Excel
		wb.saveAndClose();
		Listeners.test.log(Status.INFO, "Excel file closed");

		Thread.sleep(3000);
		WUtil.quit(driver); // Close browser
		Listeners.test.log(Status.INFO, "Browser session closed");
	}
}
