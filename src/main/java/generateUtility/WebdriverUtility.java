package generateUtility;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class WebdriverUtility {
	// WebdriverUtility WUtil = new WebdriverUtility();
	
	public void MaximizeTheWindow(WebDriver driver) {
		driver.manage().window().maximize();
	}

	public void ImplicetWait(WebDriver driver, String time) {
		Long timeout = Long.parseLong(time);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
	}
	
	public void NavigateToUrl(WebDriver driver, String url) {
		driver.get(url);		
	}
	
	public void HandlePopUpAccept(WebDriver driver) throws InterruptedException {
		Thread.sleep(3000);
		driver.switchTo().alert().accept();
	}
	
	public void HandlePopUpReject(WebDriver driver) {
		driver.switchTo().alert().dismiss();
	}
	
	public void HandlePopUpSendKeys(WebDriver driver, String text) {
		driver.switchTo().alert().sendKeys(text);
	}
	
	public String HandlePopUpGetText(WebDriver driver) {
		String texts = driver.switchTo().alert().getText();
		return texts;
	}
	
	public void MouseOver(WebDriver driver, WebElement element) {
		Actions act = new Actions(driver);
		act.moveToElement(element).perform();
	}
	
	public void SelectDropDownByValue(WebDriver driver, WebElement ddele, String value ) {
		Select sel = new Select(ddele);
		sel.selectByValue(value);
	}
	
	public void SelectDropDownByIndex(WebDriver driver, WebElement ddele, int index ) {
		Select sel = new Select(ddele);
		sel.selectByIndex(index);
	}
	
	public void SelectDropDownByTitle(WebDriver driver, WebElement ddele, String text ) {
		Select sel = new Select(ddele);
		sel.selectByVisibleText(text);
	}
	
	public List<String> FetchWindowHandles(WebDriver driver) {
		List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
		return windowHandles;
	}
	
	public void SwitchWindowChidWindow(WebDriver driver, List<String> id) {
		driver.switchTo().window(id.get(1));
	}
	
	public void SwitchToParentWindow(WebDriver driver, List<String> id) {
		driver.switchTo().window(id.get(0));
	}
	
	public void SwitchTodefaultWindow(WebDriver driver, List<String> id) {
		driver.switchTo().defaultContent();
	}
	
	public void SwitchToChildWindowUsingTitle(WebDriver driver, String title) {
		Set<String> wids = driver.getWindowHandles();
		for(String s : wids) {
			driver.switchTo().window(s);
			if(driver.getTitle().contains(title)) {
				break;
			}
		}
	}
	
	public void ChildWindowUsingTitle(WebDriver driver, String title, Set<String> wids) {
		for(String s : wids) {
			driver.switchTo().window(s);
			if(driver.getTitle().contains(title)) {
				break;
			}
		}
	}
	
	public void SwitchToWindowUsingUrl(WebDriver driver, String url) {
		Set<String> wids = driver.getWindowHandles();
		for(String s : wids) {
			driver.switchTo().window(s);
			if(driver.getCurrentUrl().equals(url)) {
				break;
			}
		}
	}
	
	public void quit(WebDriver driver) {
		driver.quit();
	}
}
