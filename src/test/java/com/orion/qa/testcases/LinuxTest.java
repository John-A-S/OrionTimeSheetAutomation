package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LinuxTest {

	public static String chromeDriverPath = System.getProperty("user.dir")+"/src/main/input/chromedriver.exe";
	//Below is for linux
	//public static String chromeDriverPath = "/usr/bin/chromedriver";
	public WebDriver driver;
	
	@BeforeMethod()
	public void init() {
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		driver = new ChromeDriver();
		driver.get("http://www.google.com");
	}

	@Test()
	public void verifyURL(){
		String title = driver.getTitle();
		System.out.println(title);
		assertEquals(title, "Google");
	}
	
	@AfterMethod()
	public void closeBrowser() {
		driver.quit();
	}

}
