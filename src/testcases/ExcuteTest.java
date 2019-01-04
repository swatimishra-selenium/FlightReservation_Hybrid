package testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import excelInputAndOutput.ExcelInteraction;
import operation.ReadObject;
import operation.UIOperation;
import utility.Constant;


public class ExcuteTest {
	WebDriver driver;
	UIOperation operation;
	ReadObject object;
	ExcelInteraction excel;
	Properties allObjects;
	@Parameters({"browser"})
	@BeforeTest
	public void setUp(String browser) throws IOException{
		if(browser.equals("chrome")){
			System.setProperty("webdriver.chrome.driver",Constant.chromeDriverPath);
			driver = new ChromeDriver();
		}else if(browser.equals("firefox")){
			System.setProperty("webdriver.gecko.driver", Constant.geckoDriverPath);
			driver = new FirefoxDriver();
		}
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		operation = new UIOperation(driver);
		object = new ReadObject();
		allObjects = object.getObjectRepository();
		excel = new ExcelInteraction();
		
}
	
	@Test(priority=0)
	private void launchFlightReservationApplication() throws IOException{
		// Invoke Application Under Test
		String url = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "InvokeApplication", 1, 0);
		operation.launchApplication(url);
		String expTitle = 	excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "InvokeApplication", 1, 1);	
		Assert.assertTrue(operation.verifyTitle(expTitle));
		
		
	}
	
	// Verify that 16 number of navigation links are present on home page
	//@Test(priority=1)
	@Test(dependsOnMethods="launchFlightReservationApplication")
	private void verifyLinks(){
		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println("Number of links on home page : "+links.size());
		
		
		Assert.assertEquals(links.size(), 16);
		
		for(WebElement l1 : links){
			operation.highlightElement(l1);
			System.out.println("Link Text = "+l1.getText());
		}
	}
	
	//@Test(priority=2)
	@Test(dependsOnMethods="verifyLinks")
	private void verifyWebTable(){
		int rowCount = driver.findElements(By.xpath("(//table)[10]/tbody/tr")).size();
		int colCount = driver.findElements(By.xpath("(//table)[10]/tbody/tr[1]/td")).size();
		ArrayList<String> expValues = new ArrayList<String>();
		expValues.add("Atlanta to Las Vegas");
		expValues.add("$398");
		expValues.add("Boston to San Francisco");
		expValues.add("$513");
		expValues.add("Los Angeles to Chicago");
		expValues.add("$168");
		expValues.add("New York to Chicago");
		expValues.add("$198");
		expValues.add("Phoenix to San Francisco");
		expValues.add("$213");
		
		ArrayList<String> actValues = new ArrayList<String>();
		for(int i=1;i<=rowCount;i++){
			for(int j=1;j<=colCount;j++){
				operation.highlightElement(driver.findElement(By.xpath("(//table)[10]/tbody/tr["+i+"]/td["+j+"]")));
				System.out.print(driver.findElement(By.xpath("(//table)[10]/tbody/tr["+i+"]/td["+j+"]")).getText()+" | ");
				actValues.add(driver.findElement(By.xpath("(//table)[10]/tbody/tr["+i+"]/td["+j+"]")).getText());
			}
			System.out.println();
		}
		
		Assert.assertTrue(expValues.equals(actValues));
		
		
	}
	
	// Verify that Login To Flight Reservation is successful after entering valid credentials
	//@Test(priority=3)
	@Test(dependsOnMethods="verifyWebTable")
	private void login() throws IOException{
		// Enter UserName
		String  userName = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "Login", 1, 0);
		operation.setText(By.name(allObjects.getProperty("UserName")), "UserName", userName);
	
		// Enter Password
		String password = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "Login", 1, 1);
		operation.setText(By.name(allObjects.getProperty("Password")), "Password", password);
		
		// Click on 'Sign-In'
		operation.click(By.name(allObjects.getProperty("Sign-In")), "Sign-In");
		String expTitle = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "Login", 1, 2);
		
		Assert.assertTrue(operation.verifyTitle(expTitle));
	}
	
	// Verify that User should be able to reserve a flight after entering valid flight details
	//@Test(priority=4)
	@Test(dependsOnMethods="login")
	public void reserveAFlight() throws IOException{
		// Select Trip Type
		operation.click(By.cssSelector(allObjects.getProperty("Oneway")), "Oneway");
		
		// Select Departure From
		String departureFrom = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "FlightDetails", 1, 0);
		operation.select(By.name(allObjects.getProperty("DepartureFrom")), "DepartureFrom", departureFrom);
				
		// Select Arrival To
		String arrivalTo = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "FlightDetails", 1, 1);
		operation.select(By.name(allObjects.getProperty("ArrivalTo")), "ArrivalTo", arrivalTo);
				
				
		// Select Class Preference
		
		operation.click(By.xpath(allObjects.getProperty("First")), "First");
				
		// Select Airline Preference
		String airlinePreference = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "FlightDetails", 1, 2);
		operation.select(By.name(allObjects.getProperty("AirlinePreference")), "AirlinePreference", airlinePreference);
				
		// Click on 'CONTINUE'
				
		operation.click(By.name(allObjects.getProperty("CONTINUE1")), "CONTINUE1");
		Assert.assertTrue(operation.verifyElementPresent(By.name(allObjects.getProperty("CONTINUE2")), "CONTINUE2"));
				
		// Click on 'CONTINUE'
		operation.click(By.name(allObjects.getProperty("CONTINUE2")), "CONTINUE2");
		
		
	}
	
	// Verify that user should be able to book a ticket after entering valid passenger details
	//@Test(priority=5)
	@Test(dependsOnMethods="reserveAFlight")
	private void bookATicket() throws IOException{
		// Enter First Name
		String firstName = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "PersonalDetails", 1, 0);
		operation.setText(By.name(allObjects.getProperty("FirstName")), "FirstName", firstName);
		// Enter Last Name
		String lastName = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "PersonalDetails", 1, 1);
		operation.setText(By.name(allObjects.getProperty("LastName")), "LastName", lastName);
		// Enter Credit Card Number
		String cardNumber = excel.getCellData(System.getProperty("user.dir")+"//TestData", "FR.xls", "PersonalDetails", 1, 2);
		operation.setText(By.name(allObjects.getProperty("CreditCard")), "CreditCard", cardNumber);
		// Click on 'SECURE PURCHASE'
		operation.click(By.name(allObjects.getProperty("SECUREPURCHASE")), "SECUREPURCHASE");		
		operation.highlightElement(driver.findElement(By.xpath(allObjects.getProperty("ConfirmationText"))));
		Assert.assertTrue(operation.verifyTitle("Flight Confirmation: Mercury Tours"));

		
	}
	
	
	@Test
	public void testSkip() {
		throw new SkipException("This test got skipped as it was not ready to be executed.");
	}
	
	@Test
	public void testFail() {
		Assert.assertFalse(true);
	}
	
	@AfterTest
	public void closeDriver(){
		driver.quit();
	}

}
