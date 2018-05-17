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

public class Test_NewTimeSheet_SubmitFunctionality extends OrionBase {
	ArrayList<String> objTest;
	ArrayList<String> objGridData;

	String NewReportPeriod;
	int RowNumb;
	boolean isAttachmntExist, isSameFiles, isAttachmentdisabled;

	public Test_NewTimeSheet_SubmitFunctionality() {
		super();
	}
	
	@Parameters({"Browser", "ClassName"})
	@BeforeClass
	public void InitObjects(String Browser, String ClassName) {
		try {
			System.out.println("********** Test_NewTimeSheet_SubmitFunctionality START ************* ");
			init(Browser, ClassName, true);
			log.info("********** Test_NewTimeSheet_SubmitFunctionality START ************* ");
			log.info("Inside InitObjects");	

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
		System.out.println("********** Test_NewTimeSheet_SubmitFunctionality END *************");
		log.info("********** Test_NewTimeSheet_SubmitFunctionality END *************");
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
			log.info("Login button clicked");
			try {
				assertEquals(true, CommonMethods.lbl_LoginUserIcon(driver).isDisplayed());
				log.info("Login success");
			} catch (NoSuchElementException e) {
				log.error("Exception : Login button not found; Error occured: "+ e.getMessage());
				assertEquals(false, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method Test_LoginToOrion_IsSuccess : "+ e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "Test_LoginToOrion_IsSuccess" })
	public void Test_IfNewTimeSheetPage_Isdisplayed() {
		log.info("Inside Test_IfNewTimeSheetPage_Isdisplayed method");
		clickNewTimeSheetlink();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Exception in method Test_IfNewTimeSheetPage_Isdisplayed : "+e.getMessage());
		}

		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet New Time Sheet");
		log.info("New Time Sheet page is displayed");
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfNewTimeSheetPage_Isdisplayed" })
	public void Test_IfSubmitMessage_IsDisplayed() {
		try {
			log.info("Inside  Test_IfSubmitMessage_IsDisplayed" );
				
			Select rptPeriod = new Select(TimeSheetEditPage.lbl_ReportDate(driver));
			WebElement ele = rptPeriod.getFirstSelectedOption();
			NewReportPeriod = ele.getText();
			log.info("New Report Period " + NewReportPeriod);
			InjectTestData();
			log.debug("Submit button click");
			TimeSheetEditPage.ScrollScreenToSubmitButtonAndClick(driver, jse);
			Thread.sleep(1000);

			log.debug("Warning message: OK click");	
			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSubmit_OK(driver, wait)).click().build().perform();
			
			Thread.sleep(1000);
			WebElement ElementMsg = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

			String strSaveMsg = ElementMsg.getText();

			log.debug("Timesheet submitted successfully: OK click");	
			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();
			
			assertEquals(strSaveMsg, "Time Sheet Submitted Successfully.");
			log.info("TimeSheet submitted successfully");
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Exception in method Test_IfSubmitMessage_IsDisplayed " + e.getMessage());
		}
	}

	@Test(priority = 4, dependsOnMethods = { "Test_IfSubmitMessage_IsDisplayed" })
	public void Test_IfDataSubmittedCorrectly() {
		try {
			log.info("Inside Test_IfDataSubmittedCorrectly to verify data is submitted correctly");

			Thread.sleep(3000);

			try {
				log.debug("Verfiy ReportPeriod "+ NewReportPeriod + ". Click link");
				TimeSheetMainPage.grd_clickReportPeriodLink(driver, NewReportPeriod).click();
			} catch (NoSuchElementException e) {
				log.error("Link "+NewReportPeriod+" not found.  Error :"+e.getMessage());
				assertTrue(false);
				
			}


			objGridData.clear();
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			log.debug("Comparing submitted & test data are equal!");

			/* Note: Though download file functionality working fine locally in windows, unable to download file  
			 * in Linux/Jenkins Environment. Hence commenting download file comparison testing, need to revisit 
			 * later.  This may be due to environment setup or Selenium restrictions on Angular JS code.
			 * This may be most probably due to Angular JS code since we are able to download files in Linux/Jenkins 
			 * from other sites :-(

			 * DownloadfileAndComparewithTestFile();
			 * assertEquals(((CommonMethods.compareList(objTest, objGridData)) && isSameFiles), true);
			*/
			
			log.info("Test Data " + objTest.toString());
			log.info("Grid data " + objGridData.toString());
			assertEquals((CommonMethods.compareList(objTest, objGridData)), true);
			log.info("Data compared successfully");
			log.info("Timesheet data submitted correctly");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method "+ e.getMessage());
		}
	}

	@Test(priority = 5, dependsOnMethods = { "Test_IfDataSubmittedCorrectly" })
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

	public void clickNewTimeSheetlink() {
		try {
			log.debug("New Timesheet button click");
			act.moveToElement(wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.btn_NewTimeSheet(driver))))
					.click().build().perform();
			log.info("New timesheet button successfully clicked");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occured during new timesheet button click "+ e.getMessage());
		}
	}

	public void UploadAttachment() {
		try {
			log.info("Inside Upload Attachment");
			log.debug("Add Attach button click");
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
			log.debug("Add Attachment");
			TimeSheetEditPage.wait_grd_AddAttachclickable(driver, RowValue)
				.sendKeys(CommonMethods.Sample_FileNamewithPath);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method UploadAttachment "+ e.getMessage());
		}
	}

	public String getLatestUploadFile() {
		log.info("Inside getLatestUploadFile");
		try {
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			int RowValue = 1;
			if (Rows.size() > 1) {
				RowValue = Rows.size();
			}
			List<WebElement> Cols = Rows.get(RowValue - 1).findElements(By.tagName("td"));
			return Cols.get(0).getText();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method getLatestUploadFile "+ e.getMessage());
			return "Inside getLatestUploadFile Exception";
		}
	}

	public void DownloadfileAndComparewithTestFile() {
		log.info("Inside DownloadfileAndComparewithTestFile");
		try {
			String TempFileName = getLatestUploadFile();
			log.info("Temp File Name : "+ TempFileName);

			log.debug("setting Downloadproperties..");
			
		 //   setDownloadProperties(TempFileName);
			
			System.out.println(driver.toString());
			
			
			Thread.sleep(5000);
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText(TempFileName)));
			System.out.println("Temp file name exists :" + TempFileName);
			
			driver.findElement(By.linkText(TempFileName)).click();
			System.out.println("After downloadfile click ");
			Thread.sleep(20000);
			TempFileName = TempFileName.replace("/", "_");
			System.out.println("File comparison :"+ CommonMethods.Sample_FileNamewithPath + "\n  Downloaded file : " + CommonMethods.Attachment_File_Download_Location + TempFileName );

			log.debug("File comparison :"+ CommonMethods.Sample_FileNamewithPath + " /n Downloaded file : " + CommonMethods.Attachment_File_Download_Location + TempFileName );
			
			if (CommonMethods.CompareFilesbyByte(CommonMethods.Sample_FileNamewithPath,
					CommonMethods.Attachment_File_Download_Location + TempFileName) == true) {
				isSameFiles = true;
				log.info("File comparison completed successfully.  Both files matches");
			} else {
				isSameFiles = false;
				log.info("File comparison completed successfully.  Both files mis-matches");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method DownloadfileAndComparewithTestFile "+e.getMessage());
		}
	}

