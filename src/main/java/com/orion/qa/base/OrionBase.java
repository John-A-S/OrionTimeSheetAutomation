package com.orion.qa.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.orion.qa.testcases.LinuxTest;
import com.orion.qa.utils.CommonMethods;

public class OrionBase {
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Actions act;
	public static JavascriptExecutor jse;
	public static Logger log;

	String Chromebrowser = "webdriver.chrome.driver";
	String IEbrowser = "webdriver.ie.driver";

	public OrionBase() {
		
		log = LogManager.getLogger(this.getClass().getName());
		log.info("Calling base class from "+this.getClass().getName()+" to read Excel Paths");
		
		CommonMethods.readExcel_Paths();

	}

	public static void init(String Browser, Boolean isDownloadReq) {
		log.info("Inside Orion base Init" );
		if (Browser.equalsIgnoreCase("firefox")) {
			if (isDownloadReq) {
				log.debug("Setting firefox driver property");
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
			log.debug("Setting IE driver property");
			driver = new InternetExplorerDriver();
		} else if (Browser.equalsIgnoreCase("chrome")) {
			if (isDownloadReq) {
				log.debug("Setting Chrome driver property");
				/* following code is to download files using Chrome browser */
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", CommonMethods.Attachment_File_Download_Location);
				ChromeOptions options = new ChromeOptions();
				  
				//for linux
			    // options.addArguments("--headless", "window-size=1024,768", "--no-sandbox");
			    // options.setBinary(System.getProperty("user.dir")+"/src/main/input/chromedriver");
				// System.out.println("after set binary ");
				
				HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
				options.setExperimentalOption("prefs", chromePrefs);
				
				options.addArguments("--test-type");
				options.addArguments("--disable-extensions"); // to disable browser extension popup
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				
			    cap.setCapability("webdriver.chrome.args", Arrays.asList("--whitelisted-ips=127.0.0.1"));
				
				cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");
				// System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
				// System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver");
				driver = new ChromeDriver(cap);
			}
			else {
				log.debug("Setting Chrome driver property");
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");
			    // System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//src//main//input//chromedriver");
				driver = new ChromeDriver();
			}
		}

		driver.manage().deleteAllCookies();
		log.info("Deleted all cookies");
		driver.manage().window().maximize();
		log.info("Windows Maximized");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.get(CommonMethods.URL_TimeSheet);

		wait = new WebDriverWait(driver, 100);
		act = new Actions(driver);
		jse = (JavascriptExecutor) driver;

	}
	
	public static void CloseBrowser() {
		log.info("Inside Close Browser");
		if (!driver.toString().contains("null")) {
			driver.quit();
		log.info("Browser closed");		
		}
	}

}
