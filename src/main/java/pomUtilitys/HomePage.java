package pomUtilitys;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import lisernrsUtility.Listeners;

public class HomePage {
	// Initializing
	@FindBy(className = "hdrLink")
	private WebElement title;
	
	@FindBy(linkText = "Contacts")
	private WebElement Contacts;
	
	@FindBy(linkText = "Organizations")
	private WebElement Organizations;
	
	@FindBy(xpath = "//img[@src='themes/softed/images/user.PNG']")
	private WebElement signout;
	
	
	// Declaration 
	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// utilization
	public void gettitle() {
		Listeners.test.log(Status.INFO, "Fetching the home page title element");
		title.getText();
	}
	
	public void getContacts() {
		Listeners.test.log(Status.INFO, "Clicked on 'Contacts' module");
		Contacts.click();
	}

	public void getOrganizations() {
		Listeners.test.log(Status.INFO, "Clicked on 'Organizations' module");
		Organizations.click();
	}
	
	public void getsignout(WebDriver driver) {
		Listeners.test.log(Status.INFO, "Attempting to sign out of the application");
		Actions actions = new Actions(driver);
		actions.moveToElement(signout).perform();
		WebElement logout = driver.findElement(By.xpath("//*[@id='ondemand_sub']/table/tbody/tr[2]/td/a"));
		logout.click();
	}	
	
}
