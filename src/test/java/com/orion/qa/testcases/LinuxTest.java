package com.orion.qa.testcases;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LinuxTest {

	public WebDriver driver;

	public static String chromeDriverPath = System.getProperty("user.dir") + "/src/main/input/chromedriver";

	@BeforeMethod()
	public void init() {
		/* Chrome driver */
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		ChromeOptions options = new ChromeOptions(); 
		
//		 options.addArguments("--disable-extensions");
		 options.addArguments("--headless");
		 options.addArguments("--no-sandbox");

		 //	options.addArguments("--disable-gpu");
		 //	options.setBinary("/usr/bin/chromium");

		driver = new ChromeDriver(options);
		driver.get("http://www.google.com");
	}

	@Test()
	public void verifyURL() {
		String title = driver.getTitle();
		System.out.println(title);
		assertEquals(title, "Google");
	}

	@AfterMethod()
	public void closeBrowser() {
		driver.quit();
	}

}
