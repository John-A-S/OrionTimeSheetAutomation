package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orion.qa.pages.LoginPage;
import com.orion.qa.pages.TimeSheetEditPage;
import com.orion.qa.pages.TimeSheetMainPage;
import com.orion.qa.utils.CommonMethods;

public class Test_RejectedTimeSheet_CancelFunctionality {
	WebDriver driver;
	WebDriverWait wait;
	Actions act;
	JavascriptExecutor jse;
	ArrayList<String> objTest;
	ArrayList<String> objGridData;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";

	int RowNumb;
	int AttachmentRowId;

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {
		System.out.println("********** Test_RejectedTimeSheet_CancelFunctionality ************* ");

		try {
			CommonMethods.readExcel_Paths();

			if (Browser.equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
			} else if (Browser.equalsIgnoreCase("ie")) {
				System.setProperty(IEbrowser, CommonMethods.IE_Browser_Location);
				driver = new InternetExplorerDriver();
			} else if (Browser.equalsIgnoreCase("chrome")) {
				// System.setProperty(Chromebrowser, CommonMethods.Chrome_Browser_Location);
				driver = new ChromeDriver();
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.get(CommonMethods.URL_TimeSheet);

			wait = new WebDriverWait(driver, 100);
			act = new Actions(driver);
			jse = (JavascriptExecutor) driver;
			objTest = new ArrayList<String>();
			objGridData = new ArrayList<String>();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		if (!driver.toString().contains("null")) {
			driver.quit();
		}
		System.out.println("********** Test_RejectedTimeSheet_CancelFunctionality ************* ");
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
		// RowNumb will have the row number of draft timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'R');
		if (RowNumb <= 0) {
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

	public void Test_InjectTestDataandCancel() throws InterruptedException {
		objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
		InjectTestData();
		System.out.println("InjectTestData");
		Thread.sleep(5000);
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
		System.out.println("After ScrollScreenToCancelButtonAndClick");
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public void Test_IfDatanotSaved() {
		try {
			Test_InjectTestDataandCancel();
			clicklink(RowNumb);
			objGridData.clear();
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			assertEquals((!(CommonMethods.compareList(objTest, objGridData)) && chkUploadFileisCancelled()), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 6, dependsOnMethods = { "Test_IfDatanotSaved" })
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
					.sendKeys("C:\\Users\\jasel\\FileCompare\\abc1.docx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean chkUploadFileisCancelled() {
		try {
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			if (Rows.size() == AttachmentRowId) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public void InjectTestData() {
		try {
			objTest = (ArrayList<String>) objGridData.clone();

			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");

			WebElement Element = TimeSheetEditPage.grd_ColSunday(driver);
			Element.clear();
			Element.sendKeys("5");
			objTest.set(1, "5");

			WebElement Element1 = TimeSheetEditPage.grd_ColMonday(driver);
			Element1.clear();
			Element1.sendKeys("1");
			objTest.set(2, "1");

			WebElement Element2 = TimeSheetEditPage.grd_txtComment(driver);
			Element2.clear();
			Element2.sendKeys("This is from Inject Data method - Draft Cancel Functionality ");
			objTest.set(8, "This is from Inject Data method - Draft Cancel Functionality ");

			UploadAttachment();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
