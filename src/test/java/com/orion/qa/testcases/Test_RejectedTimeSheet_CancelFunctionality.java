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

public class Test_RejectedTimeSheet_CancelFunctionality extends OrionBase {
	ArrayList<String> objTest;
	ArrayList<String> objGridData;

	int RowNumb;
	int AttachmentRowId;
	
	public Test_RejectedTimeSheet_CancelFunctionality() {
		super();
		log.info("After calling Base class");

	}

	@Parameters({"Browser", "ClassName"})
	@BeforeClass
	public void InitObjects(String Browser, String ClassName) {
		System.out.println("********** Test_RejectedTimeSheet_CancelFunctionality START ************* ");

		try {
			
			log.info("********** Test_RejectedTimeSheet_CancelFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			
			init(Browser, ClassName, true);
			objTest = new ArrayList<String>();
			objGridData = new ArrayList<String>();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InitObjects "+ e.getMessage());

		}
	}

	@AfterClass
	public void CloseObjects() {
		System.out.println("********** Test_RejectedTimeSheet_CancelFunctionality END ************* ");
		CloseBrowser();
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
		// RowNumb will have the row number of draft timesheet //
		log.info("Inside Test_IfEditTimeSheetPage_Isdisplayed");
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'R');
		if (RowNumb <= 0) {
			log.info("No Rejected timesheet to process");
			assertTrue(false, "No record to process");
		}
		log.info("Rejected timesheet exist in Row "+ RowNumb);

		clicklink(RowNumb);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Exception in method Test_IfEditTimeSheetPage_Isdisplayed : "+ e.getMessage());

		}

		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet Edit Time Sheet");
		log.info("Edit TimeSheet screen displayed successfully");
	}

	public void Test_InjectTestDataandCancel() throws InterruptedException {
		log.info("Read existing data from the screen to the object");
		objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
		log.info("Input test data");
		InjectTestData();
		log.info("Initiate Cancel button click");
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public void Test_IfDatanotSaved() {
		try {
			log.info("Inside Test_IfDatanotSaved");
			Test_InjectTestDataandCancel();
			clicklink(RowNumb);
			objGridData.clear();
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			assertEquals((!(CommonMethods.compareList(objTest, objGridData)) && chkUploadFileisCancelled()), true);
			log.info("Data compared successfully!");
		} catch (Exception e) {
			log.error("Exception in Test_IfDatanotSaved method : " + e.getMessage());
			e.printStackTrace();
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
			log.info("Logout successfully");
			assertEquals(true, LoginPage.btnLogin(driver).isDisplayed());
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
			log.info("Test file attached");
		} catch (Exception e) {
			log.error("Exception in method UploadAttachment " + e.getMessage());
			e.printStackTrace();
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
			log.error("Exception in chkUploadFileisCancelled method " + e.getMessage());
			e.printStackTrace();
			return true;
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
			log.info("Injecting test data to the screen completed.");

		} catch (Exception e) {
			log.error("Exception in InjectTestData method :" + e.getMessage());
			e.printStackTrace();
		}
	}
}
