package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

	boolean isSameFiles;
	
	public Test_PreApprovedTimeSheet_SubmitFunctionality() {
		super();
		log.info("After calling Base class");

	}

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		try {

			System.out.println("********** Test_PreApprovedTimeSheet_SubmitFunctionality START ************* ");
			
			log.info("********** Test_PreApprovedTimeSheet_SubmitFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			init(Browser, true);


		} catch (Exception e) {
			log.error("Exception in method InitObjects "+ e.getMessage());

			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_PreApprovedTimeSheet_SubmitFunctionality END ************* ");
		log.info("********** Test_PreApprovedTimeSheet_CancelFunctionality END *************");
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

		// RowNumb will have the row number of Pre-Approved timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'P');
		if (RowNumb <= 0)  {
			log.info("No Pre-Approved timesheet to process");

			assertTrue(false, "No record to process");
		} 

		log.info("PreApproved timesheet exist in Row "+ RowNumb);
		clicklink(RowNumb);
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
		WebElement ElementMsg1 = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

		String strSaveMsg1 = ElementMsg1.getText();

		act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();
		
		assertEquals(strSaveMsg1, "Time Sheet Submitted Successfully.");
	}
	
	@Test(priority = 5, dependsOnMethods = { "Test_SubmitButton_InjectTestDataandVerify" })
	public void Test_IfUpdatedDataisSubmitted() {
		try {
			log.info("Inside Test_IfUpdatedDataisSubmitted");
			Thread.sleep(3000);
			clicklink(RowNumb);

			TimeSheetEditPage.ScrollScreenToElement(driver, jse, TimeSheetEditPage.grd_txtComment(driver));
			
			String comment = TimeSheetEditPage.grd_txtComment(driver).getAttribute("value");
			
			boolean isCommentTextSame = comment.equals(CommonMethods.readTestData("TestData", "comment"));
			
			DownloadfileAndComparewithTestFile();
			
			assertEquals( (isCommentTextSame && isSameFiles), true);

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

	public void clicklink(int RowNo) {
		try {
			log.info("Inside clicklink");

			act.moveToElement(
					wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.getGrdElement(driver, RowNo)))).click()
					.build().perform();
		} catch (Exception e) {
			log.error("Exception in method clicklink " + e.getMessage());

			e.printStackTrace();
		}
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
