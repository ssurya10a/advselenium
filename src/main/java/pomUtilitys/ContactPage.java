package pomUtilitys;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import lisernrsUtility.Listeners;

public class ContactPage {
	// Initialization
	@FindBy(xpath = "//img[@title = 'Create Contact...']")
	private WebElement newcon;

	/*
	 * @FindBy(xpath =
	 * "//a[@title='Organizations' and text()='wv']/ancestor::tr/child::td/child::a[text()='del']"
	 * ) private WebElement delete;
	 */

	// Declaration
	public ContactPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// Utilization
	public void getNewcon() {
		Listeners.test.log(Status.INFO, "Clicked 'Create Contact' button");
		newcon.click();
	}
}
