package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
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

public class Test_RejectedTimeSheet_SubmitFunctionality {
	WebDriver driver;
	WebDriverWait wait;
	Actions act;
	JavascriptExecutor jse;
	ArrayList<String> objTest;
	ArrayList<String> objGridData;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";

	int RowNumb;
	boolean isAttachmntExist, isSameFiles, isAttachmentdisabled;

	@Parameters("Browser")
	@BeforeClass
	public void InitObjects(String Browser) {

		System.out.println("********** Test_RejectedTimeSheet_SubmitFunctionality ************* ");
		
		try {
			CommonMethods.readExcel_Paths();

			if (Browser.equalsIgnoreCase("firefox")) {
				FirefoxProfile profile = new FirefoxProfile();
				DesiredCapabilities dc = DesiredCapabilities.firefox();
				profile.setAcceptUntrustedCertificates(false);
				profile.setAssumeUntrustedCertificateIssuer(true);
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.helperApps.alwaysAsk.force", false);
				profile.setPreference("browser.download.manager.showWhenStarting", false);
				profile.setPreference("browser.download.dir", CommonMethods.Attachment_File_Download_Location);
				profile.setPreference("browser.download.downloadDir", CommonMethods.Attachment_File_Download_Location);
				profile.setPreference("browser.download.defaultFolder",
						CommonMethods.Attachment_File_Download_Location);
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"application/plain, application/msword");
				dc = DesiredCapabilities.firefox();
				dc.setCapability(FirefoxDriver.PROFILE, profile);
				driver = new FirefoxDriver(dc);
			} else if (Browser.equalsIgnoreCase("ie")) {
				driver = new InternetExplorerDriver();
			} else if (Browser.equalsIgnoreCase("chrome")) {
				// System.setProperty(Chromebrowser, CommonMethods.Chrome_Browser_Location);
				/* following code is to download files using Chrome browser */
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", CommonMethods.Attachment_File_Download_Location);
				ChromeOptions options = new ChromeOptions();
				HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--test-type");
				options.addArguments("--disable-extensions"); // to disable browser extension popup
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new ChromeDriver(cap);
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
		System.out.println("********** Test_RejectedTimeSheet_SubmitFunctionality ************* ");
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

	@Test(priority = 3, dependsOnMethods = { "Test_IfEditTimeSheetPage_Isdisplayed" })
	public void Test_IfSubmitMessage_IsDisplayed() {
		try {
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			InjectTestData();
			TimeSheetEditPage.ScrollScreenToSubmitButtonAndClick(driver, jse);
			Thread.sleep(2000);

			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSubmit_OK(driver, wait)).click().build().perform();
			
			Thread.sleep(1000);
			WebElement ElementMsg1 = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

			String strSaveMsg1 = ElementMsg1.getText();

			act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();

			assertEquals(strSaveMsg1, "Time Sheet Submitted Successfully.");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 5, dependsOnMethods = { "Test_IfSubmitMessage_IsDisplayed" })
	public void Test_IfDataSavedCorrectly() {
		try {
			Thread.sleep(1000);
			clicklink(RowNumb);
			objGridData.clear();
			objGridData = TimeSheetEditPage.ReadWeeklyDatafromGridtoElement(driver, wait, jse);
			DownloadfileAndComparewithTestFile();
			assertEquals(((CommonMethods.compareList(objTest, objGridData)) && isSameFiles), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 6, dependsOnMethods = { "Test_IfDataSavedCorrectly" })
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
			TempFileName = TempFileName.replace("/", "_");
			Thread.sleep(9000);
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

	@SuppressWarnings("unchecked")
	public void InjectTestData() {
		try {
			objTest = (ArrayList<String>) objGridData.clone();

			CommonMethods.ScrollScreenToElement(driver, jse,
					".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input");

			WebElement Element = TimeSheetEditPage.grd_ColSunday(driver);
			Element.clear();
			Element.sendKeys("25");
			objTest.set(1, "25");

			WebElement Element1 = TimeSheetEditPage.grd_ColMonday(driver);
			Element1.clear();
			Element1.sendKeys("15");
			objTest.set(2, "15");

			WebElement Element2 = TimeSheetEditPage.grd_txtComment(driver);
			Element2.clear();
			Element2.sendKeys("This is from Inject Data method");
			objTest.set(8, "This is from Inject Data method");

			UploadAttachment();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
