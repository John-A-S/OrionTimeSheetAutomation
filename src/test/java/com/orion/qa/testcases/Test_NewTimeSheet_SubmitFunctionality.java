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

public class Test_NewTimeSheet_SubmitFunctionality extends OrionBase {
	ArrayList<String> objTest;
	ArrayList<String> objGridData;

	String NewReportPeriod;
	int RowNumb;
	boolean isAttachmntExist, isSameFiles, isAttachmentdisabled;

	public Test_NewTimeSheet_SubmitFunctionality() {
		super();
	}

	
	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		try {

			System.out.println("********** Test_NewTimeSheet_SubmitFunctionality START ************* ");
			
			init(Browser, true);

			objTest = new ArrayList<String>();
			objGridData = new ArrayList<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		CloseBrowser();
		System.out.println("********** Test_NewTimeSheet_SubmitFunctionality END ************* ");
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

	@Test(priority = 3, dependsOnMethods = { "Test_IfNewTimeSheetPage_Isdisplayed" })
	public void Test_IfSubmitMessage_IsDisplayed() {
		try {

			Select rptPeriod = new Select(TimeSheetEditPage.lbl_ReportDate(driver));
			WebElement ele = rptPeriod.getFirstSelectedOption();
			NewReportPeriod = ele.getText();
			
			InjectTestData();
			TimeSheetEditPage.ScrollScreenToSubmitButtonAndClick(driver, jse);
			Thread.sleep(1000);
			//WebElement ElementMsg = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

			// String strSaveMsg = ElementMsg.getText();

			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSubmit_OK(driver, wait)).click().build().perform();
			
			Thread.sleep(1000);
			WebElement ElementMsg1 = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

			String strSaveMsg1 = ElementMsg1.getText();

			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();
			
			//assertEquals(strSaveMsg1, "Warning: No modification is allowed after submission. Please use \"Contact Us\" form if you did that in error.");
			assertEquals(strSaveMsg1, "Time Sheet Submitted Successfully.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 4, dependsOnMethods = { "Test_IfSubmitMessage_IsDisplayed" })
	public void Test_IfDataSubmittedCorrectly() {
		try {

			// Get the last row which is added as New timesheet
			/*
			 * WebElement TableData = TimeSheetMainPage.grd_MonthlyData(driver);
			 * List<WebElement> Rows = TableData.findElements(By.tagName("tr"));
			 * clicklink(Rows.size());
			 */
			Thread.sleep(3000);
			TimeSheetMainPage.grd_clickReportPeriodLink(driver, NewReportPeriod).click();

			objGridData.clear();
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			DownloadfileAndComparewithTestFile();
			assertEquals(((CommonMethods.compareList(objTest, objGridData)) && isSameFiles), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 5, dependsOnMethods = { "Test_IfDataSubmittedCorrectly" })
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

	public void clickNewTimeSheetlink() {
		try {
			act.moveToElement(wait.until(ExpectedConditions.visibilityOf(TimeSheetMainPage.btn_NewTimeSheet(driver))))
					.click().build().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void UploadAttachment() {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getLatestUploadFile() {
		try {
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			int RowValue = 1;
			if (Rows.size() > 1) {
				RowValue = Rows.size();
			}
			List<WebElement> Cols = Rows.get(RowValue - 1).findElements(By.tagName("td"));
			return Cols.get(0).getText();
		} catch (Exception e) {
			e.printStackTrace();
			return "Inside getLatestUploadFile Exception";
		}
	}

	public void DownloadfileAndComparewithTestFile() {
		try {
			String TempFileName = getLatestUploadFile();
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText(TempFileName)));
			driver.findElement(By.linkText(TempFileName)).click();
			Thread.sleep(9000);
			TempFileName = TempFileName.replace("/", "_");
			if (CommonMethods.CompareFilesbyByte(CommonMethods.Sample_FileNamewithPath,
					CommonMethods.Attachment_File_Download_Location + TempFileName) == true) {
				isSameFiles = true;
			} else {
				isSameFiles = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void InjectTestData() {
		try {

			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");

			// populate default value to the test data
			objTest.add(0, CommonMethods.readTestData("TestData", "company"));
				
			WebElement Element = TimeSheetEditPage.grd_ColSunday(driver);
			Element.clear();
			Element.sendKeys(CommonMethods.readTestData("TestData", "sun"));
			objTest.add(1, CommonMethods.readTestData("TestData", "sun"));

			WebElement Element1 = TimeSheetEditPage.grd_ColMonday(driver);
			Element1.clear();
			Element1.sendKeys(CommonMethods.readTestData("TestData", "mon"));
			objTest.add(2, CommonMethods.readTestData("TestData", "mon"));

			// populate default value to the test data
			objTest.add(3, CommonMethods.readTestData("TestData", "tue"));

			// populate default value to the test data
			objTest.add(4, CommonMethods.readTestData("TestData", "wed"));

			// populate default value to the test data
			objTest.add(5, CommonMethods.readTestData("TestData", "thu"));

			// populate default value to the test data
			objTest.add(6, CommonMethods.readTestData("TestData", "fri"));

			// populate default value to the test data
			objTest.add(7, CommonMethods.readTestData("TestData", "sat"));

			WebElement Element2 = TimeSheetEditPage.grd_txtComment(driver);
			Element2.clear();
			Element2.sendKeys(CommonMethods.readTestData("TestData", "comment"));
			objTest.add(8, CommonMethods.readTestData("TestData", "comment"));

			UploadAttachment();

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
