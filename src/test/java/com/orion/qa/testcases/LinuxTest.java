package com.orion.qa.testcases;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.qa.utils.CommonMethods;

public class LinuxTest {

	//public WebDriver driver;
	public static ChromeDriverService driverService;
	public static ChromeDriver driver;
	public static ChromeOptions options;
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
	
	public static void setDownloadSettings(String filename) throws ClientProtocolException, IOException {
		
		System.out.println("Inside setDownloadSettings");

		Map<String, String> params = new HashMap<String, String>();
		params.put("behavior", "allow");
		params.put("downloadPath", CommonMethods.Attachment_File_Download_Location);

		Map<String, Object> commandParams = new HashMap<String, Object>();
		commandParams.put("cmd", "Page.setDownloadBehavior");
		commandParams.put("params", params);
		
		if (filename != "") {
			System.out.println("Filename : "+filename);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();
        String command = objectMapper.writeValueAsString(commandParams);
        System.out.println("Command : "+ command);
        
        String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
        System.out.println("u : "+u);
        HttpPost request = new HttpPost(u);
        request.addHeader("content-type", "application/zip");
        if (filename != "") {
        	request.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"" );
            System.out.println("Content Dispostion :"+ "attachment; filename=\"" + filename + "\"");
        } 

        request.setEntity(new StringEntity(command));
        HttpResponse res = httpClient.execute(request);
        System.out.println(res.getStatusLine().getStatusCode());
        
	}

	@Test()
	public void downloadfile() throws ClientProtocolException, IOException, InterruptedException {
		
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		options = new ChromeOptions();
		options.addArguments("--test-type");
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-extensions"); // to disable browser extension popup

		driverService = ChromeDriverService.createDefaultService();
		driver = new ChromeDriver(driverService, options);

		System.out.println("Before get" + driver.toString());
		
		/* setDownloadSettings("");
        driver.get("http://www.seleniumhq.org/download/");
		System.out.println("After get" + driver.toString());
		driver.findElement(By.linkText("32 bit Windows IE")).click();
		System.out.println("After linkText " + driver.toString()); */

		driver.get("http://192.168.1.226:8080/orion-web/app/");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@placeholder='User ID']")).sendKeys("John");
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("infomatics@123");
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		
		Thread.sleep(3000);
		
		driver.findElement(By.linkText("04/29/2018 - 05/05/2018")).click();
		
		Thread.sleep(2000);
		
		System.out.println(driver.findElement(By.xpath("//h3")).getText());
		
		Thread.sleep(2000);

		ScrollScreenToElement(driver, driver.findElement(By.xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]")));

		setDownloadSettings("John Joseph_04/29/2018 - 05/05/2018_0.docx");
		//setDownloadSettings("John Joseph_04_29_2018 - 05_05_2018_0.docx", true);
		System.out.println("Title : " + driver.getTitle()+ " Current URL : "+driver.getCurrentUrl());
		
		driver.navigate().refresh();
		driver.findElement(By.xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]")).click();
		
		Thread.sleep(10000);
		
		System.out.println("After Download");
	}
	
	public static void ScrollScreenToElement(WebDriver driver, WebElement element) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@AfterMethod()
	public void closeBrowser() {
		//driver.quit();
	}

}
