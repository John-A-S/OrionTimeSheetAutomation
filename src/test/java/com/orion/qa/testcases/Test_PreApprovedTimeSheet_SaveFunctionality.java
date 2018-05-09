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

public class Test_PreApprovedTimeSheet_SaveFunctionality extends OrionBase{

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";

	int RowNumb;
	
	public Test_PreApprovedTimeSheet_SaveFunctionality() {
		super();

	}

	@Parameters({"Browser", "ClassName"})
	@BeforeClass
	public void InitObjects(String Browser, String ClassName) {
		try {

			System.out.println("********** Test_PreApprovedTimeSheet_SaveFunctionality START ************* ");
			
			init(Browser, ClassName, false);
			log.info("********** Test_PreApprovedTimeSheet_SaveFunctionality START ************* ");
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
		System.out.println("********** Test_PreApprovedTimeSheet_SaveFunctionality END ************* ");
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

		Select period = new Select(driver.findElement(By.id("reportperiod")));
		String strPeriod = CommonMethods.readTestData("TestData", "PreApprovedTimeSheet");
		period.selectByVisibleText(strPeriod);
		log.info("Get report period details from the test data input file. " + strPeriod );

		
		// RowNumb will have the row number of pre-approved timesheet //
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
	public void Test_SaveButton_IsDisplayed() {
		log.debug("Verify Save button exists");

		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		assertEquals(TimeSheetEditPage.verifySaveButtonExists(driver), false);
		log.info("Save button exists");
	}

	@Test(priority = 4, dependsOnMethods = { "Test_SaveButton_IsDisplayed" })
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
}
