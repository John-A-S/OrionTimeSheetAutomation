package com.orion.qa.testcases;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.qa.utils.listener;

public class LinuxTest {

	public static WebDriver driver1;
	public static ChromeDriverService driverService;
	public static ChromeDriver driver;
	public static ChromeOptions options;
	public static String chromeDriverPath = System.getProperty("user.dir") + "//src//main//input//chromedriver";
	public static String chromeDownloadPath = System.getProperty("user.dir") + "/src/main/input/download/";
	public static EventFiringWebDriver eventDriver;

	@Test()
	public static void invokeBrowser() {
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		System.out.println("After Chrome driver path setting");
		options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		System.out.println("After Chrome Options");
		System.out.println(options.toString());
		driver1= new ChromeDriver(options);
		driver1.get("http://www.google.com");
		System.out.println("After opening google");
		driver1.findElement(By.name("q")).sendKeys("Hello");
		System.out.println("After entering Hello");
	}
	
	public static void ScrollScreenToElement(WebDriver driver, WebElement element) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setDownloadSettings() throws ClientProtocolException, IOException {

		System.out.println("Inside setDownloadSettings");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("behavior", "allow");
		params.put("downloadPath", chromeDownloadPath);

		Map<String, Object> commandParams = new HashMap<String, Object>();
		commandParams.put("cmd", "Page.setDownloadBehavior");
		commandParams.put("params", params);

		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();
		String command = objectMapper.writeValueAsString(commandParams);
		System.out.println("Command : " + command);

		String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
		System.out.println("u : " + u);
		HttpPost request = new HttpPost(u);
		request.addHeader("content-type", "application/zip");

		request.setEntity(new StringEntity(command));
		HttpResponse res = httpClient.execute(request);
		System.out.println(res.getStatusLine().getStatusCode());
	}

	@Test(enabled=false)
	public static void downloadfile() throws InterruptedException, ClientProtocolException, IOException {

		System.out.println("Inside downloadfile");

		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		options = new ChromeOptions();
		options.addArguments("--test-type");
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-extensions"); // to disable browser extension popup

		driverService = ChromeDriverService.createDefaultService();
		driver = new ChromeDriver(driverService, options);

		// setDownloadSettings();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("behavior", "allow");
		params.put("downloadPath", chromeDownloadPath);

		Map<String, Object> commandParams = new HashMap<String, Object>();
		commandParams.put("cmd", "Page.setDownloadBehavior");
		commandParams.put("params", params);

		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();
		String command = objectMapper.writeValueAsString(commandParams);
		System.out.println("Command : " + command);

		String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
		System.out.println("u : " + u);
		HttpPost request = new HttpPost(u);
		request.addHeader("content-type", "application/zip");

		request.setEntity(new StringEntity(command));
		HttpResponse res = httpClient.execute(request);
		System.out.println(res.getStatusLine().getStatusCode());

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		System.out.println("Before get" + driver.toString());

		eventDriver = new EventFiringWebDriver(driver);

		listener handler = new listener();
		eventDriver.register(handler);

		/*
		 * eventDriver.get(
		 * "http://toolsqa.wpengine.com/automation-practice-switch-windows/");
		 * WebElement element = eventDriver.findElement(By.id("target"));
		 * element.click();
		 */
		 DownloadDocfromOrion();
		// DownloadDocfromExternal

		// Event_DownloadDocfromOrion();
		// Event_DownloadDocfromExternal();
	}

