package lisernrsUtility;

import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import baseClassUtility.BaseClass;

public class Listeners implements ITestListener, ISuiteListener  {

	public ExtentSparkReporter spark;
	public static ExtentTest test;
	public ExtentReports report;

	@Override
	public void onTestStart(ITestResult result) {
		String testname = result.getMethod().getMethodName();
		Reporter.log(testname + " Test Execution Started", true);
		test.log(Status.INFO, "Test Started: " + testname);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testname = result.getMethod().getMethodName();
		Reporter.log(testname + " Test Execution Success", true);
		test.log(Status.PASS, "Test Passed: " + testname);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testname = result.getMethod().getMethodName();
		String time =  new Date().toString().replace(":", "_").replace(" ", "_");
		Reporter.log(testname + " Test Execution Failure", true);
		test.log(Status.FAIL, "Test Failed: " + testname + " at " + time);
		TakesScreenshot tks = (TakesScreenshot) BaseClass.driver;
		String ss = tks.getScreenshotAs(OutputType.BASE64);
		test.addScreenCaptureFromBase64String(ss, "Screenshot - " + testname + " at " + time);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testname = result.getMethod().getMethodName();
		Reporter.log(testname + " Test Execution Skipped", true);
		test.log(Status.SKIP, "Test Skipped: " + testname);
	}

	@Override
	public void onStart(ISuite suite) {
		Reporter.log("Suite Execution started - Configure the Reports", true);

		// configure the advance report
		String time =  new Date().toString().replace(":", "_").replace(" ", "_");
		spark = new ExtentSparkReporter("./AdavanceReports/Report_" + time + ".html");
		spark.config().setDocumentTitle("VTiger CRM Project");
		spark.config().setReportName("Test Report");
		spark.config().setTheme(Theme.DARK);

		report = new ExtentReports();
		report.attachReporter(spark);
		report.setSystemInfo("OS", "WINDOWS-11");
		report.setSystemInfo("Browser", "CHROME-134.264.96");

		test = report.createTest("Suite Initialization");
		test.log(Status.INFO, "Report initialized successfully");
	}

	@Override
	public void onFinish(ISuite suite) {
		Reporter.log("Suite Execution ended - Backup the Reports", true);
		test.log(Status.INFO, "Suite Execution Completed. Report flushed.");
		report.flush();
	}
}
