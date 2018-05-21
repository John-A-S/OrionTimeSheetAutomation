package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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

public class Test_PreApprovedTimeSheet_SubmitFunctionality extends OrionBase{
	String strExistingComment;

	int RowNumb;
	int AttachmentRowNo;
	String rptPeriod;
	String strMonth;


	boolean isSameFiles;
	
	public Test_PreApprovedTimeSheet_SubmitFunctionality() {
		super();
	}

	@Parameters({"Browser", "ClassName"})
	@BeforeClass
	public void InitObjects(String Browser, String ClassName) {
		try {

			System.out.println("********** Test_PreApprovedTimeSheet_SubmitFunctionality START ************* ");
			
			init(Browser, ClassName, true);

			log.info("********** Test_PreApprovedTimeSheet_SubmitFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

		} catch (Exception e) {
			log.error("Exception in method InitObjects "+ e.getMessage());
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_PreApprovedTimeSheet_SubmitFunctionality END ************* ");
		log.info("********** Test_PreApprovedTimeSheet_SubmitFunctionality END *************");
	}

	@Test(dataProvider = "credentials", dataProviderClass = CommonMethods.class, priority = 1)
	public void Test_LoginToOrion_IsSuccess(String UserID, String Password) {
		try {
			
			log.info("Inside Test_LoginToOrion_IsSuccess method");
			log.debug("Setting User Credentials");
		
			LoginPage.txtbx_UserName(driver).sendKeys(UserID);
			LoginPage.txtbx_Password(driver).sendKeys(Password);
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
			log.error("Exception in method Test_LoginToOrion_IsSuccess : "+ e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 2, dependsOnMethods = { "Test_LoginToOrion_IsSuccess" })
	public void Test_IfEditTimeSheetPage_Isdisplayed() {
		log.info("Inside Test_IfEditTimeSheetPage_Isdisplayed");
		
		strMonth = CommonMethods.readTestData("TestData", "PreApprovedTimeSheet");
		log.info("Get report period details from the test data input file. " + strMonth);

		rptPeriod = CommonMethods.readTestData("TestData", "PreApprovedTimeSheetRptPeriod");
		log.info("Get report period link details from the test data input file. " + rptPeriod );

		SetTimePeriod();
		
		clicklink(rptPeriod);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("Exception in method Test_IfEditTimeSheetPage_Isdisplayed : "+ e.getMessage());
			e.printStackTrace();
		}
		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet Edit Time Sheet");
		log.info("Edit TimeSheet screen displayed successfully");
		
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public  void Test_SubmitButton_IsDisplayed() {
		log.debug("Verify Submit button exists");

		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		assertEquals(TimeSheetEditPage.verifySubmitButtonExists(driver), true);
		log.info("Submit button exists");

	}
	
	@Test(priority = 4, dependsOnMethods = {"Test_SubmitButton_IsDisplayed"} )
	public void Test_VerifyGridisDisabled() {
		log.debug("Verify grid is enabled?");
		// to confirm the grid is disabled as of now we just check one column
		assertFalse(TimeSheetEditPage.grd_ColMonday(driver).isEnabled());
		log.info("Timesheet grid is disabled!");
	}
	
	@Test(priority = 5, dependsOnMethods = { "Test_VerifyGridisDisabled" })
	public  void Test_SubmitButton_InjectTestDataandVerify() throws InterruptedException {
		//strExistingComment = ReadCurrentData();
		log.info("Inject Test Data");
		InjectTestData();
		log.debug("Initiate scroll to Submit button");
		TimeSheetEditPage.ScrollScreenToSubmitButtonAndClick(driver, jse);
		
		Thread.sleep(1000);
		log.debug("Initiate Submit button click");
		act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSubmit_OK(driver, wait)).click().build().perform();
		log.info("Submit button clicked");
		Thread.sleep(1000);
		WebElement ElementMsg = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

		String strSaveMsg = ElementMsg.getText();

		act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();
		
		assertEquals(strSaveMsg, "Time Sheet Submitted Successfully.");
	}
	
	@Test(priority = 5, dependsOnMethods = { "Test_SubmitButton_InjectTestDataandVerify" })
	public void Test_IfUpdatedDataisSubmitted() {
		try {
			log.info("Inside Test_IfUpdatedDataisSubmitted");
			Thread.sleep(3000);
			SetTimePeriod();

			clicklink(rptPeriod);

			TimeSheetEditPage.ScrollScreenToElement(driver, jse, TimeSheetEditPage.grd_txtComment(driver));
			
			String comment = TimeSheetEditPage.grd_txtComment(driver).getAttribute("value");
			log.info("Current data :  "+ comment);	
			/* Note: Though download file functionality working fine locally in windows, unable to download file  
			 * in Linux/Jenkins Environment. Hence commenting download file comparison testing, need to revisit 
			 * later.  This may be due to environment setup or Selenium restrictions on Angular JS code.
			 * This may be most probably due to Angular JS code since we are able to download files in Linux/Jenkins 
			 * from other sites :-(
			 * DownloadfileAndComparewithTestFile();
			 *  assertEquals( (isCommentTextSame && isSameFiles), true);
			*/
			log.debug("Comparing test data and current data in the screen");
			assertTrue(comment.equals(CommonMethods.readTestData("TestData", "comment")));
			log.info("Both test data and current data are equal");
		} catch (Exception e) {
			log.error("Exception in Test_IfUpdatedDataisSubmitted method : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void DownloadfileAndComparewithTestFile() {
		try {
			log.info("Inside DownloadfileAndComparewithTestFile");
			String TempFileName = getLatestUploadFile();
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText(TempFileName)));
			driver.findElement(By.linkText(TempFileName)).click();
			TempFileName = TempFileName.replace("/", "_");
			Thread.sleep(5000);
			if (CommonMethods.CompareFilesbyByte(CommonMethods.Sample_FileNamewithPath,
					CommonMethods.Attachment_File_Download_Location + TempFileName) == true) {
				isSameFiles = true;
			} else {
				isSameFiles = false;
			}
		} catch (Exception e) {
			log.error("Exception in DownloadfileAndComparewithTestFile method " + e.getMessage()); 
			e.printStackTrace();
		}
	}

	@Test(priority = 6, dependsOnMethods = { "Test_IfUpdatedDataisSubmitted" })
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
	
	public void SetTimePeriod() {
		Select period = new Select(driver.findElement(By.id("reportperiod")));
		period.selectByVisibleText(strMonth);
	}
	
	
	public String getLatestUploadFile() {
		try {
			log.info("Inside getLatestUploadFile");
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			int RowValue = 1;
			if (Rows.size() >= 1) {
				RowValue = Rows.size();
				/* AttachmentRowNo is the row id where test file is uploaded */	
				if (RowValue == AttachmentRowNo)
				{
					List<WebElement> Cols = Rows.get(RowValue - 1).findElements(By.tagName("td"));
					return Cols.get(0).getText();
				}
			}
			return "";
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in getLatestUploadFile method : " + e.getMessage());
			return "Inside getLatestUploadFile Exception";
		}
	}

	public boolean ChkTestFileisCancelled() {
		try {
			log.info("Inside ChkTestFileisCancelled");
			if (getLatestUploadFile() == "")
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (Exception e) {
			log.error("Exception in ChkTestFileisCancelled method : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}


	public void InjectTestData() {
		try {
			log.info("Inside InjectTestData");
			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");
			
			WebElement Element1 = TimeSheetEditPage.grd_txtComment(driver);
			Element1.clear();
			Element1.sendKeys(CommonMethods.readTestData("TestData", "comment"));

			UploadAttachment();

		} catch (Exception e) {
			log.error("Exception in InjectTestData method : " + e.getMessage() );
			e.printStackTrace();
		}
	}

	public String ReadCurrentData() {
		try {
			log.info("Inside ReadCurrentData");
			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");
			
			WebElement Element1 = TimeSheetEditPage.grd_txtComment(driver);
			return Element1.getText();

		} catch (Exception e) {
			log.error("Exception in ReadCurrentData method : " + e.getMessage() );
			e.printStackTrace();
			return "";
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
			AttachmentRowNo = RowValue;
		} catch (Exception e) {
			log.error("Exception in UploadAttachment method : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
