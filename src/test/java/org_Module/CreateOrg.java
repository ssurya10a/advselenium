package org_Module;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class CreateOrg {

	@Test
	public void createNewOrg() throws InterruptedException {

		// Initialize the Chrome browser
		WebDriver driver = new ChromeDriver();

		// Maximize the browser window
		driver.manage().window().maximize();

		// Set implicit wait time for all element interactions
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Open the login page of the application
		driver.get("http://localhost:8888/index.php?action=Login&module=Users");

		// Enter username in the login form
		driver.findElement(By.name("user_name")).sendKeys("admin");

		// Enter password in the login form
		driver.findElement(By.name("user_password")).sendKeys("tiger");

		// Click the login button
		driver.findElement(By.id("submitButton")).click();

		// Navigate to "Organizations" section
		driver.findElement(By.linkText("Organizations")).click();

		// Pause to let the page load
		Thread.sleep(3000);

		// Click the "Create Organization" icon (plus symbol)
		driver.findElement(By.xpath("//img[@title=\"Create Organization...\"]")).click();
		Thread.sleep(3000);

		// Enter organization name into the form field
		driver.findElement(By.name("accountname")).sendKeys("hello");
		Thread.sleep(3000);

		// Click the Save button to create the organization
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		Thread.sleep(3000);

		// Return to the Organizations list to verify creation
		driver.findElement(By.linkText("Organizations")).click();
		Thread.sleep(3000);

		// Capture the name of the newly created organization for validation
		String ogname = driver.findElement(By.xpath("(//a[text()=\"hello\"])[2]")).getText();
		Thread.sleep(3000);

		// If the organization name matches, proceed to delete it
		if (ogname.equals("hello")) {

			// Click on the delete (del) link for the created organization
			driver.findElement(By.xpath("//a[text()=\"hello\"]/../following-sibling::td/child::a[text()=\"del\"]")).click();

			// Handle the alert popup confirmation for deletion
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}

		Thread.sleep(8000); // Pause before logout

		// Locate the logout area using class and valign attributes
		WebElement mouceonlogout = driver.findElement(By.xpath("//td[@class=\"small\" and @valign=\"bottom\"]"));

		// Create Actions class object to handle mouse events
		Actions actions = new Actions(driver);

		// Perform mouse hover to reveal logout option
		actions.moveToElement(mouceonlogout).perform();

		// Locate the "Sign Out" link from the expanded menu
		WebElement signout = driver.findElement(By.xpath("//*[@id=\"ondemand_sub\"]/table/tbody/tr[2]/td/a"));

		Thread.sleep(3000);

		// Click on the "Sign Out" link to logout
		signout.click();

		Thread.sleep(3000);

		// Close the browser
		driver.quit();
	}
}
