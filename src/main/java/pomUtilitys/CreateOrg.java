package pomUtilitys;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import lisernrsUtility.Listeners;

public class CreateOrg {
	// Initializing
	@FindBy(name = "accountname")
	private WebElement name;

	@FindBy(name = "industry")
	private WebElement industry;

	@FindBy(name = "accounttype")
	private WebElement accounttype;

	@FindBy(id = "phone")
	private WebElement phone;
	
	@FindBy(xpath = "//input[@title='Save [Alt+S]']")
	private WebElement save;

	// Declaration
	public CreateOrg(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// utilization
	public void getName(String orgname) {
		Listeners.test.log(Status.INFO, "Entered Organization Name: " + orgname);
		name.sendKeys(orgname);
	}

	public WebElement getIndustry() {
		Listeners.test.log(Status.INFO, "Accessed 'Industry' dropdown");
		return industry;
	}

	public WebElement getAccounttype() {
		Listeners.test.log(Status.INFO, "Accessed 'Account Type' dropdown");
		return accounttype;
	}

	public void getPhone(String orgphno) {
		Listeners.test.log(Status.INFO, "Entered Phone Number: " + orgphno);
		phone.sendKeys(orgphno);
	}

	public void getsave() throws InterruptedException {
		Listeners.test.log(Status.INFO, "Clicked on 'Save' button");
		Thread.sleep(3000);
		save.click();
	}
}
