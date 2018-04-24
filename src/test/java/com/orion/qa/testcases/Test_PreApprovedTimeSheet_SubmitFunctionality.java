package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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

public class Test_PreApprovedTimeSheet_SubmitFunctionality {
	WebDriver driver;
	WebDriverWait wait;
	Actions act;
	JavascriptExecutor jse;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";
	String strExistingComment;

	int RowNumb;
	int AttachmentRowNo;

	boolean isSameFiles;
	@Parameters("Browser")

	@BeforeClass
	public void InitObjects(String Browser) {
		try {

			System.out.println("********** Test_PreApprovedTimeSheet_SubmitFunctionality ************* ");

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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void CloseObjects() {
		if (!driver.toString().contains("null")) {
			driver.quit();
		}
		System.out.println("********** Test_PreApprovedTimeSheet_SubmitFunctionality ************* ");
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
	public  void Test_SubmitButton_IsDisplayed() {
		TimeSheetEditPage.ScrollToSUBMITSAVECANCEL(driver, jse);
		assertEquals(TimeSheetEditPage.verifySubmitButtonExists(driver), true);
	}
	
	@Test(priority = 4, dependsOnMethods = {"Test_SubmitButton_IsDisplayed"} )
	public void Test_VerifyGridisDisabled() {
		// to confirm the grid is disabled as of now we just check one column
		assertFalse(TimeSheetEditPage.grd_ColMonday(driver).isEnabled());
	}
	
	@Test(priority = 5, dependsOnMethods = { "Test_VerifyGridisDisabled" })
	public  void Test_SubmitButton_InjectTestDataandVerify() throws InterruptedException {
		//strExistingComment = ReadCurrentData();
		InjectTestData();
		TimeSheetEditPage.ScrollScreenToSubmitButtonAndClick(driver, jse);
		
		Thread.sleep(1000);

		act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSubmit_OK(driver, wait)).click().build().perform();
		
		Thread.sleep(1000);
		WebElement ElementMsg1 = TimeSheetEditPage.Wait_Msg_TimeSheetSave(driver, wait);

		String strSaveMsg1 = ElementMsg1.getText();

		act.moveToElement(TimeSheetEditPage.Wait_Msg_TimeSheetSave_OK(driver, wait)).click().build().perform();
		
		assertEquals(strSaveMsg1, "Time Sheet Submitted Successfully.");
		
	}
	
	@Test(priority = 5, dependsOnMethods = { "Test_SubmitButton_InjectTestDataandVerify" })
	public void Test_IfUpdatedDataisSubmitted() {
		try {
			
			System.out.println("Row Number : " + RowNumb);
			clicklink(RowNumb);
			
			TimeSheetEditPage.ScrollScreenToElement(driver, jse, TimeSheetEditPage.grd_txtComment(driver));
			
			String comment = TimeSheetEditPage.grd_txtComment(driver).getAttribute("value");
			
			boolean isCommentTextSame = comment.equals("This is from Inject Data method");
			
			System.out.println("isCommentTextSame "+isCommentTextSame);
			DownloadfileAndComparewithTestFile();
			
			assertEquals( (isCommentTextSame && isSameFiles), true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DownloadfileAndComparewithTestFile() {
		try {
			String TempFileName = getLatestUploadFile();
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText(TempFileName)));
			driver.findElement(By.linkText(TempFileName)).click();
			TempFileName = TempFileName.replace("/", "_");
			Thread.sleep(5000);
			System.out.println("Sample file name "+ CommonMethods.Sample_FileNamewithPath + "Attachment File :" + CommonMethods.Attachment_File_Download_Location + TempFileName);	
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


	@Test(priority = 6, dependsOnMethods = { "Test_IfUpdatedDataisSubmitted" })
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
			System.out.println("Row Size"+ Rows.size()+ " AttachmentRow No: " + AttachmentRowNo);
			int RowValue = 1;
			if (Rows.size() >= 1) {
				RowValue = Rows.size();
				/* AttachmentRowNo is the row id where test file is uploaded */	
				System.out.println("Row Value " + RowValue+" AttachmentRowNo " + AttachmentRowNo);
				if (RowValue == AttachmentRowNo)
				{
					System.out.println("Inside RowValue == AttachmentRowno");
					List<WebElement> Cols = Rows.get(RowValue - 1).findElements(By.tagName("td"));
					System.out.println(Cols.get(0).getText());
					return Cols.get(0).getText();
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
