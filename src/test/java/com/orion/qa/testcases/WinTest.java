package com.orion.qa.testcases;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
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
	
	
	public static void clickAndSaveFile(WebElement element) throws InterruptedException {
		try {
			Robot robot = new Robot();
		    //get the focus on the element..don't use click since it stalls the driver          
		    element.sendKeys("");
		    

		    System.out.println(element.getText());
		    //simulate pressing enter
		    element.click();
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);

		    //wait for the modal dialog to open            
		    Thread.sleep(2000);
		    
		    //press s key to save            
		    robot.keyPress(KeyEvent.VK_S);
		    robot.keyRelease(KeyEvent.VK_S);
		    Thread.sleep(2000);

		    //press enter to save the file with default name and in default location
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    
		 } 
		catch (AWTException e) {
			e.printStackTrace();
		}	
		}
		
	//public static void DownloadDocfromOrion() throws InterruptedException, ClientProtocolException, IOException {
	public static void DownloadDocfromOrion() throws InterruptedException {
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


		System.out.println("isDownload document link displayed: "
				+ driver.findElement(By.linkText("john.docx")).isDisplayed());
	
		clickAndSaveFile(driver.findElement(By.linkText("john.docx")));
		
		//Thread.sleep(10000);
		/*
		driver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")).click();
		System.out.println("After download link click");
		
		Thread.sleep(5000);
		
		File f = new File(chromeDownloadPath + "john.docx");

		if (f.exists()) {
			System.out.println("Successfully downloaded");
		}
		*/
	}	
	
	public static void DownloadDocfromExternal() throws InterruptedException, ClientProtocolException, IOException {
		
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
		System.out.println("After CV templates click");
		
		Thread.sleep(2000);
		ScrollScreenToElement(driver, driver.findElement(By.id("subbutton")));
		Thread.sleep(2000);
		driver.findElement(By.id("subbutton")).click();
		System.out.println("After subbuton click");

		Thread.sleep(2000);
		ScrollScreenToElement(driver, driver.findElement(By.linkText("Free download")));
		
		// clickAndSaveFile(driver.findElement(By.linkText("Free download")));		
		
		
		driver.findElement(By.linkText("Free download")).click();
		/*Thread.sleep(2000);
		
		File f = new File(chromeDownloadPath + "CV_Template_A4_Prof.docx");

		if (f.exists()) {
			System.out.println("Successfully downloaded");
			}
			*/
	}
	
	@Test()
	public void downloadfile() throws ClientProtocolException, IOException, InterruptedException {
		/*Chrome */
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		driver = new ChromeDriver();

		options = new ChromeOptions();
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions"); 

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		//chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", chromeDownloadPath);
		chromePrefs.put("download.prompt_for_download", false);
		chromePrefs.put("download.directory_upgrade", true);
		chromePrefs.put("safebrowsing.enabled", true);
		
		options.setExperimentalOption("prefs", chromePrefs);
		driver = new ChromeDriver(options);
	
		
		
		
		//DownloadDocfromExternal();
	    DownloadDocfromOrion();
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
