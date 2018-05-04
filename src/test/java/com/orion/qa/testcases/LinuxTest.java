package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.qa.utils.CommonMethods;

public class LinuxTest {

	public WebDriver driver;

	public static String chromeDriverPath = System.getProperty("user.dir") + "/src/main/input/chromedriver";
	
	public static String chromeDownloadPath = System.getProperty("user.dir") + "/src/main/input/download/";
	
	@BeforeMethod()
	public void init() {
		/* Chrome driver 
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--headless");
		options.addArguments("--no-sandbox");

		// options.addArguments("--disable-extensions");
		// options.addArguments("--disable-gpu");
		// options.setBinary("/usr/bin/chromium");

		driver = new ChromeDriver(options);
		driver.get("http://www.google.com");*/
		
		CommonMethods.readExcel_Paths();
	}

	@Test()
	public void verifyURL() {
		/*String title = driver.getTitle();
		System.out.println(title);
		assertEquals(title, "Google"); */
	}

	@Test()
	public void downloadfile() throws ClientProtocolException, IOException {
		
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type");
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-extensions"); // to disable browser extension popup

		ChromeDriverService driverService = ChromeDriverService.createDefaultService();
		ChromeDriver driver = new ChromeDriver(driverService, options);

		Map<String, Object> commandParams = new HashMap<String, Object>();
		commandParams.put("cmd", "Page.setDownloadBehavior");
		Map<String, String> params = new HashMap<String, String>();
		params.put("behavior", "allow");
		
		System.out.println("CommonMethods.Attachment_File_Download_Location "+ CommonMethods.Attachment_File_Download_Location);
		System.out.println("chromeDownloadPath "+ chromeDownloadPath);
		params.put("downloadPath", CommonMethods.Attachment_File_Download_Location);
		//params.put("downloadPath", chromeDownloadPath);
		commandParams.put("params", params);
		
		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();
        String command = objectMapper.writeValueAsString(commandParams);
        String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
        HttpPost request = new HttpPost(u);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(command));
        httpClient.execute(request);

        driver.get("http://www.seleniumhq.org/download/");
		driver.findElement(By.linkText("32 bit Windows IE")).click();

	}

	@AfterMethod()
	public void closeBrowser() {
		//driver.quit();
	}

}
