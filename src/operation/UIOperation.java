package operation;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class UIOperation {
	WebDriver driver;

	public UIOperation(WebDriver driver){
		this.driver = driver;
	}
	
	private Logger logger = Logger.getLogger("flightReservation");
/*
 * Function Name : launchApplication
 * Description : This function invokes Application Under Test
 * Parameters : url[String]
 * Return Type : void
 */
		
		public void launchApplication(String url){
			try{
			logger.info("Invoking Application Under Test : "+url);
			driver.get(url);
			driver.manage().window().maximize();
			logger.info("Invoked Application Under Test : "+url);
		}catch(Exception ex){
			logger.error("Exception occurred while launching Application Under Test : "+ex.getMessage());
		
		}
		}

/*
 * Function Name : click
 * Description : This function clicks on the specified web element
 * Parameters : locator[By],objectName[String]
 * Return Type : void
 */

		public void click(By locator,String objectName){
			try{
			logger.info("Clicking on '"+objectName+"'");
			WebElement elemntToBeClicked = driver.findElement(locator);
			highlightElement(elemntToBeClicked);
			elemntToBeClicked.click();
			logger.info("Clicked on '"+objectName+"'");
			}catch(Exception ex){
			logger.error("Exception occurred while clicking on '"+objectName+"' : "+ex.getMessage());	
		}
		}
			
/*
 * Function Name : setText
 * Description : This function enters a specified value in the specified web edit
 * Parameters : locator[By],objectName[String],value[String]
 * Return Type : void
 */			
	public void setText(By locator,String objectName,String value){
		try{
		logger.info("Entering '"+value+"' in the '"+objectName+"' edit field");
			WebElement elemntToBeSet = driver.findElement(locator);
			highlightElement(elemntToBeSet);
			elemntToBeSet.sendKeys(value);
			logger.info("Entered '"+value+"' in the '"+objectName+"' edit field");
		}catch(Exception ex){
			logger.error("Exception occurred while entering '"+value+"' in the '"+objectName+"' edit field : "+ex.getMessage());
		}
		
	}

/*
 * Function Name : select
 * Description : This function selects a specified value from the specified web list
 * Parameters : locator[By],objectName[String],value[String]
 * Return Type : void
 */	
	
	
	
	public void select(By locator,String objectName,String value){
		try{
		logger.info("Selecting '"+value+"' from the '"+objectName+"' drop down");
		WebElement elemntToBeSelected = driver.findElement(locator);
		highlightElement(elemntToBeSelected);
		Select select = new Select(elemntToBeSelected);
		select.selectByVisibleText(value);
		logger.info("Selected '"+value+"' from the '"+objectName+"' drop down");
		}catch(Exception ex){
			logger.error("Exception occurred while selecting '"+value+"' from the '"+objectName+"' drop down : "+ex.getMessage());
		}
	}
	
/*
 * Function Name : verifyTitle
 * Description : This function verifies page title and return a boolean value
 * Parameters : expTitle[String]
 * Return Type : boolean [true/false]
 */	
			
	public boolean verifyTitle(String expTitle){
	try{	
	  logger.info("Verifying Page Title : "+expTitle);
		if(driver.getTitle().equals(expTitle)){
			logger.info("Verified Page Title : "+expTitle);
			return true;	
		}else{
			return false;
		}
		
	}catch(Exception ex){
		logger.error("Exception occurred while verifying page title : "+expTitle+" : "+ex.getMessage());
		return false;
	}
	
}
	
/*
 * Function Name : verifyElementPresent
 * Description : This function verifies whether a specified web element is present on the web page or not
 * Parameters : locator[By],objectName[String]
 * Return Type : boolean [true/false]
 */		
	
	
	public boolean verifyElementPresent(By locator, String objectName){
	try{		
	 logger.info("Verifying the presence of '"+objectName+"' on web page");
			
	 if(driver.findElement(locator).isDisplayed()){
		 highlightElement(driver.findElement(locator));
		return true;		
	 }else{
		return false;
				
	 }
	}catch(Exception ex){
		logger.error("Exception occurred while verifying the presence of '"+objectName+"' on web page : "+ex.getMessage());
		return false;
	}
}
	
/*
 * Function Name : verifyText
 * Description : This function verifies whether a specified text is present on the web page or not
 * Parameters : expText[String]
 * Return Type : boolean [true/false]
 */		
	
public boolean verifyText(String expText){
		try{
	
	 logger.info("Verifying the presence of '"+expText+"' on web page");
	 if(driver.getPageSource().contains(expText)){
		return true;		
	}else{
		return false;
	}
		}catch(Exception ex){
			logger.error("Exception occurred while verifying the presence of '"+expText+"' on web page : "+ex.getMessage());
			return false;
		}
			
	}	
	
	public void highlightElement(WebElement element){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].setAttribute('Style','background: yellow; border: 2px solid red;');", element);
	}

}