	public void InjectTestData() {
		try {
			log.debug("Sending test data to the fields");
			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");

			// populate default value to the test data
			objTest.add(0, CommonMethods.readTestData("TestData", "company"));
				
			WebElement Element = TimeSheetEditPage.grd_ColSunday(driver);
			Element.clear();
			Element.sendKeys(CommonMethods.readTestData("TestData", "sun"));
			objTest.add(1, CommonMethods.readTestData("TestData", "sun"));

			WebElement Element1 = TimeSheetEditPage.grd_ColMonday(driver);
			Element1.clear();
			Element1.sendKeys(CommonMethods.readTestData("TestData", "mon"));
			objTest.add(2, CommonMethods.readTestData("TestData", "mon"));

			// populate default value to the test data
			objTest.add(3, CommonMethods.readTestData("TestData", "tue"));

			// populate default value to the test data
			objTest.add(4, CommonMethods.readTestData("TestData", "wed"));

			// populate default value to the test data
			objTest.add(5, CommonMethods.readTestData("TestData", "thu"));

			// populate default value to the test data
			objTest.add(6, CommonMethods.readTestData("TestData", "fri"));

			// populate default value to the test data
			objTest.add(7, CommonMethods.readTestData("TestData", "sat"));

			WebElement Element2 = TimeSheetEditPage.grd_txtComment(driver);
			Element2.clear();
			Element2.sendKeys(CommonMethods.readTestData("TestData", "comment"));
			objTest.add(8, CommonMethods.readTestData("TestData", "comment"));

			UploadAttachment();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InjectTestData " + e.getMessage());
		}
	}

	public void clicklink(int RowNo) {
		log.debug("Inside clicklink");
		try {
			act.moveToElement(
					wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.getGrdElement(driver, RowNo)))).click()
					.build().perform();
		} catch (Exception e) {
			log.error("Exception in clicklink method "+ e.getMessage());
			e.printStackTrace();
		}
	}

}
