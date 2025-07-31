package pomUtilitys;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import lisernrsUtility.Listeners;

// Page Object Model class for the Login Page
public class Loginpage {

	// Declaration of WebElements using @FindBy annotation

	// Header element with link text "vtiger"
	@FindBy(linkText = "vtiger")
	private WebElement header;

	// Username text field
	@FindBy(name = "user_name")
	private WebElement usernameTF;

	// Password text field
	@FindBy(name = "user_password")
	private WebElement passnameTF;

	// Login button
	@FindBy(id = "submitButton")
	private WebElement loginbutton;

	// Constructor to initialize WebElements using PageFactory
	public Loginpage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// Method to return the text of the header
	public String getHeader() {
		Listeners.test.log(Status.INFO, "Fetching the login page header text");
		return header.getText();
	}

	// Method to input username into the username text field
	public void getUsernameTF(String user) {
		Listeners.test.log(Status.INFO, "Entering username into the username text field");
		usernameTF.sendKeys(user);
	}

	// Method to input password into the password text field
	public void getPassnameTF(String pass) {
		Listeners.test.log(Status.INFO, "Entering password into the password text field");
		passnameTF.sendKeys(pass);
	}

	// Method to click on the login button
	public void getLoginbutton() {
		Listeners.test.log(Status.INFO, "Clicking on the login button");
		loginbutton.click();
	}
}
