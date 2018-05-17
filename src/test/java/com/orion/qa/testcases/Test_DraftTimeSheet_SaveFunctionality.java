package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orion.qa.base.OrionBase;
import com.orion.qa.pages.LoginPage;
import com.orion.qa.pages.TimeSheetEditPage;
import com.orion.qa.pages.TimeSheetMainPage;
import com.orion.qa.utils.CommonMethods;

public class Test_DraftTimeSheet_SaveFunctionality extends OrionBase{
	ArrayList<String> objTest;
	ArrayList<String> objGridData;

	int RowNumb;
	boolean isAttachmntExist, isSameFiles, isAttachmentdisabled;
	public String rptPeriod;
	
	public Test_DraftTimeSheet_SaveFunctionality() {
		super();
	}

	@Parameters({"Browser", "ClassName"})
	@BeforeClass
	public void InitObjects(String Browser, String ClassName) {
		System.out.println("********** Test_DraftTimeSheet_SaveFunctionality START ************* ");
		try {
			
			init(Browser, ClassName, true);

			log.info("********** Test_DraftTimeSheet_SaveFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			objTest = new ArrayList<String>();
			objGridData = new ArrayList<String>();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InitObjects "+ e.getMessage());

		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_DraftTimeSheet_SaveFunctionality END ************* ");
		log.info("********** Test_DraftTimeSheet_SaveFunctionality END *************");

	}

	@Test(dataProvider = "credentials", dataProviderClass = CommonMethods.class, priority = 1)
	public void Test_LoginToOrion_IsSuccess(String UserID, String Password) {
		try {
			log.info("Inside Test_LoginToOrion_IsSuccess method");
			log.debug("Setting User Credentials");

			LoginPage.txtbx_UserName(driver).sendKeys(UserID);
			LoginPage.txtbx_Password(driver).sendKeys(Password);
			log.debug("Login button click");

			LoginPage.btnLogin(driver).click();
			try {
				assertEquals(true, CommonMethods.lbl_LoginUserIcon(driver).isDisplayed());
				log.info("Login success");

			} catch (NoSuchElementException e) {
				log.error("Exception : Login button not found; Error occured: "+ e.getMessage());
				assertEquals(false, true);
			}
		} catch (Exception e) {
			log.error("Exception in method Test_LoginToOrion_IsSuccess : "+ e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 2, dependsOnMethods = { "Test_LoginToOrion_IsSuccess" })
	public void Test_IfEditTimeSheetPage_Isdisplayed() {
		log.info("Inside Test_IfEditTimeSheetPage_Isdisplayed method");
		log.debug("Verify draft timesheet exist or not");

		Select period = new Select(driver.findElement(By.id("reportperiod")));
		String strPeriod = CommonMethods.readTestData("TestData", "DraftTimeSheet");
		period.selectByVisibleText(strPeriod);
		log.info("Get report period details from the test data input file. " + strPeriod );

		rptPeriod = CommonMethods.readTestData("TestData", "DraftTimeSheetRptPeriod");
		log.info("Get report period link details from the test data input file. " + rptPeriod );
		
		clicklink(rptPeriod);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Exception in method Test_IfEditTimeSheetPage_Isdisplayed after clicklink: "+e.getMessage());
		}

		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet Edit Time Sheet");
		log.info("Edit Time Sheet page is displayed");
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public void Test_IfSaveMessage_IsDisplayed() {
		try {
			log.info("Inside Test_IfSaveMessage_IsDisplayed method");
			log.debug("Read existing data to the object");
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			log.info("Existing data stored " + objGridData.toString());
			log.debug("Inject test data to the fields");
			
			InjectTestData();
			log.info("Test data entered successfully" );
			log.debug("Initiate Save button click");

			TimeSheetEditPage.ScrollScreenToSaveButtonAndClick(driver, jse);
			Thread.sleep(2000);
			WebElement ElementMsg = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

			String strSaveMsg = ElementMsg.getText();

			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();
			assertEquals(strSaveMsg, "Time Sheet Saved Successfully.");
			log.info("Save button clicked");

		} catch (InterruptedException e) {
			log.error("Exception in method Test_IfSaveMessage_IsDisplayed : "+e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 4, dependsOnMethods = { "Test_IfSaveMessage_IsDisplayed" })
	public void Test_IfDataSavedCorrectly() {
		try {
			log.info("Inside Test_IfDataSavedCorrectly method");

			Thread.sleep(1000);
			log.info("Re-open the report period to verify data is saved correctly");
			
			clicklink(rptPeriod);
			objGridData.clear();
			log.info("Reading existing data to the object");
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			
			/* Note: Though download file functionality working fine locally in windows, unable to download file  
			 * in Linux/Jenkins Environment. Hence commenting download file comparison testing, need to revisit 
			 * later.  This may be due to environment setup or Selenium restrictions on Angular JS code.
			 * This may be most probably due to Angular JS code since we are able to download files in Linux/Jenkins 
			 * from other sites :-(
			`* DownloadfileAndComparewithTestFile();
			`* assertEquals(((CommonMethods.compareList(objTest, objGridData)) && isSameFiles), true);
			 */
			
			log.info("Test data entered " + objTest.toString());
			log.info("Current Screen data " + objGridData.toString());
			assertTrue(CommonMethods.compareList(objTest, objGridData));
			log.info("Data Saved correctly");
		} catch (Exception e) {
			log.error("Exception in method Test_IfDataSavedCorrectly : "+e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 5, dependsOnMethods = { "Test_IfDataSavedCorrectly" })
	public void Test_LogoutfromOrion_IsSuccess() {
		try {
			log.info("Inside Test_LogoutfromOrion_IsSuccess");
			log.debug("Identifying loginUserIcon for logout");

			act.moveToElement(CommonMethods.lbl_LoginUserIcon(driver)).click().perform();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(CommonMethods.btn_Logout(driver)));
			log.debug("Logout button click");

			CommonMethods.btn_Logout(driver).click();
			assertEquals(true, LoginPage.btnLogin(driver).isDisplayed());
			log.info("Logout successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method Test_LogoutfromOrion_IsSuccess " + e.getMessage());	
		}
	}

	public void clicklink(String period) {
		try {
			log.info("Inside clickLink, Report Period is : "+period);
			log.debug("Initiate Report Period click ");
			
			act.moveToElement(
					wait.until(ExpectedConditions.elementToBeClickable(TimeSheetMainPage.grd_clickReportPeriodLink(driver, period)))).click()
					.build().perform();
			log.info("Row clicked ");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method clicklink " + e.getMessage());		
		}
	}

	public void UploadAttachment() {
		try {
			log.info("Inside UploadAttachment");

			TimeSheetEditPage.wait_btn_AddAttachclickable(driver, wait).click();

			WebElement TableData = TimeSheetEditPage.grd_AttachmentData(driver);
			List<WebElement> Rows = TableData.findElements(By.tagName("tr"));
			int RowValue = 1;
			if (Rows.size() > 1) {
				RowValue = Rows.size();
			}
			wait.until(ExpectedConditions
					.elementToBeClickable(TimeSheetEditPage.wait_grd_AddAttachclickable(driver, RowValue)));
			// enter the file path onto the file-selection input field //
			TimeSheetEditPage.wait_grd_AddAttachclickable(driver, RowValue)
				.sendKeys(CommonMethods.Sample_FileNamewithPath);
			log.info("Upload success");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method UploadAttachment " + e.getMessage());		

		}
	}

	public String getLatestUploadFile() {
		try {
			log.info("Inside getLatestUploadFile" );
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			int RowValue = 1;
			if (Rows.size() > 1) {
				RowValue = Rows.size();
			}
			List<WebElement> Cols = Rows.get(RowValue - 1).findElements(By.tagName("td"));
			return Cols.get(0).getText();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method getLatestUploadFile " + e.getMessage());		
			return "Inside getLatestUploadFile Exception";
		}
	}

	public void DownloadfileAndComparewithTestFile() {
		try {
			log.info("Inside DownloadfileAndComparewithTestFile");
			String TempFileName = getLatestUploadFile();
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText(TempFileName)));
			driver.findElement(By.linkText(TempFileName)).click();
			TempFileName = TempFileName.replace("/", "_");
			Thread.sleep(9000);
			if (CommonMethods.CompareFilesbyByte(CommonMethods.Sample_FileNamewithPath,
					CommonMethods.Attachment_File_Download_Location + TempFileName) == true) {
				isSameFiles = true;
			} else {
				isSameFiles = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method DownloadfileAndComparewithTestFile " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void InjectTestData() {
		try {
			log.info("Inside InjectTestData");

			objTest = (ArrayList<String>) objGridData.clone();

			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");

			WebElement Element = TimeSheetEditPage.grd_ColSunday(driver);
			Element.clear();
			Element.sendKeys(CommonMethods.readTestData("TestData", "sun"));
			objTest.set(1, CommonMethods.readTestData("TestData", "sun"));

			WebElement Element1 = TimeSheetEditPage.grd_ColMonday(driver);
			Element1.clear();
			Element1.sendKeys(CommonMethods.readTestData("TestData", "mon"));
			objTest.set(2, CommonMethods.readTestData("TestData", "mon"));

			WebElement Element2 = TimeSheetEditPage.grd_txtComment(driver);
			Element2.clear();
			Element2.sendKeys(CommonMethods.readTestData("TestData", "comment"));
			objTest.set(8, CommonMethods.readTestData("TestData", "comment"));

			UploadAttachment();
			log.info("Test data added to the screen : " + objTest.toString());

		} catch (Exception e) {
			log.error("Exception in InjectTestData "+ e.getMessage());

			e.printStackTrace();
		}
	}
}
