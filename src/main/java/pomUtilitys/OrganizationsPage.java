package pomUtilitys;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import lisernrsUtility.Listeners;

public class OrganizationsPage {
	// Initialization
	@FindBy(xpath = "//img[@title = 'Create Organization...']")
	private WebElement neworg;

	/*
	 * 
	 * @FindBy(xpath = "//a[@title='Organizations' and text()='wv']/ancestor::tr/child::td/child::a[text()='del']"
	 * ) private WebElement delete;
	 * 
	 */

	// Declaration
	public OrganizationsPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// Utilization
	public void getNeworg() {
		Listeners.test.log(Status.INFO, "Cliced on new orgnaization");
		neworg.click();
	}
	
}
