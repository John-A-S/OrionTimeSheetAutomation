package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.NoSuchElementException;
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

public class Test_SubmittedTimeSheet_SaveFunctionality extends OrionBase{

	int RowNumb;

	public Test_SubmittedTimeSheet_SaveFunctionality() {
		super();
	}

	
	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {

		try {
			
			System.out.println("********** Test_SubmittedTimeSheet_SaveFunctionality START ************* ");
			
			init(Browser, false);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_SubmittedTimeSheet_SaveFunctionality END ************* ");
	}

	@Test(dataProvider = "credentials", dataProviderClass = CommonMethods.class, priority = 1)
	public void Test_LoginToOrion_IsSuccess(String UserID, String Password) {
		try {
			LoginPage.txtbx_UserName(driver).sendKeys(UserID);
			LoginPage.txtbx_Password(driver).sendKeys(Password);
			LoginPage.btnLogin(driver).click();
			try {
				assertEquals(true, CommonMethods.lbl_LoginUserIcon(driver).isDisplayed());
			} catch (NoSuchElementException e) {
				assertEquals(false, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 2, dependsOnMethods = { "Test_LoginToOrion_IsSuccess" })
	public void Test_IfEditTimeSheetPage_Isdisplayed() {
		// RowNumb will have the row number of Submitted timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'S');
		if (RowNumb <= 0)  {
			assertTrue(false, "No record to process");
		} 

		clicklink(RowNumb);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet Edit Time Sheet");
	}
	
	
	@Test(priority=3, dependsOnMethods = {"Test_IfEditTimeSheetPage_Isdisplayed"}) 
	public void Test_VerifyUserCanEnterTime() {
		assertFalse(TimeSheetEditPage.grd_ColMonday(driver).isEnabled());
	}
	
	
	@Test(priority=4, dependsOnMethods = {"Test_VerifyUserCanEnterTime"}) 
	public void Test_VerifyUserAddAttachment() {
		assertFalse(TimeSheetEditPage.AddAttachclickable(driver).isEnabled());
	}
	

	@Test(priority=5, dependsOnMethods = {"Test_VerifyUserAddAttachment"}) 
	public void Test_VerifyUserAddcomment() {
		assertFalse(TimeSheetEditPage.grd_txtComment(driver).isEnabled());
	}
	
	@Test(priority = 6, dependsOnMethods = { "Test_VerifyUserAddcomment" })
	public  void Test_SaveButton_IsDisplayed() {
		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		assertEquals(TimeSheetEditPage.verifySaveButtonExists(driver), false);
	}

	@Test(priority = 7, dependsOnMethods = { "Test_SaveButton_IsDisplayed" })
	public void Test_LogoutfromOrion_IsSuccess() {
		try {
			act.moveToElement(CommonMethods.lbl_LoginUserIcon(driver)).click().perform();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(CommonMethods.btn_Logout(driver)));
			CommonMethods.btn_Logout(driver).click();
			assertEquals(true, LoginPage.btnLogin(driver).isDisplayed());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clicklink(int RowNo) {
		try {
			act.moveToElement(
					wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.getGrdElement(driver, RowNo)))).click()
					.build().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
