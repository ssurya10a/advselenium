package contacts_Module;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class AllInOneTest {

	@Test
	public void test() throws EncryptedDocumentException, IOException, InterruptedException {
		
		// Step 1: Load Excel file for data-driven testing
		FileInputStream efos = new FileInputStream("./src/test/resources/testdata.xlsx");
		Workbook wb = WorkbookFactory.create(efos);

		// Get organization details from "org_data" sheet
		Sheet sh = wb.getSheet("org_data");
		String cname = sh.getRow(7).getCell(2).toString();        // Organization name
		String ctype = sh.getRow(7).getCell(4).toString();        // Organization type (dropdown)
		String cindustry = sh.getRow(7).getCell(3).toString();    // Industry type (dropdown)

		// Get contact details from "cont_data" sheet
		Sheet she = wb.getSheet("cont_data");
		String lname = she.getRow(7).getCell(2).toString();       // Last name of contact
		//String conname = she.getRow(7).getCell(3).toString();     // (Unused) Possibly full name or related info

		// Step 2: Load common data from properties file
		FileInputStream pfos = new FileInputStream("./src/test/resources/commondata.properties");
		Properties p = new Properties();
		p.load(pfos);  // Load key-value pairs

		// Get required configuration values from the properties file
		String url = p.getProperty("url");
		String id = p.getProperty("id");
		String pass = p.getProperty("pass");
		String time = p.getProperty("time");

		// Step 3: Setup and launch browser
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();

		// Apply implicit wait from config
		long times = Long.parseLong(time);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(times));

		// Navigate to the application URL
		driver.get(url);

		// Step 4: Perform login
		driver.findElement(By.name("user_name")).sendKeys(id);
		driver.findElement(By.name("user_password")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();

		// Step 5: Create Organization
		driver.findElement(By.linkText("Organizations")).click();
		Thread.sleep(3000); // Wait for page to load
		driver.findElement(By.xpath("//img[@title='Create Organization...']")).click();
		Thread.sleep(3000); // Wait for form to load

		// Enter organization name
		driver.findElement(By.name("accountname")).sendKeys(cname);

		// Select industry and type from dropdowns
		WebElement industry = driver.findElement(By.name("industry"));
		WebElement type = driver.findElement(By.name("accounttype"));
		Select sel = new Select(industry);
		sel.selectByVisibleText(cindustry);  // Select by visible text

		Select tsel = new Select(type);
		tsel.selectByVisibleText(ctype);     // ✅ FIXED: Correct method is selectByVisibleText, not selectByContainsVisibleText

		// Save the organization
		driver.findElement(By.xpath("//input[@title=\"Save [Alt+S]\"]")).click();

		// Step 6: Create Contact
		Thread.sleep(3000);
		driver.findElement(By.linkText("Contacts")).click(); // Navigate to Contacts
		Thread.sleep(3000);
		driver.findElement(By.xpath("//img[@title='Create Contact...']")).click(); // Click "Create Contact"
		driver.findElement(By.name("lastname")).sendKeys(lname); // Fill last name field

		// Close workbook after reading
		wb.close();
	}
}
