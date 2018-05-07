package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

public class Test_ApprovedTimeSheet_CancelFunctionality extends OrionBase {
	int RowNumb;

	public Test_ApprovedTimeSheet_CancelFunctionality() {
		super();
		log.info("After calling Base class");
	}

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		System.out.println("********** Test_ApprovedTimeSheet_CancelFunctionality START ************* ");
		try {
			log.info("********** Test_ApprovedTimeSheet_CancelFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			init(Browser, false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InitObjects "+ e.getMessage());
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_ApprovedTimeSheet_CancelFunctionality END ************* ");
		log.info("********** Test_ApprovedTimeSheet_CancelFunctionality END *************");
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
			e.printStackTrace();
			log.error("Exception in method Test_LoginToOrion_IsSuccess : "+ e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "Test_LoginToOrion_IsSuccess" })
	public void Test_IfEditTimeSheetPage_Isdisplayed() {
		try {
			log.info("Inside Test_IfEditTimeSheetPage_Isdisplayed method");
			Thread.sleep(500);
		} catch (InterruptedException e) {
			log.error("Exception in method Test_IfEditTimeSheetPage_Isdisplayed : "+e.getMessage());
			e.printStackTrace();
		}

		log.info("Get report period details from the test data input file." );
		Select period = new Select(driver.findElement(By.id("reportperiod")));
		period.selectByVisibleText(CommonMethods.readTestData("TestData", "ApprovedTimeSheet"));

		// RowNumb will have the row number of approved timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'A');
		if (RowNumb <= 0) {
			assertTrue(false, "No record to process");
		}
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

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public void Test_EditTimesheetScreenComponents_AreDisabled() {
		log.info("Inside Test_EditTimesheetScreenComponents_AreDisabled");
		log.debug("Check if Add Attachment is disabled");
		assertEquals(TimeSheetEditPage.wait_btn_AddAttachclickable(driver, wait).getAttribute("disabled"), "true",
				"Attach component disabled");
		log.info("Add Attachment is disabled");
		
		log.debug("Check if weekly columns are disabled");
		assertEquals(TimeSheetEditPage.grd_ColSunday(driver).getAttribute("disabled"), "true", "Col1 disabled");
		log.info("Weekly columns are disabled");
		
		log.debug("Check if Comment field is disabled");
		assertEquals(TimeSheetEditPage.grd_txtComment(driver).getAttribute("disabled"), "true",
				"Comment component disabled");
		log.info("Common field is disabled");
	}

	@Test(priority = 4, dependsOnMethods = { "Test_EditTimesheetScreenComponents_AreDisabled" })
	public void Test_ClickCancelExists() {
		log.info("Inside Test_ClickCancelExists");
		log.debug("Move cursor to Cancel button");
		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		log.debug("Verfiy cancel button exists ");
		assertEquals(TimeSheetEditPage.verifyCancelButtonExists(driver), true);
		log.info("Cancel button exists ");
		log.debug("Initiate cancel button click");
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
		log.info("Cancel button clicked successfully");
	}

	@Test(priority = 6, dependsOnMethods = { "Test_ClickCancelExists" })
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
			log.error("Exception in method Test_LogoutfromOrion_IsSuccess " + e.getMessage());		}
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
}
