package optmizedCode;

import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import generateUtility.JavaUtility;
import generateUtility.PropertyFileUtility;
import generateUtility.WebdriverUtility;
import generateUtility.WorkbookFileUtility;
import lisernrsUtility.Listeners;
import pomUtilitys.ContactPage;
import pomUtilitys.CreateContact;
import pomUtilitys.HomePage;
import pomUtilitys.Loginpage;

public class CreateContactAndDate {

    @Test(retryAnalyzer = lisernrsUtility.Retry.class)
    public void test1() throws EncryptedDocumentException, IOException, InterruptedException {
    	
    	// try pull
    	// try push with changes
        // Create JavaUtility instance to generate random number and date values
        JavaUtility jutil = new JavaUtility();
        int num = jutil.generateRandomNumber();

        // Log test start
        Listeners.test.log(Status.INFO, "Test started: Create contact and set date");

        // Open Excel workbook and fetch contact name with appended random number
        WorkbookFileUtility wb = new WorkbookFileUtility();
        wb.openExcel();
        String conname = wb.getCellValue("cont_data", 1, 2) + num;
        Listeners.test.log(Status.INFO, "Fetched contact name from Excel: " + conname);

        // Load properties like URL, username, password, and timeout
        PropertyFileUtility prop = new PropertyFileUtility();
        String url = prop.getProperty("url");
        String id = prop.getProperty("id");
        String pass = prop.getProperty("pass");
        String timeout = prop.getProperty("timesout");
        Listeners.test.log(Status.INFO, "Loaded properties file");

        // Launch browser and apply window and timeout settings
        WebDriver driver = new ChromeDriver();
        WebdriverUtility WUtil = new WebdriverUtility();

        WUtil.MaximizeTheWindow(driver);
        Listeners.test.log(Status.INFO, "Browser window maximized");

        WUtil.ImplicetWait(driver, timeout);
        Listeners.test.log(Status.INFO, "Implicit wait applied");

        WUtil.NavigateToUrl(driver, url);
        Listeners.test.log(Status.INFO, "Navigated to URL: " + url);

        // Perform login using POM
        Loginpage log = new Loginpage(driver);
        log.getUsernameTF(id);
        log.getPassnameTF(pass);
        log.getLoginbutton();
        Listeners.test.log(Status.INFO, "Logged in with credentials");

        // Verify Home page loaded
        HomePage home = new HomePage(driver);
        String heddertitle = driver.findElement(By.xpath("//tr/td/a[contains(text(), 'Home')]")).getText();
        Assert.assertEquals(heddertitle, "Home");
        Listeners.test.log(Status.PASS, "Verified Home page loaded");

        // Navigate to Contacts module
        home.getContacts();
        Listeners.test.log(Status.INFO, "Navigated to Contacts module");

        // Click to create a new contact
        ContactPage createcon = new ContactPage(driver);
        createcon.getNewcon();
        Listeners.test.log(Status.INFO, "Clicked on create new Contact");

        // Fill in contact last name
        CreateContact newcontact = new CreateContact(driver);
        newcontact.getLastname(conname);
        Listeners.test.log(Status.INFO, "Entered last name: " + conname);

        // Set Start Date
        WebElement sdate = newcontact.getStartdate();
        sdate.clear();
        String sformattedDate = jutil.getSystemCurrentDate();
        sdate.sendKeys(sformattedDate);
        Listeners.test.log(Status.INFO, "Entered Start Date: " + sformattedDate);

        // Set End Date (30 days later)
        WebElement edate = newcontact.getEnddate();
        edate.clear();
        String eformattedDate = jutil.getDateAfterSpecifiedDays(30);
        edate.sendKeys(eformattedDate);
        Listeners.test.log(Status.INFO, "Entered End Date: " + eformattedDate);

        // Save the new contact
        newcontact.getSave();
        Listeners.test.log(Status.INFO, "Clicked Save on new contact form");

        // Navigate back to Contacts list
        home.getContacts();
        Listeners.test.log(Status.INFO, "Navigated back to Contacts list");

        // Verify the contact is present in the list using dynamic XPath
        String contact = driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname + "']")).getText();

        // Use SoftAssert for verification
        SoftAssert sart = new SoftAssert();
        sart.assertEquals(contact, conname);
        sart.assertAll();
        Listeners.test.log(Status.PASS, "Verified contact created: " + contact);

        // If contact exists, delete it and update Excel status
        if (contact.contains(conname)) {
            driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname
                    + "']/../following-sibling::td/child::a[text()='del']")).click();
            Listeners.test.log(Status.INFO, "Deleted created contact: " + conname);

            wb.setCellValue("cont_data", 1, 3, "true"); // Mark as deleted
            WUtil.HandlePopUpAccept(driver);
            Listeners.test.log(Status.PASS, "Contact deletion confirmed");
        } else {
            // If contact not found, mark deletion as failed
            Listeners.test.log(Status.FAIL, "Contact not found: " + conname);
            wb.setCellValue("cont_data", 1, 3, "false");
        }

        // Save and close the workbook
        wb.saveAndClose();
        Listeners.test.log(Status.INFO, "Excel file updated with result");

        // Wait for UI to stabilize before logout
        Thread.sleep(3000);

        // Log out of the application
        home.getsignout(driver);
        Listeners.test.log(Status.INFO, "Logged out from the application");

        // Close the browser
        WUtil.quit(driver);
        Listeners.test.log(Status.INFO, "Closed the browser and ended test");
    }
}
