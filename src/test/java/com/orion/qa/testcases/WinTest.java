package com.orion.qa.testcases;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.apache.http.client.ClientProtocolException;
import com.orion.qa.utils.CommonMethods;

public class WinTest {

	public static WebDriver driver;
	public static ChromeOptions options;

	public static String chromeDriverPath = System.getProperty("user.dir") + "\\src\\main\\input\\chromedriver.exe";
	public static String chromeDownloadPath = System.getProperty("user.dir") + "\\src\\main\\input\\download\\";

	@BeforeMethod()
	public void init() {
		CommonMethods.readExcel_Paths();
	}

	@Test()
	public void downloadfile() throws ClientProtocolException, IOException, InterruptedException {
		
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		options = new ChromeOptions();
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions"); 

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", chromeDownloadPath);
		
		options.setExperimentalOption("prefs", chromePrefs);

		driver = new ChromeDriver(options);

		/*
        driver.get("http://www.seleniumhq.org/download/");
		System.out.println("After get" + driver.toString());
		driver.findElement(By.linkText("32 bit Windows IE")).click();
		System.out.println("After linkText " + driver.toString());
		 */
		
		

		driver.get("http://www.cvtemplatemaster.com");
		Thread.sleep(2000);
		System.out.println("Got it :" + driver.findElement(By.linkText("Got it!")).isDisplayed());
		driver.findElement(By.linkText("Got it!")).click();
		System.out.println("Got it : After click");
		Thread.sleep(2000);
		
		Actions act = new Actions(driver);
		System.out.println("Before action.movetoelement");
		act.moveToElement(driver.findElement(By.xpath("//a[contains(text(), 'CV templates')]"))).perform();
		System.out.println("After action.movetoelement");
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'CV templates')]")));
		driver.findElement(By.xpath("//a[contains(text(), 'CV templates')]")).click();
		
		Thread.sleep(5000);
		ScrollScreenToElement(driver, driver.findElement(By.id("subbutton")));
		Thread.sleep(5000);
		driver.findElement(By.id("subbutton")).click();
		Thread.sleep(5000);
		ScrollScreenToElement(driver, driver.findElement(By.linkText("Free download")));
		//setDownloadSettings("CV_Template_A4_Prof.docx");
		driver.findElement(By.linkText("Free download")).click();
		Thread.sleep(2000);
		
		File f = new File(chromeDownloadPath + "CV_Template_A4_Prof.docx");
		if (f.exists()) {
			System.out.println("Successfully downloaded");
		}
		
		
		
		driver.get("http://192.168.1.226:8080/orion-web/app/");
		Thread.sleep(3000);
	
		driver.findElement(By.xpath("//input[@placeholder='User ID']")).sendKeys("John");
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("infomatics@123");
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		
		Thread.sleep(2000);
		
		driver.findElement(By.linkText("04/29/2018 - 05/05/2018")).click();
		
		Thread.sleep(2000);
		
		ScrollScreenToElement(driver, driver.findElement(By.xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]")));

		driver.findElement(By.xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]")).click();
		
		Thread.sleep(2000);
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
		driver.quit();
	}

}
