package pomUtilitys;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import lisernrsUtility.Listeners;

public class CreateContact {
	// Initialization
	@FindBy(name = "lastname")
	private WebElement lastname;
	
	@FindBy(name = "support_end_date")
	private WebElement enddate;
	
	@FindBy(name = "support_start_date")
	private WebElement startdate;
	
	@FindBy(xpath = "//input[@title='Save [Alt+S]']")
	private WebElement save; 
	
	// Declaration
	public CreateContact(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	// Utilization
	public void getLastname(String lname) {
		Listeners.test.log(Status.INFO, "Entered Last Name: " + lname);
		lastname.sendKeys(lname);
	}

	public WebElement getEnddate() {
		Listeners.test.log(Status.INFO, "Accessed 'Support End Date' field");
		return enddate;
	}

	public WebElement getStartdate() {
		Listeners.test.log(Status.INFO, "Accessed 'Support Start Date' field");
		return startdate;
	}

	public void getSave() {
		Listeners.test.log(Status.INFO, "Clicked 'Save' button");
		save.click();
	}
}
