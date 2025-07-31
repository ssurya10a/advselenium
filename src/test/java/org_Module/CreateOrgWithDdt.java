package org_Module;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class CreateOrgWithDdt{

	@Test
	public void CreateOrgnWithDdt() throws InterruptedException, IOException {
		

		// Step 1: Load the Excel file containing test data
		FileInputStream efis = new FileInputStream("./src/test/resources/testdata.xlsx");

		// Step 2: Create a Workbook object from the file stream
		Workbook wb = WorkbookFactory.create(efis);

		// Step 3: Read organization name from sheet "org_data", row 2, column 3
		String orgname = wb.getSheet("org_data").getRow(1).getCell(2).toString();

		// Launch Chrome browser
		WebDriver driver = new ChromeDriver();


		// Click on the "Organizations" link
		driver.findElement(By.linkText("Organizations")).click();

		Thread.sleep(3000); // Pause to allow the page to load

		// Click on "Create Organization" button (plus icon)
		driver.findElement(By.xpath("//img[@title=\"Create Organization...\"]")).click();
		Thread.sleep(3000);

		// Enter the organization name fetched from Excel
		driver.findElement(By.name("accountname")).sendKeys(orgname);
		Thread.sleep(3000);

		// Click the Save button
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		Thread.sleep(3000);

		// Navigate back to the Organizations list to verify creation
		driver.findElement(By.linkText("Organizations")).click();
		Thread.sleep(3000);

		// Fetch the name of the created organization from the list
		String ogname = driver.findElement(By.xpath("(//a[text()=\"" + orgname + "\"])[2]")).getText();
		Thread.sleep(3000);

		// Compare retrieved name with original to confirm creation
		if (ogname.equals(orgname)) {

			System.out.println("Created a Organization with Organization name");

			// Click on the "del" link next to the organization to delete it
			driver.findElement(
					By.xpath("//a[text()=\"" + orgname + "\"]/../following-sibling::td/child::a[text()=\"del\"]"))
					.click();

			// Handle confirmation alert popup
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			System.out.println("Not created a Organization with Organization name");
		}

		Thread.sleep(8000); // Wait for deletion to complete

	}
}
