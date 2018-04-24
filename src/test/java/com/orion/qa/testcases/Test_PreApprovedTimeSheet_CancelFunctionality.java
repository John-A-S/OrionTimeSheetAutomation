package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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

public class Test_PreApprovedTimeSheet_CancelFunctionality {
	WebDriver driver;
	WebDriverWait wait;
	Actions act;
	JavascriptExecutor jse;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";
	String strExistingComment;

	int RowNumb;
	int AttachmentRowNo;

	@Parameters("Browser")

	@BeforeClass
	public void InitObjects(String Browser) {
		try {

			System.out.println("********** Test_PreApprovedTimeSheet_CancelFunctionality ************* ");

			CommonMethods.readExcel_Paths();

			if (Browser.equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
			}else if (Browser.equalsIgnoreCase("ie")) { 
				System.setProperty(IEbrowser, CommonMethods.IE_Browser_Location);
				driver = new InternetExplorerDriver();
			}else if (Browser.equalsIgnoreCase("chrome")) { 
				//System.setProperty(Chromebrowser, CommonMethods.Chrome_Browser_Location);
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
		System.out.println("********** Test_PreApprovedTimeSheet_CancelFunctionality ************* ");
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
		// RowNumb will have the row number of Pre-Approved timesheet //
		RowNumb = TimeSheetMainPage.ReadMonthlyDatafromGridtoElement(driver, 'P');
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
	public  void Test_CancelButton_IsDisplayed() {
		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		assertEquals(TimeSheetEditPage.verifyCancelButtonExists(driver), true);
	}
	
	@Test(priority = 4, dependsOnMethods = { "Test_CancelButton_IsDisplayed" })
	public  void Test_CancelButton_InjectTestDataandVerify() {
		strExistingComment = ReadCurrentData();
		InjectTestData();
		TimeSheetEditPage.ScrollScreenToCancelButtonAndClick(driver, jse);
	}
	
	@Test(priority = 5, dependsOnMethods = { "Test_CancelButton_InjectTestDataandVerify" })
	public void Test_IfUpdatedDataisCancelled() {
		try {
			clicklink(RowNumb);
			TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
			/* ChkTestFileisCancelled - To ensure uploaded file is not saved
			 !(strOldComment.equals("This is from Inject Data method") - To ensure test data is not saved */
			if (ChkTestFileisCancelled() && !(strExistingComment.equals("This is from Inject Data method"))) {
      			assertTrue(true);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 6, dependsOnMethods = { "Test_IfUpdatedDataisCancelled" })
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
	
	public String getLatestUploadFile() {
		try {
			List<WebElement> Rows = TimeSheetEditPage.grd_AttachmentData(driver).findElements(By.tagName("tr"));
			int RowValue = 1;
			if (Rows.size() >= 1) {
				RowValue = Rows.size();
				/* AttachmentRowNo is the row id where test file is uploaded */	
				if (RowValue == AttachmentRowNo)
				{
					List<WebElement> Cols = Rows.get(RowValue - 1).findElements(By.tagName("td"));
					Cols.get(0).getText();
				}
			}
			return "";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "Inside getLatestUploadFile Exception";
		}
	}

	public boolean ChkTestFileisCancelled() {
		try {
			if (getLatestUploadFile() == "")
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public void InjectTestData() {
		try {
			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");
			
			WebElement Element1 = TimeSheetEditPage.grd_txtComment(driver);
			Element1.clear();
			Element1.sendKeys("This is from Inject Data method");

			UploadAttachment();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String ReadCurrentData() {
		try {
			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");
			
			WebElement Element1 = TimeSheetEditPage.grd_txtComment(driver);
			return Element1.getText();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
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
					.sendKeys("C:\\Users\\jasel\\FileCompare\\abc1.docx");
			AttachmentRowNo = RowValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
