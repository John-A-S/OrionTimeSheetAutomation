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

public class Test_ApprovedTimeSheet_SaveFunctionality extends OrionBase{
	
	int RowNumb;
	
	public Test_ApprovedTimeSheet_SaveFunctionality() {
		super();
		log.info("After calling Base class");
	}

	@Parameters({"Browser", "ClassName"})
	@BeforeClass
	public void InitObjects(String Browser, String ClassName) {
		System.out.println("********** Test_ApprovedTimeSheet_SaveFunctionality START ************* ");

		try {
			log.info("********** Test_ApprovedTimeSheet_SaveFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			init(Browser, ClassName, false);	
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InitObjects "+ e.getMessage());
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_ApprovedTimeSheet_SaveFunctionality END ************* ");
		log.info("********** Test_ApprovedTimeSheet_SaveFunctionality END *************");

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
			log.error("Exception in method Test_LoginToOrion_IsSuccess : "+ e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(priority = 2, dependsOnMethods = { "Test_LoginToOrion_IsSuccess" })
	public void Test_IfEditTimeSheetPage_Isdisplayed() {
		log.info("Inside Test_IfEditTimeSheetPage_Isdisplayed method");

		log.info("Get report period details from the test data input file." );
		Select period = new Select(driver.findElement(By.id("reportperiod")));
		period.selectByVisibleText(CommonMethods.readTestData("TestData", "ApprovedTimeSheet"));


		// RowNumb will have the row number of Approved timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'A');
		if (RowNumb <= 0)  {
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
	public  void Test_SaveButton_IsDisplayed() {
		log.info("Inside Test_SaveButton_IsDisplayed");
		log.debug("Move cursor to Save button");

		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		log.info("Save button exists ");
		log.debug("Initiate Save button click");
		assertEquals(TimeSheetEditPage.verifySaveButtonExists(driver), false);
		log.info("Save button clicked successfully");

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
}