	public static void Event_DownloadDocfromOrion() throws InterruptedException, ClientProtocolException, IOException {

		eventDriver.get("http://192.168.1.226/orion-web/app/");
		Thread.sleep(2000);
		eventDriver.findElement(By.xpath("//input[@placeholder='User ID']")).sendKeys("John");
		eventDriver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("infomatics@123");
		eventDriver.findElement(By.xpath("//button[text()='Login']")).click();
		System.out.println("After login button click");
		Thread.sleep(3000);

		eventDriver.findElement(By.linkText("05/06/2018 - 05/12/2018")).click();
		System.out.println("After linkText -> 05/06/2018 - 05/12/2018 click");

		Thread.sleep(2000);

		System.out.println(eventDriver.findElement(By.xpath("//h3")).getText());

		Thread.sleep(2000);

		ScrollScreenToElement(eventDriver, eventDriver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")));
		System.out.println("After ScrollScreenToElement ");

		System.out.println("isDownload document link displayed: "
				+ eventDriver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")).isDisplayed());

		/*
		 * WebElement ele =
		 * eventDriver.findElement(By.xpath("//a[contains(text(), 'john.docx')]"));
		 * JavascriptExecutor executor = (JavascriptExecutor)eventDriver;
		 * executor.executeScript("arguments[0].click();", ele);
		 * 
		 * Actions act = new Actions(eventDriver); WebElement ele =
		 * eventDriver.findElement(By.xpath("//a[contains(text(), 'john.docx')]"));
		 * act.moveToElement(ele).click().build().perform();
		 */

		Thread.sleep(3000);
			
		/*	WebElement element_p = (new WebDriverWait(eventDriver, 20))
	            .until(ExpectedConditions.elementToBeClickable(By
	                    .xpath("//a[contains(text(), 'john.docx')]")));
			element_p.click();
		 */	
		new WebDriverWait(eventDriver, 20)
		.until(ExpectedConditions.elementToBeClickable(By
				.xpath("//a[@ng-click='download(attachment.attachmentId,attachment.fileName)']"))).click();
	
		//	eventDriver.findElement(By.xpath("//a[@ng-click='download(attachment.attachmentId,attachment.fileName)']")).click();

		eventDriver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")).click();
		
		/*		The below code is downloading the file in LINUX environment as well. 
		 * 		String fromfile = "http://192.168.1.226/orion-web/common/download?fileId=48160946-e56b-4be9-8141-e6e28b1d90b8&fileName=john.docx";
				FileUtils.copyURLToFile(
				new URL(fromfile),	new File(chromeDownloadPath + "john.docx"), 10000, 10000);
		 */

		Thread.sleep(50000);
		System.out.println("After download link click");

		File f = new File(chromeDownloadPath + "john.docx");

		if (f.exists()) {
			System.out.println("Successfully downloaded");
		}
	}

	public static void Event_DownloadDocfromExternal()
			throws InterruptedException, ClientProtocolException, IOException {

		eventDriver.get("http://www.cvtemplatemaster.com");
		Thread.sleep(2000);
		System.out.println("Got it :" + driver.findElement(By.linkText("Got it!")).isDisplayed());
		eventDriver.findElement(By.linkText("Got it!")).click();
		System.out.println("Got it : After click");
		Thread.sleep(2000);

		Actions act = new Actions(eventDriver);
		System.out.println("Before action.movetoelement");
		act.moveToElement(eventDriver.findElement(By.xpath("//a[contains(text(), 'CV templates')]"))).perform();
		System.out.println("After action.movetoelement");
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'CV templates')]")));
		eventDriver.findElement(By.xpath("//a[contains(text(), 'CV templates')]")).click();
		System.out.println("After CV templates click");

		Thread.sleep(2000);
		ScrollScreenToElement(eventDriver, eventDriver.findElement(By.id("subbutton")));
		Thread.sleep(2000);
		eventDriver.findElement(By.id("subbutton")).click();
		System.out.println("After subbuton click");

		Thread.sleep(2000);
		ScrollScreenToElement(eventDriver, eventDriver.findElement(By.linkText("Free download")));

		System.out.println("href "
				+ eventDriver.findElement(By.xpath("//a[contains(text(), 'Free download')]")).getAttribute("href"));

		Thread.sleep(3000);
		eventDriver.findElement(By.linkText("Free download")).click();
		Thread.sleep(2000);

		System.out.println("File to look for : " + chromeDownloadPath + "CV_Template_A4_Prof.docx");
		File f = new File(chromeDownloadPath + "CV_Template_A4_Prof.docx");

		if (f.exists()) {
			System.out.println("Successfully downloaded");
		}
	}

