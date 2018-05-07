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
		log.info("After calling Base class");

	}

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		try {
			
			log.info("********** Test_NewTimeSheet_CancelFunctionality START ************* ");
			log.info("Inside InitObjects");	
			log.info("Browser parameter value: "+Browser);

			System.out.println("********** Test_NewTimeSheet_SaveFunctionality START ************* ");
			init(Browser, true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in method InitObjects "+ e.getMessage());

		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_NewTimeSheet_SaveFunctionality END ************* ");
		log.info("********** Test_DraftTimeSheet_SubmitFunctionality END *************");

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
	public void Test_IfNewTimeSheetPage_Isdisplayed() {
		
		log.info("Inside Test_IfNewTimeSheetPage_Isdisplayed method");
		log.debug("Click New TimeSheet");

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

	public void Test_InjectTestDataandCancel() {
		
		log.info("Inside Test_InjectTestDataandCancel");
		
		InjectTestData();
		log.debug("Navigage to Cancel button and click");
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
		log.info("Cancel button clicked");
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfNewTimeSheetPage_Isdisplayed" })
	public void Test_IfDatanotSaved() {
		try {
			log.info("Inside Test_IfDatanotSaved");
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
				log.error("Exception in Test_IfDatanotSaved method. New Time Period not found. "+e.getMessage());
				assertEquals(false, false, "PASSED");
			}
		} catch (Exception e) {
			System.out.println("Inside 2nd Catch");
			log.error("Exception in Test_IfDatanotSaved method. Inside 2nd Catch. "+e.getMessage());
			assertEquals(false, false);
			// e.printStackTrace();
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

	public void InjectTestData() {
		try {
			
			log.info("Inside InjectTestData");

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
			log.info("Test data added to the screen");


		} catch (Exception e) {
			log.error("Exception in InjectTestData "+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void clickNewTimeSheetlink() {
		try {
			log.info("Inside clickNewTimeSheetlink");
			log.debug("Initiate New TimeSheet button click");
			act.moveToElement(wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.btn_NewTimeSheet(driver))))
					.click().build().perform();
			log.info("New TimeSheet button clicked");
		} catch (Exception e) {
			log.error("Exception in clickNewTimeSheetlink "+e.getMessage());
			e.printStackTrace();
		}
	}
}
