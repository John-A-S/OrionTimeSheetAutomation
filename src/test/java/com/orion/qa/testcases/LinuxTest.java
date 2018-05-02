package com.orion.qa.testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LinuxTest {

	// public static String chromeDriverPath = System.getProperty("user.dir")+"/src/main/input/chromedriver.exe";
	//Below is for linux
	//public static String chromeDriverPath = "/usr/bin/chromedriver";
	//public static String chromeDriverPath = System.getProperty("user.dir")+"/src/main/input/chromedriver";
	
	public static String firefoxpath = System.getProperty("user.dir")+"/src/main/input/geckodriver.exe";
	public WebDriver driver;
	
	@BeforeMethod()
	public void init() {
/*		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		
		ChromeOptions options = new ChromeOptions();
		options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");

		driver = new ChromeDriver(options);
		driver.get("http://www.google.com");
*/		System.setProperty("webdriver.firefox.marionette", firefoxpath);

		driver = new FirefoxDriver();
		driver.get("http://www.google.com");

	// System.out.println("Inside init - Before Method");	
	}

	@Test()
	public void verifyURL(){
	/*	String title = driver.getTitle();
		System.out.println(title);
		assertEquals(title, "Google");
	*/System.out.println("Inside verifyURL ");	
	
		}
	
	@AfterMethod()
	public void closeBrowser() {
//		driver.quit();
		System.out.println("Inside close browser");
	}

}
