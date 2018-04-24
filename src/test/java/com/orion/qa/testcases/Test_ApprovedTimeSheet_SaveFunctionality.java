package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orion.qa.pages.LoginPage;
import com.orion.qa.pages.TimeSheetEditPage;
import com.orion.qa.pages.TimeSheetMainPage;
import com.orion.qa.utils.CommonMethods;

public class Test_ApprovedTimeSheet_SaveFunctionality {
	WebDriver driver;
	WebDriverWait wait;
	Actions act;
	JavascriptExecutor jse;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";

	int RowNumb;
	@Parameters("Browser")

	@BeforeClass
	public void InitObjects(String Browser) {
		System.out.println("********** Test_ApprovedTimeSheet_SaveFunctionality ************* ");
		
		try {

			CommonMethods.readExcel_Paths();

			if (Browser.equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
			}else if (Browser.equalsIgnoreCase("ie")) { 
				System.setProperty(IEbrowser, CommonMethods.IE_Browser_Location);
				driver = new InternetExplorerDriver();
			}else if (Browser.equalsIgnoreCase("chrome")) { 
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		if (!driver.toString().contains("null")) {
			driver.quit();
		}
		System.out.println("********** Test_ApprovedTimeSheet_SaveFunctionality ************* ");
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
		
		/* Hardcoded to test the approved timesheet scenarios */
		Select period = new Select(driver.findElement(By.id("reportperiod")));
		period.selectByVisibleText("October, 2017");


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
		}

		assertEquals(wait.until(ExpectedConditions.visibilityOf(TimeSheetEditPage.lbl_TimeSheet(driver))).getText(),
				"TimeSheet Edit Time Sheet");
	}

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public  void Test_SaveButton_IsDisplayed() {
		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		assertEquals(TimeSheetEditPage.verifySaveButtonExists(driver), false);
	}


	@Test(priority = 4, dependsOnMethods = { "Test_SaveButton_IsDisplayed" })
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
