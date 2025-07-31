package optmizedCode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class CreateOrgWithDdtNameAndDropDown {

	@Test(retryAnalyzer = lisernrsUtility.Retry.class)
	public void DropdownAndOrgNamePhno() throws Exception {

		JavaUtility jutil = new JavaUtility();
		int num = jutil.generateRandomNumber();
		Listeners.test.log(Status.INFO, "Generated random number: " + num);

		// Load Excel data
		WorkbookFileUtility wb = new WorkbookFileUtility();
		wb.openExcel();
		Listeners.test.log(Status.INFO, "Opened Excel file");

		String orgname = wb.getCellValue("org_data", 7, 2) + num;
		String industry = wb.getCellValue("org_data", 7, 3);
		String orgnatypeme = wb.getCellValue("org_data", 7, 4);
		Listeners.test.log(Status.INFO, "Fetched data from Excel - OrgName: " + orgname + ", Industry: " + industry + ", Type: " + orgnatypeme);

		// Load properties
		PropertyFileUtility prop = new PropertyFileUtility();
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		String time = prop.getProperty("timesout");
		Listeners.test.log(Status.INFO, "Loaded properties");

		// Launch browser and apply utility methods
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
		Listeners.test.log(Status.INFO, "Logged in successfully");

		// Navigate to Organizations
		HomePage home = new HomePage(driver);
		String heddertitle = driver.findElement(By.xpath("//tr/td/a[contains(text(), 'Home')]")).getText();
		Assert.assertEquals(heddertitle, "Home");
		Listeners.test.log(Status.INFO, "Verified Home page loaded");

		home.getOrganizations();
		Listeners.test.log(Status.INFO, "Navigated to Organizations page");

		OrganizationsPage org = new OrganizationsPage(driver);
		org.getNeworg();
		Listeners.test.log(Status.INFO, "Clicked to create a new Organization");

		// Enter org name
		CreateOrg corg = new CreateOrg(driver);
		corg.getName(orgname);
		Listeners.test.log(Status.INFO, "Entered organization name: " + orgname);

		// Handle industry dropdown
		WebElement industryDropdown = corg.getIndustry();
		WUtil.SelectDropDownByValue(driver, industryDropdown, industry);
		Listeners.test.log(Status.INFO, "Selected industry: " + industry);

		Thread.sleep(3000);

		// Handle type dropdown
		WebElement typeDropdown = corg.getAccounttype();
		WUtil.SelectDropDownByValue(driver, typeDropdown, orgnatypeme);
		Listeners.test.log(Status.INFO, "Selected account type: " + orgnatypeme);

		// Save the organization
		corg.getsave();
		Listeners.test.log(Status.INFO, "Clicked Save button");

		Thread.sleep(3000);

		// Go back to Organizations
		home.getOrganizations();
		Listeners.test.log(Status.INFO, "Navigated back to Organizations list");

		// Verify organization creation
		String ogname = driver.findElement(By.xpath("(//a[text()=\"" + orgname + "\"])[2]")).getText();

		if (orgname.equals(ogname)) {
			driver.findElement(
					By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]"))
					.click();

			WUtil.HandlePopUpAccept(driver);
			Listeners.test.log(Status.PASS, "Organization created and deleted: " + orgname);
		} else {
			Listeners.test.log(Status.FAIL, "Organization not created: " + orgname);
		}

		Thread.sleep(3000);

		// Sign out
		Thread.sleep(3000);
		home.getsignout(driver);
		Listeners.test.log(Status.INFO, "Signed out successfully");

		wb.saveAndClose();
		Listeners.test.log(Status.INFO, "Excel file saved and closed");

		Thread.sleep(3000);
		WUtil.quit(driver);
		Listeners.test.log(Status.INFO, "Browser closed");
	}
}
