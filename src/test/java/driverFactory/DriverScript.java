package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	public static WebDriver driver;
	String inputpath="./FileInput/DataEngine.xlsx";
	String outputpath="./FileOutput/HybridResults.xlsx";
	ExtentReports report;
	ExtentTest logger;
	String TestCases="MasterTestCases";//sheet stored in a ref variable
	public void startTest() throws Throwable
	{
		String Module_Status="";
		//create object for excel file util class
		ExcelFileUtil xl=new ExcelFileUtil(inputpath);
		//iterate all rows in MasterTestCases
		for(int i=1;i<=xl.rowCount(TestCases);i++)
		{
			//which ever testcase flag to Y execute them
			if (xl.getCellData(TestCases, i, 2).equalsIgnoreCase("Y")) 
			{
				//store each test case into TCModule
				String TCModule=xl.getCellData(TestCases, i, 1);
				report = new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.generateDate()+".html");
				logger=report.startTest(TCModule);

				//iterate all rows in each sheet TCModule
				for (int j = 1; j <=xl.rowCount(TCModule); j++) 
				{
					//call cells from TCModule
					String Description=xl.getCellData(TCModule, j, 0);
					String Object_Type=xl.getCellData(TCModule, j, 1);
					String Locator_Type=xl.getCellData(TCModule, j, 2);
					String Locator_Value=xl.getCellData(TCModule, j, 3);
					String Test_Data=xl.getCellData(TCModule, j, 4);
					try {
						if(Object_Type.equalsIgnoreCase("startBrowser")) 
						{
							driver =FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("openUrl")) 
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("waitForElement")) 
						{
							FunctionLibrary.waitForElement(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("typeAction")) 
						{
							FunctionLibrary.typeAction(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("clickAction")) 
						{
							FunctionLibrary.clickAction(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("validateTitle")) 
						{
							FunctionLibrary.validateTitle(Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("closeBrowser")) 
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropDownAction(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureStockNum")) 
						{
							FunctionLibrary.captureStockNum(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("capturesupnum"))
						{
							FunctionLibrary.capturesupnum(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("capturecusnum"))
						{
							FunctionLibrary.capturecusnum(Locator_Type, Locator_Value);
						}
						if(Object_Type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						//write as pass into Status cell in TCModule sheet
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_Status="True";
					}catch(Exception e) 
					{
						System.out.println(e.getMessage());
						//write as Fail into Status cell in TCModule sheet
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_Status="False";
					}
					if(Module_Status.equalsIgnoreCase("True")) 
					{
						xl.setCellData(TestCases, i, 3, "Pass", outputpath);
					}
					else 
					{
						xl.setCellData(TestCases, i, 3, "Fail", outputpath);

					}
					report.endTest(logger);
					report.flush();
				} 
			}
			else 
			{
				//Which ever test case flag to N write as blocked into status cell in MasterTestCases
				xl.setCellData(TestCases, i, 3, "Blocked", outputpath);
			}
		}
	}
}
