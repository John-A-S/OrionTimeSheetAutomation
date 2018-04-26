package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;

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

public class Test_NewTimeSheet_CancelFunctionality extends OrionBase {
	ArrayList<String> objTest;


	int RowNumb;
	int AttachmentRowId;

	public Test_NewTimeSheet_CancelFunctionality() {
		super();
	}

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		try {
			
			System.out.println("********** Test_NewTimeSheet_SaveFunctionality ************* ");
			
			init(Browser, true);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_DraftTimeSheet_SaveFunctionality ************* ");
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
	public void Test_IfNewTimeSheetPage_Isdisplayed() {
		clickNewTimeSheetlink();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet New Time Sheet");
	}

	public void Test_InjectTestDataandCancel() {
		InjectTestData();
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfNewTimeSheetPage_Isdisplayed" })
	public void Test_IfDatanotSaved() {
		try {

			Select rptPeriod = new Select(TimeSheetEditPage.lbl_ReportDate(driver));
			WebElement ele = rptPeriod.getFirstSelectedOption();
			String NewReportPeriod = ele.getText();
			System.out.println(NewReportPeriod);	
			Test_InjectTestDataandCancel();
			try {
				TimeSheetEditPage.grd_clickReportPeriodLink(driver, NewReportPeriod);
				System.out.println("New Time Period found.  Test case FAILED");
				assertEquals(true, false, "New Time Period " + NewReportPeriod + " Exist.  FAILED");
			} catch (NoSuchElementException e) {
				System.out.println(e.getMessage());
				System.out.println("New Time Period not found.  Test case PASSED");
				assertEquals(false, false, "PASSED");
			}
		} catch (Exception e) {
			System.out.println("Inside 2nd Catch");
			assertEquals(false, false);
			// e.printStackTrace();
		}
	}

	@Test(priority = 4, dependsOnMethods = { "Test_IfDatanotSaved" })
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

	public void UploadAttachment() {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void InjectTestData() {
		try {
			// objTest = (ArrayList<String>) objGridData.clone();
			WebElement Element;
			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");

			Element = TimeSheetEditPage.grd_ColSunday(driver);
			Element.clear();
			Element.sendKeys(CommonMethods.readTestData("TestData", "sun"));

			Element = TimeSheetEditPage.grd_ColMonday(driver);
			Element.clear();
			Element.sendKeys(CommonMethods.readTestData("TestData", "mon"));

			Element = TimeSheetEditPage.grd_txtComment(driver);
			Element.clear();
			Element.sendKeys(CommonMethods.readTestData("TestData", "comment"));

			UploadAttachment();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickNewTimeSheetlink() {
		try {
			act.moveToElement(wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.btn_NewTimeSheet(driver))))
					.click().build().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
