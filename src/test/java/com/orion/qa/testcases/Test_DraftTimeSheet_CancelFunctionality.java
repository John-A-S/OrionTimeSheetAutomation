package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
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

public class Test_DraftTimeSheet_CancelFunctionality extends OrionBase {
	ArrayList<String> objTest;
	ArrayList<String> objGridDataB4Changes;
	ArrayList<String> objGridDataAftrChanges;

	int RowNumb;
	int AttachmentRowId;

	public Test_DraftTimeSheet_CancelFunctionality() {
		super();
		log.info("After calling Base class");

	}

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		System.out.println("********** Test_DraftTimeSheet_CancelFunctionality START ************* ");

		try {
			
			log.info("********** Test_DraftTimeSheet_CancelFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			init(Browser, true);	
			objTest = new ArrayList<String>();
			objGridDataB4Changes = new ArrayList<String>();
			objGridDataAftrChanges = new ArrayList<String>();
			

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InitObjects "+ e.getMessage());

		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_DraftTimeSheet_CancelFunctionality END ************* ");
		log.info("********** Test_DraftTimeSheet_CancelFunctionality END *************");

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
		// RowNumb will have the row number of draft timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'D');
		if (RowNumb <= 0) {
			assertTrue(false, "No record to process");
			log.info("Draft timesheet does not exist ");

		}
		log.info("Draft timesheet exists in Row : " + RowNumb);
		clicklink(RowNumb);
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

	public void Test_InjectTestDataandCancel() {
		log.info("Inside Test_InjectTestDataandCancel method");
		log.debug("Read existing data to the object");
		objGridDataB4Changes= TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
		log.info("Existing data stored successfully");
		log.debug("Inject test data to the fields");
		
		InjectTestData();
		log.info("Test data entered successfully" );
		log.debug("Initiate Cancel button click");
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
		log.info("Cancel button clicked");
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public void Test_IfDatanotSaved() {
		try {
			log.info("Inside Test_IfDatanotSaved method");
			log.info("Calling , message,Test_InjectTestDataandCancel");
			Test_InjectTestDataandCancel();
			clicklink(RowNumb);
			log.debug("Read existing data in the screen to the object to validate");
			objGridDataAftrChanges = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			assertEquals((CommonMethods.compareList(objGridDataB4Changes, objGridDataAftrChanges) && chkUploadFileisCancelled()), true);
			log.info("Data comparison done successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method Test_IfDatanotSaved : "+e.getMessage());
		}
	}

	@Test(priority = 4, dependsOnMethods = { "Test_IfDatanotSaved" })
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
			log.info("Inside clickLink, RowNo value is : "+RowNo);
			log.debug("Initiate Row click ");

			act.moveToElement(
					wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.getGrdElement(driver, RowNo)))).click()
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
			AttachmentRowId = 1;
			if (Rows.size() > 1) {
				AttachmentRowId = Rows.size();
			}
			wait.until(ExpectedConditions
					.elementToBeClickable(TimeSheetEditPage.wait_grd_AddAttachclickable(driver, AttachmentRowId)));
			// enter the file path onto the file-selection input field //
			TimeSheetEditPage.wait_grd_AddAttachclickable(driver, AttachmentRowId)
					.sendKeys(CommonMethods.Sample_FileNamewithPath);
			log.info("Upload success");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method UploadAttachment " + e.getMessage());		
		}
	}

	public boolean chkUploadFileisCancelled() {
		try {
			log.info("Inside chkUploadFileisCancelled");
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			if (Rows.size() == AttachmentRowId) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in chkUploadFileisCancelled "+ e.getMessage());
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public void InjectTestData() {
		try {
			log.info("Inside InjectTestData");
			objTest = (ArrayList<String>) objGridDataB4Changes.clone();

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
			log.info("Test data added to the screen");
		} catch (Exception e) {
			log.error("Exception in InjectTestData "+ e.getMessage());
			e.printStackTrace();
		}
	}
}
