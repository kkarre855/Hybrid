package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.FileWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;


import utilities.PropertyFileUtil;

public class FunctionLibrary {
	public static WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser() throws Throwable
	{
		if (PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome")) 
		{

			driver=new ChromeDriver();
			driver.manage().window().maximize();

		}
		else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox")) 
		{
			driver=new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is Not matching",true);
		}
		return driver;
	}

	//method for launch url
	public static void openUrl() throws Throwable
	{
		driver.get(PropertyFileUtil.getValueForKey("Url"));
	}

	//method for any wait for element
	public static void waitForElement(String LocatorType,String LocatorValue,String TestData) 
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if (LocatorType.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
		if (LocatorType.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if (LocatorType.equalsIgnoreCase("name")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}

	}

	//method for textboxes
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if (LocatorType.equalsIgnoreCase("id")) 
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
		if (LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if (LocatorType.equalsIgnoreCase("name")) 
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}


	}
	//methods for button.checkboxes,radioButtons.links and images
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if (LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		} 
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}

	}
	//method for page title validation
	public static void validateTitle(String Expected_Tile)
	{
		String Actual_Title=driver.getTitle();
		try {                      //use try catch for good error msg
			//we use this for string data type
			Assert.assertEquals(Actual_Title, Expected_Tile, "Title is not matching");
		} catch (AssertionError a) //a holding error message
		{
			System.out.println(a.getMessage());//we get detailed error message
		}	
	}
	//close browser method
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for generate date
	public static String generateDate()
	{
		Date date=new Date();
		DateFormat df=new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
		return df.format(date);//keep on retrun new format for date
	}

	//method for listboxes
	public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath")) 
		{
			int value=Integer.parseInt(TestData);
			Select element=new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("id")) 
		{
			int value=Integer.parseInt(TestData);
			Select element=new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}

		if(LocatorType.equalsIgnoreCase("name")) 
		{
			int value=Integer.parseInt(TestData);
			Select element=new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
	}
	//method for stock number capture into notepad
	public static void captureStockNum(String LocatorType,String LocatorValue) throws Throwable 
	{
		String stockNum="";
		if(LocatorType.equalsIgnoreCase("xpath")) 
		{
			stockNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name")) 
		{
			stockNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}

		if(LocatorType.equalsIgnoreCase("id")) 
		{
			stockNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw=new FileWriter("./CaptureData/stockNumber.txt"); //giving path
		BufferedWriter bw=new BufferedWriter(fw);// to allocate memory
		bw.write(stockNum);//will write
		bw.flush(); // to override data
		bw.close(); // to close
	}

	//method for stock table
	public static void stockTable() throws Throwable
	{
		FileReader fr=new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed());
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
		Thread.sleep(4000);
		String Act_Data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Exp_Data+"    "+Act_Data,true);
		try {
			Assert.assertEquals(Exp_Data, Act_Data,"Stock Number Is Not Matching");

		}catch(AssertionError a) 
		{
			System.out.println(a.getMessage());
		}
	}

	//method for capture supplier number
	public static void capturesupnum(String LocatorType,String LocatorValue) throws Throwable 
	{
		String supplierNum="";
		if(LocatorType.equalsIgnoreCase("xpath")) 
		{
			supplierNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name")) 
		{
			supplierNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}

		if(LocatorType.equalsIgnoreCase("id")) 
		{
			supplierNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw=new FileWriter("./CaptureData/supplierNumber.txt"); //saving in notepad
		BufferedWriter bw=new BufferedWriter(fw);// to allocate memory
		bw.write(supplierNum);//will write
		bw.flush(); // to override data
		bw.close(); // to close
	}
	//method for supplier table
	public static void supplierTable() throws Throwable
	{
		FileReader fr=new FileReader("./CaptureData/supplierNumber.txt");
		BufferedReader br=new BufferedReader(fr);
		String Exp_Data = br.readLine();
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed());
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
		Thread.sleep(4000);
		String Act_Data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_Data+"    "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data, "Supplier Number Is Not Matching");

		}catch(AssertionError a) 
		{
			System.out.println(a.getMessage());
		}
	}
	//method for capture customer number
	public static void capturecusnum(String LocatorType,String LocatorValue) throws Throwable 
	{
		String customerNum="";
		if(LocatorType.equalsIgnoreCase("xpath")) 
		{
			customerNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name")) 
		{
			customerNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}

		if(LocatorType.equalsIgnoreCase("id")) 
		{
			customerNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw=new FileWriter("./CaptureData/customerNumber.txt"); //saving in notepad
		BufferedWriter bw=new BufferedWriter(fw);// to allocate memory
		bw.write(customerNum);//will write
		bw.flush(); // to override data
		bw.close(); // to close
	}

	//method for customer table
	public static void customerTable() throws Throwable
	{
		FileReader fr=new FileReader("./CaptureData/customerNumber.txt");
		BufferedReader br=new BufferedReader(fr);
		String Exp_Data = br.readLine();
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed());
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
		Thread.sleep(4000);
		String Act_Data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Act_Data+"    "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data, "Customer Number Is Not Matching");

		}catch(AssertionError a) 
		{
			System.out.println(a.getMessage());
		}
	}


}












