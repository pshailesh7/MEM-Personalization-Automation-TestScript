package Test_Script;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVWriter;

import MEM.QA.Base.BaseTest;
import MEM.QA.DataBaseConfig.DataBaseConnection;
import MEM.QA.DataBaseConfig.MerchantProduct;
import MEM.QA.DataBaseConfig.Store;
import Utility.ModelCSV;

public class Test_ValidationPersonalizationFunction extends BaseTest {
	
	
	List<ModelCSV> csvList = new ArrayList<ModelCSV>();

	@Test
	public void customize_field_validation() {
		logger = extent.createTest("geting url from data base");

		Connection conn = null;
		try {
			DataBaseConnection databaseconnection = new DataBaseConnection();
			conn = DataBaseConnection.getDbConnection();
			Statement statement = conn.createStatement();
			List<Store> storeList = databaseconnection.getStoreList();

			List<String> urlList = new ArrayList<String>();
			for (Store store : storeList) {
				int pageNumber = 1;
				int pageSize = 50;
				int offset;
				boolean isStop = true;

				while (isStop) {
					offset = (pageNumber - 1) * pageSize;
					List<MerchantProduct> merchantProductList = databaseconnection.getMerchantProducts(store, statement,
							pageSize, offset);

					if (merchantProductList.size() > 0) {

						urlList = databaseconnection.getMasterProducts(merchantProductList, statement, store, pageSize,
								offset);
						if (urlList.size() > 0) {

							test_function(urlList);
						}
						pageNumber++;
					} else {
						isStop = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("Exception while closing database connection" + e.getMessage());
					e.printStackTrace();
				}
			}

		}
	}

//	public void test_function(Store store, DataBaseConnection databaseConnection, List<String> urlList,
//			Statement statement, int pageNumber, int pageSize, int offset) {
	public void test_function(List<String> urlList) throws InterruptedException {
		
		System.out.println("done");
		
		try {
			for (String url : urlList) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless","--window-size=1920,1080");
				System.setProperty("webdriver.chrome.driver","C:\\Users\\lucentinnovation\\eclipse-workspace\\My_Easy_Monogram1\\Driver\\chromedriver.exe");
				ChromeDriver driver = new ChromeDriver();
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
				driver.get(url);
				
				ModelCSV csv = new ModelCSV();
				
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				if (isElementVisible(driver, By.xpath("//div[@class='preview-root-app-wrapper']"))) {
					
					logger = extent.createTest("Verify Personalize Button");
					try {
						wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='preview-root-app-wrapper']//child::button"))).click();					
						} 
					catch (Exception e) {
						csv.setProductDomain(url);
						csv.setException(e.getMessage());
						csvList.add(csv);
						
					}

					if (isElementPresent(driver, By.xpath("//div[@class='preview-modal-box-wrapper']"))) {

						long startTime = System.currentTimeMillis();
						String currentDate = getCurrentDate();
						boolean restartLoop = true;
						while (restartLoop) {
							// SELCT COLOR FIELD
							if (isElementPresent(driver, By.xpath("//span[text()='Which color would you like?']"))) {

								try {
									logger = extent.createTest("Verify color selection");
									wait.until(
									ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='preview-modal-box-wrapper']//div[@value='Black']"))).click();
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='footer-wrapper']//button[contains(text(),'Next')]"))).click();
									logger.log(Status.PASS, "color selection done");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL, "color selection fail Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}

							// SELECT COLOR FIELD
							if (isElementPresent(driver, By.xpath("//span[text()='Which Color would you like?']"))) {
								try {
									logger = extent.createTest("verify color selection");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@style='display: initial;']//child::div[@role='button']"))).click();
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(),'Black')]"))).click();
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='footer-wrapper']//button[contains(text(),'Next')]"))).click();
									logger.log(Status.PASS, "color selection done successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL, "color selection fail Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}

							// SELECT SIZE FIELD
							if (isElementPresent(driver, By.xpath("//div[text()='Size']"))) {
								try {
									logger = extent.createTest("verify size selection");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@style='display: initial;']//child::div[@role='button']"))).click();
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[normalize-space()='30\"']"))).click();
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='footer-wrapper']//button[contains(text(),'Next')]"))).click();
									logger.log(Status.PASS, "size selection done successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL, "size selection fail Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}

							// TOP TEXT FIELD
							if (isElementPresent(driver, By.xpath("//div[text()='Top text']"))) {
								try {
									logger = extent.createTest("verify top text field");
									driver.findElement(By.xpath("//div[@class='preview-modal-box-wrapper']//child::input[@name='properties[Top Text]']")).sendKeys("LUCENT");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]"))).click();
									logger.log(Status.PASS, "Top Text Entered Successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.PASS, "Top Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}

							// MIDDLE TEXT FIELD
							if (isElementPresent(driver, By.xpath("//div[text()='Middle text']"))) {
								try {
									logger = extent.createTest("verify Middle Text");
									driver.findElement(By.xpath("//div[@class='preview-modal-box-wrapper']//input[@name='properties[Middle Text]']")).sendKeys("INNOVATION");
									wait.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")))
											.click();
									logger.log(Status.PASS, "Middle Text Entered successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL, "Middle Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}

							// BOTTOM TEXT FIELD
							if (isElementPresent(driver, By.xpath("//div[text()='Bottom text']"))) {
								try {
									logger = extent.createTest("Verify Bottom Text Field");
									
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='preview-modal-box-wrapper']//child::input[@name='properties[Bottom Text]']"))).sendKeys("MACHANIC");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]"))).click();
									logger.log(Status.PASS, "Bottom Text Entered succsfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL, "Bottom Text Not Entered Exception Occurs " + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}

							// SELCT NAME FIELD
							if (isElementPresent(driver, By.xpath("//div[text()='Name']"))) {
								try {
									logger = extent.createTest("Verify Name Field");
									driver.findElement(By.name("//div[@class='preview-modal-box-wrapper']//child::input[@name='properties[Name]']")).sendKeys("LUCENT");
									wait.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")))
											.click();
									logger.log(Status.PASS, "Name Text Entered Successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL, "Name Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// SELECT INITIAL FIELD

							if (isElementPresent(driver, By.xpath("//div[text()='Initial']"))) {
								try {
									logger = extent.createTest("Verify Initial Text Field");
									WebElement initial = driver.findElement(By.xpath("//div[@class='preview-modal-box-wrapper']//child::select[@name='properties[Initial]']"));
									Select selectinitial = new Select(initial);
									selectinitial.selectByValue("S");
									wait.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")))
											.click();
									logger.log(Status.PASS, "Selection of Initial Text successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Selection of Initial Text Field Exception Occurs" + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// SELECT TOP FEFT TEXT FIELD

							if (isElementPresent(driver, By.xpath("//div[text()='Top left']"))) {
								try {
									logger = extent.createTest("Verify Top Left Text Field");
									driver.findElement(By.name("properties[Top Left]")).sendKeys("Lucent");
									wait.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")))
											.click();
									logger.log(Status.PASS, "Top Left Text Entered Successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Left Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// SELECT TOP RIGHT TEXT FIELD

							if (isElementPresent(driver, By.xpath("//div[text()='Top right']"))) {
								try {
									logger = extent.createTest("Verify Top Right Text Field");
									driver.findElement(By.name("properties[Top Right]")).sendKeys("LUCENT");
									wait.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")))
											.click();
									logger.log(Status.PASS, "Top Right Text Entered Successfully");
									restartLoop = true;
								} catch (Exception e) {
									logger.log(Status.FAIL,"Top Right Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
				
									break;
								}
							}
							
							// SELECT DOG STYLE

							if (isElementPresent(driver,
									By.xpath("//span[text()='Which Dog Style would you like?']"))) {
								try {
									logger = extent.createTest("Verify Dog Style");
									WebElement dogstyle = driver.findElement(By.name("properties[Dog Style]"));
									Select selectdogstyle = new Select(dogstyle);
									selectdogstyle.selectByValue("Wire-Hair");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")))
											.click();
									logger.log(Status.PASS, "Dog Style Select Successfully");
									restartLoop = true;
								} catch (Exception e) {
							
									logger.log(Status.FAIL,"Fail To Select Dog Style Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							

							// INCREASE QUANTITY

							if (isElementPresent(driver, By.xpath("//div[text()='Quantity']"))) {
								try {
									logger = extent.createTest("Verify Quantity Of The Product and Add to Cart");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='pointer forward-arrow-container']//*[name()='svg']"))).click();
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='footer-wrapper']//button[contains(text(),'Add to cart')]"))).click();
									logger.log(Status.PASS, "Quantity Of The Product and Add to Cart successfully");
									restartLoop = false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Unable to Increase Quantity and add to cart " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									
									break;
								}
							}
						
							long endTime = System.currentTimeMillis();
							long timeTaken = endTime - startTime;
							logger.log(Status.PASS, "Customization Successfully Done");
							System.out.println("this url " + url + " Time Taken For Operation " + formatTime(timeTaken));
							csv.setProductDomain(url);
							csv.setDate(currentDate);
							csv.setData(formatTime(timeTaken));
							csvList.add(csv);
							logger.log(Status.INFO, "time taken for operation" + formatTime(timeTaken));
							break;
							} 
						
						}
					driver.quit();
					
					}
				else
				{
					if (isElementPresent(driver, By.xpath("//div[@class='mema--mono--app']"))) {
						long startTime = System.currentTimeMillis();
						String currentDate = getCurrentDate();
						logger = extent.createTest("Verify Personalize Function");
						boolean restartLoop = true;
							// TOP TEXT FIELD
						while (restartLoop) {
							
							if (isElementPresent(driver, By.name("properties[Top Text]"))) {
								try {
									logger = extent.createTest("verify top text field");
									driver.findElement(By.name("properties[Top Text]")).sendKeys("LUCENT");
									logger.log(Status.PASS, "Top Text Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.PASS, "Top Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// MIDDLE TEXT FIELD

							if (isElementPresent(driver, By.name("properties[Middle Text]"))) {
								try {
									logger = extent.createTest("verify Middle Text");
									driver.findElement(By.name("properties[Middle Text]")).sendKeys("innovation");
									logger.log(Status.PASS, "Middle Text Entered successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL, "Middle Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// BOTTOM TEXT FIELD
							if (isElementPresent(driver, By.name("properties[Bottom Text]"))) {
								try {
									logger = extent.createTest("Verify Bottom Text Field");
									driver.findElement(By.name("properties[Bottom Text]")).sendKeys("mechanical");
									logger.log(Status.PASS, "Bottom Text Entered succsfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL, "Bottom Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// NAME TEXT FIELD
							if (isElementPresent(driver, By.name("properties[Name]"))) {
								try {
									logger = extent.createTest("Verify Name Field");
									driver.findElement(By.name("properties[Name]")).sendKeys("LUCENT");
									logger.log(Status.PASS, "Name Text Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL, "Name Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// SELECT INITIAL FIELD
							if (isElementPresent(driver, By.name("properties[Initial]"))) {
								try {
									logger = extent.createTest("Verify Initial Text Field");
									WebElement initial = driver.findElement(By.name("properties[Initial]"));
									Select selectinitial = new Select(initial);
									selectinitial.selectByValue("S");
									logger.log(Status.PASS, "Selection of Initial Text successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Selection of Initial Text Field Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// TOP LEFT TEXT

							if (isElementPresent(driver, By.name("properties[Top Left]"))) {
								try {
									logger = extent.createTest("Verify Top Left Text Field");
									driver.findElement(By.name("properties[Top Left]")).sendKeys("Top Left Text");
									logger.log(Status.PASS, "Top Left Text Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Left Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// TOP RIGHT TEXT

							if (isElementPresent(driver, By.name("properties[Top Right]"))) {
								try {
									logger = extent.createTest("Verify Top Right Text Field");
									driver.findElement(By.name("properties[Top Right]")).sendKeys("Top Right Text");
									logger.log(Status.PASS, "Top Right Text Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Right Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							//BANNER TEXT FIELD
							if (isElementPresent(driver, By.name("properties[Banner Text]"))) {
								try {
									logger = extent.createTest("Verify Banner Field");
									driver.findElement(By.name("properties[Banner Text]")).sendKeys("Top Right Text");
									logger.log(Status.PASS, "Banner Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Right Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							if (isElementPresent(driver, By.name("properties[Your Personalization]"))) {
								try {
									logger = extent.createTest("Verify Banner Field");
									driver.findElement(By.name("properties[Your Personalization]")).sendKeys("Lucent");
									logger.log(Status.PASS, "Banner Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Right Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							
							if (isElementPresent(driver, By.name("properties[Your Personalization]"))) {
								try {
									logger = extent.createTest("Verify Banner Field");
									driver.findElement(By.name("properties[Your Personalization]")).sendKeys("Lucent");
									logger.log(Status.PASS, "Banner Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Right Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							
							if (isElementPresent(driver, By.name("properties[Text]"))) {
								try {
									logger = extent.createTest("Verify Banner Field");
									driver.findElement(By.name("properties[Text]")).sendKeys("Lucent");
									logger.log(Status.PASS, "Text Entered Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,
											"Top Right Text Not Entered Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							// SELECT DOG STYLE

							if (isElementPresent(driver, By.name("properties[Dog Style]"))) {
								try {
									logger = extent.createTest("Verify Dog Style");
									WebElement dogstyle = driver.findElement(By.name("properties[Dog Style]"));
									Select selectdogstyle = new Select(dogstyle);
									selectdogstyle.selectByValue("Wire-Hair");
									wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]"))).click();
									logger.log(Status.PASS, "Dog Style Select Successfully");
									restartLoop=false;
								} catch (Exception e) {
									logger.log(Status.FAIL,"Fail To Select Dog Style Exception Occurs " + e.getCause());
									System.out.println(url + " An accurred Exception ;" + e.getCause());
									csv.setProductDomain(url);
									csv.setException(e.getMessage());
									csvList.add(csv);
									break;
								}
							}
							
							long endTime = System.currentTimeMillis();
							long timeTaken = endTime - startTime;
							logger.log(Status.PASS, "Customization Successfully Done");
							System.out.println("this url " + url + "Time Taken For Operation " + formatTime(timeTaken));
							csv.setProductDomain(url);
							csv.setDate(currentDate);
							csv.setData(formatTime(timeTaken));
							csvList.add(csv);
							logger.log(Status.INFO, " Time Taken for Operation" + formatTime(timeTaken));
							break;
						}
					
					}
					driver.quit();
				}
				generateCSV(csvList);
			}
			
		} catch (TimeoutException e) {
			System.out.println("page not loaded");
			
					}

	}
	private static String getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate.formatted(currentDate);
    }
	private static String formatTime(long millis) {
		
		long seconds = millis / 1000;
//		long minutes = seconds / 60;
		
		String executiontime =String.format("%02d.%02d", seconds % 60, millis % 1000) + " SECONDS ";
		
		return executiontime;
	}
	private static boolean isElementVisible(WebDriver driver, By by) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
	        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='preview-root-app-wrapper']")));
	        return true;
	    } catch (TimeoutException e) {
	        return false;
	    }
	}
	private static boolean isElementPresent(WebDriver driver, By by) {
		
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			
			return false;
		}
	}
	public void generateCSV(List<ModelCSV> csvlist) {
		String filepath = "PERSONALIZATION_FUNCTIONALITY_REPORT.csv";
		try (CSVWriter writer = new CSVWriter(new FileWriter(filepath))) {
			writer.writeNext(new String[] { "PRODUCT DOMAIN","DATE","TIME TAKEN FOR TESTING", "EXCEPTION" });
			for (ModelCSV modelcsv : csvList) {
				writer.writeNext(
						new String[] { modelcsv.getProductDomain(),modelcsv.getDate(),modelcsv.getData(), modelcsv.getException() });
			}
		} catch (IOException exception) {
			
		}
	}
	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();

		}
	}
}