	public static void DownloadDocfromOrion() throws InterruptedException, ClientProtocolException, IOException {
		
		System.out.println("Inside DownloadDocfromOrion");
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

		Actions act = new Actions(driver);
		WebElement ele = driver.findElement(By.xpath("//a[contains(text(), 'john.docx')]"));
		act.moveToElement(ele).click().build().perform();

		Thread.sleep(5000);
		
		WebElement element_p = (new WebDriverWait(driver, 20))
				.until(ExpectedConditions.elementToBeClickable(By
                .xpath("//a[contains(text(), 'john.docx')]")));
		element_p.click();
		
		WebElement elem1 = driver.findElement(By.cssSelector("a[ng-click='download(attachment.attachmentId,attachment.fileName)']"));
		elem1.sendKeys(Keys.RETURN);
		//driver.findElement(By.xpath("//a[contains(text(), 'john.docx')]")).click();

		Thread.sleep(2000);
		System.out.println("After download link click");

		File f = new File(chromeDownloadPath + "john.docx");

		if (f.exists()) {
			System.out.println("Successfully downloaded");
		}
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

		Actions act1 = new Actions(driver);
		WebElement ele = driver.findElement(By.xpath("//a[contains(text(), 'Free download')]"));
		act1.moveToElement(ele).click().build().perform();

		// driver.findElement(By.linkText("Free download")).click();
		Thread.sleep(2000);

		System.out.println("File to look for : " + chromeDownloadPath + "CV_Template_A4_Prof.docx");
		File f = new File(chromeDownloadPath + "CV_Template_A4_Prof.docx");

		if (f.exists()) {
			System.out.println("Successfully downloaded");
		}

	}

