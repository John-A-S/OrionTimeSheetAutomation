package com.orion.qa.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orion.qa.utils.CommonMethods;

public class OrionBase {
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Actions act;
	public static JavascriptExecutor jse;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";

	public OrionBase() {
		CommonMethods.readExcel_Paths();
	}

	public static void init(String Browser, Boolean isDownloadReq) {
		if (Browser.equalsIgnoreCase("firefox")) {
			if (isDownloadReq) {
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
			}
			else
				driver = new FirefoxDriver();
		} else if (Browser.equalsIgnoreCase("ie")) {
			driver = new InternetExplorerDriver();
		} else if (Browser.equalsIgnoreCase("chrome")) {
			if (isDownloadReq) {
				 // org.apache.log4j.BasicConfigurator.configure();	
				// System.setProperty(Chromebrowser, CommonMethods.Chrome_Browser_Location);
				/* following code is to download files using Chrome browser */
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", CommonMethods.Attachment_File_Download_Location);
				ChromeOptions options = new ChromeOptions();
				//for linux
			    options.setBinary(System.getProperty("user.dir")+"/src/main/input/chromedriver");
				
				HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
				options.setExperimentalOption("prefs", chromePrefs);
				
				options.addArguments("--test-type");
				options.addArguments("--disable-extensions"); // to disable browser extension popup
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				
				cap.setCapability("webdriver.chrome.args", Arrays.asList("--whitelisted-ips=127.0.0.1"));
				
				cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				//System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver");
				driver = new ChromeDriver(cap);
			}
			else {
				// System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");
			    System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//src//main//input//chromedriver");
				driver = new ChromeDriver();
			}
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.get(CommonMethods.URL_TimeSheet);

		wait = new WebDriverWait(driver, 100);
		act = new Actions(driver);
		jse = (JavascriptExecutor) driver;

	}
	
	public static void CloseBrowser() {
		if (!driver.toString().contains("null")) {
			driver.quit();
		}
	}

}
