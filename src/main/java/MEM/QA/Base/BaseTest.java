package MEM.QA.Base;

import java.io.File;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class BaseTest {

	public static WebDriver driver;
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest logger;


	@BeforeTest
	public void beforetestmethod() 
	{

		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+File.separator+"reports"+File.separator+"MEMPROJECTExtentReport.html");
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		sparkReporter.config().setTheme(Theme.DARK);

		sparkReporter.config().setDocumentTitle("Personalization Report");
		sparkReporter.config().setReportName("Automation Test Result by Shailesh");
		sparkReporter.config().setTimeStampFormat("EEEE,MMMM,DD,YYYY, HH:mm a '('zzz')' ");

		logger = extent.createTest("Test_ValidationPersonalizationFunction");
	}

	@AfterMethod
	public void aftermethod(ITestResult result) 
	{

		if(result.getStatus()== ITestResult.FAILURE) {
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+"-Test Case Failed", ExtentColor.RED));
		}
		else if (result.getStatus() == ITestResult.SKIP) 
		{
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+"-Test Case Skipped", ExtentColor.ORANGE));
		}
		else if(result.getStatus() == ITestResult.SUCCESS) 
		{
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+"-Test Case pass", ExtentColor.GREEN));}

		if (driver != null) {
			driver.quit();
		}
	}
}