	/*
	 * @BeforeMethod() public void init() {
	 * 
	 * CommonMethods.readExcel_Paths(); }
	 * 
	 * @Test(enabled=false) public void downloadfile() throws
	 * ClientProtocolException, IOException, InterruptedException {
	 * 
	 * System.setProperty("webdriver.chrome.driver", chromeDriverPath);
	 * 
	 * options = new ChromeOptions(); options.addArguments("--test-type");
	 * options.addArguments("--headless"); options.addArguments("--no-sandbox");
	 * options.addArguments("--disable-extensions"); // to disable browser extension
	 * popup
	 * 
	 * driverService = ChromeDriverService.createDefaultService(); driver = new
	 * ChromeDriver(driverService, options);
	 * 
	 * System.out.println("Before get" + driver.toString());
	 * 
	 * setDownloadSettings(""); driver.get("http://www.seleniumhq.org/download/");
	 * System.out.println("After get" + driver.toString());
	 * driver.findElement(By.linkText("32 bit Windows IE")).click();
	 * System.out.println("After linkText " + driver.toString());
	 * 
	 * driver.get("http://192.168.1.226:8080/orion-web/app/"); Thread.sleep(2000);
	 * driver.findElement(By.xpath("//input[@placeholder='User ID']")).sendKeys(
	 * "John");
	 * driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(
	 * "infomatics@123");
	 * driver.findElement(By.xpath("//button[text()='Login']")).click();
	 * 
	 * Thread.sleep(3000);
	 * 
	 * driver.findElement(By.linkText("04/29/2018 - 05/05/2018")).click();
	 * 
	 * Thread.sleep(2000);
	 * 
	 * System.out.println(driver.findElement(By.xpath("//h3")).getText());
	 * 
	 * Thread.sleep(2000);
	 * 
	 * ScrollScreenToElement(driver, driver.findElement(By.
	 * xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]"))
	 * );
	 * 
	 * 
	 * setDownloadSettings("test.docx");
	 * driver.get("https://www2.le.ac.uk/Members/davidwickins/old/test.docx/view");
	 * System.out.println("After get" + driver.toString());
	 * driver.findElement(By.xpath("//*[@id='content-core']/p/span/a")).click();
	 * System.out.println("After linkText " + driver.toString());
	 * 
	 * 
	 * 
	 * setDownloadSettings("John Joseph_04/29/2018 - 05/05/2018_0.docx");
	 * //setDownloadSettings("John Joseph_04_29_2018 - 05_05_2018_0.docx", true);
	 * System.out.println("Title : " + driver.getTitle()+
	 * " Current URL : "+driver.getCurrentUrl());
	 * 
	 * //driver.findElement(By.
	 * xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]"))
	 * .click();
	 * driver.findElement(By.linkText("John Joseph_04/29/2018 - 05/05/2018_0.docx"))
	 * .click();
	 * 
	 * Thread.sleep(10000);
	 * 
	 * System.out.println("After Download"); }
	 * 
	 * @Test(enabled=false) public void callChromeDownload() throws IOException,
	 * InterruptedException {
	 * chromeDownload("http://192.168.1.226:8080/orion-web/app/", 2,
	 * chromeDownloadPath); }
	 * 
	 * public static void chromeDownload(String address, int Headless, String
	 * DownDir) throws IOException, InterruptedException{
	 * 
	 * if (ValidateOS.isWindows()){ System.out.println("This is a Windows system.");
	 * System.setProperty("webdriver.chrome.driver",
	 * "resources\\driver\\chromedriver.exe"); //options.
	 * setBinary("C:\\Users\\Juri\\AppData\\Local\\Google\\Chrome SxS\\Application\\chrome.exe"
	 * ); // If this is commented, the grabber will use the main Chrome } else if
	 * (ValidateOS.isUnix()){ System.out.println("This is a Unix system.");
	 * //System.setProperty("webdriver.chrome.driver",
	 * "resources/driver/chromedriver");
	 * System.setProperty("webdriver.chrome.driver", chromeDriverPath);
	 * options.setBinary("/usr/bin/google-chrome"); }
	 * 
	 * 
	 * String Filename = DownDir + "John Joseph_04_29_2018 - 05_05_2018_0.docx";
	 * 
	 * System.out.println("FileName : "+ Filename); String downloadFilepath =
	 * DownDir;
	 * 
	 * System.out.println("Download File Path : "+ chromeDownloadPath);
	 * System.out.println("webdriver.chrome.driver : "+ chromeDriverPath);
	 * System.out.println("Address : " + address);
	 * System.setProperty("webdriver.chrome.driver", chromeDriverPath);
	 * 
	 * options = new ChromeOptions();
	 * 
	 * switch (Headless){ case 1:{ options.addArguments("--headless");
	 * options.addArguments("--no-sandbox"); break;} case 2: default:
	 * options.addArguments("--window-size=1152,768"); break; }
	 * options.addArguments("--test-type");
	 * options.addArguments("--disable-extensions");
	 * 
	 * driverService = ChromeDriverService.createDefaultService(); driver = new
	 * ChromeDriver(driverService, options);
	 * 
	 * Map<String, Object> commandParams = new HashMap<String, Object>();
	 * commandParams.put("cmd", "Page.setDownloadBehavior"); Map<String, String>
	 * params = new HashMap<String, String>(); params.put("behavior", "allow");
	 * params.put("downloadPath", downloadFilepath); params.put("cmd",
	 * "Page.setDownloadBehavior");
	 * 
	 * commandParams.put("params", params); ObjectMapper objectMapper = new
	 * ObjectMapper(); CloseableHttpClient httpClient =
	 * HttpClientBuilder.create().build(); String command =
	 * objectMapper.writeValueAsString(commandParams); String u =
	 * driverService.getUrl().toString() + "/session/" + driver.getSessionId() +
	 * "/chromium/send_command"; HttpPost request = new HttpPost(u);
	 * request.addHeader("content-type",
	 * "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	 * request.addHeader("Content-Disposition", "attachment; filename=\"" + Filename
	 * + "\""); System.out.println("attachment; filename=\"" + Filename + "\"");
	 * request.setEntity(new StringEntity(command)); httpClient.execute(request);
	 * 
	 * driver.get(address);
	 * 
	 * Thread.sleep(2000);
	 * driver.findElement(By.xpath("//input[@placeholder='User ID']")).sendKeys(
	 * "John");
	 * driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(
	 * "infomatics@123");
	 * driver.findElement(By.xpath("//button[text()='Login']")).click();
	 * 
	 * System.out.println("After login button click"); Thread.sleep(3000);
	 * 
	 * driver.findElement(By.linkText("04/29/2018 - 05/05/2018")).click();
	 * System.out.println("After linkText(\"04/29/2018 - 05/05/2018\") click");
	 * 
	 * Thread.sleep(2000);
	 * 
	 * ScrollScreenToElement(driver, driver.findElement(By.
	 * xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]"))
	 * ); System.out.println("After ScrollScreenToElement click"); // John
	 * Joseph_04/29/2018 - 05/05/2018_0.docx;;
	 * John%20Joseph_04_29_2018%20-%2005_05_2018_0.docx driver.findElement(By.
	 * xpath("//a[contains(text(), 'John Joseph_04/29/2018 - 05/05/2018_0.docx')]"))
	 * .click(); System.out.println("After final click");
	 * //driver.findElement(By.id("download")).click(); driver.quit(); }
	 */

}
