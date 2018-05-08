package com.orion.qa.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class LinuxTest_csv {
	
	
	public static void ScrollScreenToElement(WebDriver driver, WebElement element) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test()
	 public static void doScrape(String[] urls) {
		 try
		 {
			 for(String url : urls) {
	         //Create new Chromedriver, set file download path, allow the download popup to be automatically accepted,and merge the properties into chromedriver
	         System.setProperty("webdriver.chrome.driver", "/src/main/input/chromedriver");
	         String downloadFilepath = "/src/main/input/download";

	         ChromeOptions options = new ChromeOptions();
	         options.addArguments("--test-type");
	         //options.addArguments("--headless");
	         options.addArguments("--disable-extensions");
	 			options.addArguments("--headless");
	 			options.addArguments("--no-sandbox");

	         //Instantiate above options in driverService
	         ChromeDriverService driverService = ChromeDriverService.createDefaultService();
	         ChromeDriver driver = new ChromeDriver(driverService, options);


	         Map<String, Object> commandParams = new HashMap<String, Object>();
	         commandParams.put("cmd", "Page.setDownloadBehavior");

	         Map<String, Object> params = new HashMap<String, Object>();
	         params.put("behavior", "allow");
	         params.put("downloadPath", downloadFilepath);
	         params.put("cmd", "Page.setDownloadBehavior");


	         commandParams.put("params", params);
	         ObjectMapper om = new ObjectMapper();
	         CloseableHttpClient httpClient = HttpClients.createDefault();
	         String command = null;
	         try
	         {
	        	 command = om.writeValueAsString(commandParams);
	         }
	         catch(JsonProcessingException jpe)
	         { 
	        	 jpe.printStackTrace(); 
	         }
	         String postURL = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
	         HttpPost postRequest = new HttpPost(postURL);
	         postRequest.addHeader("content-type", "application/json");
	         postRequest.addHeader("accept", "*.*");
	         try
	         {
	        	 postRequest.setEntity(new StringEntity(command));
	             httpClient.execute(postRequest);
	         }
	         catch (UnsupportedEncodingException uee) 
	         { 
	        	 uee.printStackTrace(); 
	         }
	         catch (IOException ioe) 
	         { 
	        	 ioe.printStackTrace(); 
	         }


	 		driver.get("http://192.168.1.226/orion-web/app/"); 
			Thread.sleep(2000);
			driver.findElement(By.xpath("//input[@placeholder='User ID']")).sendKeys("John");
			driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("infomatics@123");
			driver.findElement(By.xpath("//button[text()='Login']")).click();
			System.out.println("After login button click");
			Thread.sleep(3000);

			driver.findElement(By.linkText("05/06/2018 - 05/12/2018")).click();
			System.out.println("After linkText -> 05/06/2018 - 05/12/2018 click");

			Thread.sleep(2000);

			System.out.println(driver.findElement(By.xpath("//h3")).getText());

			Thread.sleep(2000);

			ScrollScreenToElement(driver, driver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")));
			System.out.println("After ScrollScreenToElement ");

			
			System.out.println("isDownload document link displayed: "
					+ driver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")).isDisplayed());

			driver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")).click();
					
			Thread.sleep(50000);
			System.out.println("After download link click");
			
			File f = new File(downloadFilepath + "john.docx");

			if (f.exists()) {
				System.out.println("Successfully downloaded");
			}

             driver.quit();
         }
     }catch( java.lang.InterruptedException inter ){ System.err.println("Thread.sleep broke something, wtf"); inter.printStackTrace(); }
 }
}