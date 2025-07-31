package contacts_Module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Createcontact {

	@Test
	public void test1() throws EncryptedDocumentException, IOException, InterruptedException {

		// 🔹 Step 1: Load contact data from Excel
		FileInputStream fis = new FileInputStream("./src/test/resources/testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		String conname = wb.getSheet("cont_data").getRow(1).getCell(2).toString();

		// 🔹 Step 2: Load configuration from properties file
		FileInputStream pfis = new FileInputStream("./src/test/resources/commondata.properties");
		Properties prop = new Properties();
		prop.load(pfis);

		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pass = prop.getProperty("pass");
		long timeout = Long.parseLong(prop.getProperty("timesout"));

		// 🔹 Step 3: Set up WebDriver
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

		// 🔹 Step 4: Navigate to application and log in
		driver.get(url);
		driver.findElement(By.name("user_name")).sendKeys(id);
		driver.findElement(By.name("user_password")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();

		// 🔹 Step 5: Navigate to "Contacts" module
		driver.findElement(By.linkText("Contacts")).click();

		// 🔹 Step 6: Create new contact
		driver.findElement(By.xpath("//img[@title='Create Contact...']")).click();
		driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(conname);
		driver.findElement(By.xpath("//input[@title='Save [Alt+S]']")).click();

		// 🔹 Step 7: Go back to Contacts list
		driver.findElement(By.linkText("Contacts")).click();

		// 🔹 Step 8: Verify if contact is created
		String contact = driver.findElement(
				By.xpath("//a[@title='Contacts' and text()='" + conname + "']")).getText();

		if (contact.contains(conname)) {
			// 🔸 Contact found: delete it
			driver.findElement(By.xpath("//a[@title='Contacts' and text()='" + conname + "']/../following-sibling::td/child::a[text()='del']")).click();
			System.out.println("Contact created with Contact name: " + conname);

			// 🔹 Mark result as success in Excel
			wb.getSheet("cont_data").getRow(1).createCell(3).setCellValue(true);

			// 🔹 Confirm deletion in alert
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} else {
			// 🔸 Contact not found
			System.out.println("Contact not created with Contact name: " + conname);
			
			// 🔹 Mark result as success in Excel
			wb.getSheet("cont_data").getRow(1).createCell(3).setCellValue(false);
		}

		// 🔹 Step 9: Write result back to Excel
		FileOutputStream fos = new FileOutputStream("./src/test/resources/testdata.xlsx");
		wb.write(fos);

		// 🔹 Step 10: Log out from the application
		Thread.sleep(3000);
		WebElement signout = driver.findElement(By.xpath("//*[@id=\"ondemand_sub\"]/table/tbody/tr[2]/td/a"));

		Thread.sleep(3000); // Allow dropdown to load

		// Click on Sign Out
		signout.click();


		// 🔹 Step 11: Cleanup
		wb.close();
		Thread.sleep(3000);
		driver.quit();
	}
}
