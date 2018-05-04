package com.orion.qa.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.qa.utils.CommonMethods;

public class OrionBase {
	// public static WebDriver driver;
	public static ChromeDriver driver;
	public static ChromeOptions options;
	public static ChromeDriverService driverService;

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

	public static void init(String Browser, Boolean isDownloadReq) throws ClientProtocolException, IOException {
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
				//driver = new FirefoxDriver(dc);
			}
			else
				log.info("FireFoxDriver");
				//driver = new FirefoxDriver();
		} else if (Browser.equalsIgnoreCase("ie")) {
			log.debug("Setting IE driver property");
			//driver = new InternetExplorerDriver();
		} else if (Browser.equalsIgnoreCase("chrome")) {
			if (isDownloadReq) {
				log.debug("Setting Chrome driver property");
				/* following code is to download files using Chrome browser */

				//System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");

				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver");
				
				options = new ChromeOptions();
				options.addArguments("--test-type");
				options.addArguments("--headless");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-extensions"); // to disable browser extension popup

				driverService = ChromeDriverService.createDefaultService();
				driver = new ChromeDriver(driverService, options);

			/*	Map<String, Object> commandParams = new HashMap<String, Object>();
				commandParams.put("cmd", "Page.setDownloadBehavior");
				Map<String, String> params = new HashMap<String, String>();
				params.put("behavior", "allow");
				params.put("downloadPath", CommonMethods.Attachment_File_Download_Location);
				commandParams.put("params", params);
				
				ObjectMapper objectMapper = new ObjectMapper();
				HttpClient httpClient = HttpClientBuilder.create().build();
		        String command = objectMapper.writeValueAsString(commandParams);
		        String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
		        HttpPost request = new HttpPost(u);
		        request.addHeader("content-type", "application/json");
		        request.setEntity(new StringEntity(command));
		        httpClient.execute(request);
*/
				
				
			/*	HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				//chromePrefs.put("browser.setDownloadBehavior", "allow");
				chromePrefs.put("profile.default_content_settings.popups", 0);
				System.out.println("Download location "+CommonMethods.Attachment_File_Download_Location);
				//chromePrefs.put("downloadPath", CommonMethods.Attachment_File_Download_Location);
				chromePrefs.put("download.default_directory", CommonMethods.Attachment_File_Download_Location);

				//ChromeOptions options = new ChromeOptions();
				//for linux
				//options.addArguments("--headless");
				//options.addArguments("--no-sandbox");

				options.setExperimentalOption("prefs", chromePrefs);
				//options.addArguments("--test-type");
				//options.addArguments("--disable-extensions"); // to disable browser extension popup
  
				HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");
		        
		        
				driver = new ChromeDriver(cap);*/ 
				//driver = new ChromeDriver(options);
			}
			else {
				log.debug("Setting Chrome driver property");
				//System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//src//main//input//chromedriver.exe");
			    System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//src//main//input//chromedriver");
			    ChromeOptions options = new ChromeOptions();
				//for linux
				options.addArguments("--headless");
				options.addArguments("--no-sandbox");
			    driver = new ChromeDriver(options);
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
	
	public static void setDownloadProperties(String filename) throws ClientProtocolException, IOException {
		log.info("Inside setDownloadProperties" );
		
		System.out.println("Inside setDownloadProperties" + driver.getCurrentUrl());
		
		Map<String, Object> commandParams = new HashMap<String, Object>();
		commandParams.put("cmd", "Page.setDownloadBehavior");
		Map<String, String> params = new HashMap<String, String>();
		params.put("behavior", "allow");
		params.put("downloadPath", CommonMethods.Attachment_File_Download_Location);
		commandParams.put("params", params);

		System.out.println("Inside setDownloadProperties"  + driver.toString());
		
		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();
        String command = objectMapper.writeValueAsString(commandParams);
        System.out.println("Inside setDownloadProperties - command from Json " + command);
        String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
        System.out.println("Inside setDownloadProperties - value of u " + u);
        HttpPost request = new HttpPost(u);
        request.addHeader("content-type", "application/json");
        // header( 'Content-Disposition: attachment;filename="'.$filename.'"');
        // response.setHeader("Content-Disposition", "attachment; filename=" + zipFile.getFileName());
        //request.setHeader("Content-Disposition","attachment; filename=" + filename );

        //request.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        request.setEntity(new StringEntity(command));
        
        System.out.println(request.toString());
        System.out.println(request.headerIterator().toString());
        
        httpClient.execute(request);
        
        
	}
	

}
