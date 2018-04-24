package com.orion.qa.pages;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orion.qa.utils.CommonMethods;

public class TimeSheetEditPage {
	private static WebElement element = null;
	private static ArrayList<String> objTempGridData;;

	public static WebElement lbl_TimeSheet(WebDriver driver) {
		return(driver.findElement(By.tagName("h3")));
	}
	
	public static WebElement lbl_ReportDate(WebDriver driver) {
		return(driver.findElement(By.id("reportperiod")));
	}
	
	public static WebElement wait_btn_AddAttachclickable(WebDriver driver, WebDriverWait wait) {
		return(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add Attachment"))));
	}
	
	public static WebElement AddAttachclickable(WebDriver driver) {
		return(driver.findElement(By.linkText("Add Attachment")));
	}
	
	public static WebElement grd_clickReportPeriodLink(WebDriver driver, String RptPeriod) {
		return(driver.findElement(By.linkText(RptPeriod)));
	}

	public static WebElement grd_AttachmentData(WebDriver driver) {
		return(driver.findElement(By.xpath("//table[@class='table table-bordered table-hover']/tbody")));
	}

	public static WebElement wait_grd_AddAttachclickable(WebDriver driver, int RowValue) {
		element = driver.findElement(By.xpath(
		//		"//*[@id='timeSheet_save_form']/div/div/div/div[4]/div/table/tbody/tr[" + RowValue + "]/td[1]/input"));
				"//table[@class='table table-bordered table-hover']/tbody/tr[" + RowValue + "]/td[1]/input"));
		return element;
	}

	public static WebElement Wait_Msg_TimeSheetSave(WebDriver driver, WebDriverWait wait) {
		return(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='bootbox-body']"))));
	}

	public static WebElement Msg_TimeSheetSave(WebDriver driver) {
		return(driver.findElement(By.xpath("//div[@class='bootbox-body']")));
	}

	public static WebElement Wait_Msg_TimeSheetSave_OK(WebDriver driver, WebDriverWait wait) {
		return(wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-bb-handler='ok']"))));
	}
	
	public static WebElement Msg_TimeSheetSave_OK(WebDriver driver) {
		return(driver.findElement(By.xpath("//button[@data-bb-handler='ok']")));
	}
	
	public static WebElement Wait_Msg_TimeSheetSubmit_OK(WebDriver driver, WebDriverWait wait) {
		return(wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-bb-handler='confirm']"))));
	}
	
	public static WebElement grd_ColSunday(WebDriver driver) {
		return(driver.findElement(By.xpath("//td[@data-title='sun']//input")));
				//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input"));
	}

	public static WebElement grd_ColMonday(WebDriver driver) {
		return(driver.findElement(By.xpath("//td[@data-title='mon']//input")));
				//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[5]/input"));
	}

	public static WebElement grd_txtComment(WebDriver driver) {
		return(driver.findElement(By.xpath("//textarea[@id='comment']")));
	}

	public static void grd_AttachmentDataExist(WebDriver driver) {
		driver.findElement(
				//By.xpath("//*[@id='timeSheet_save_form']/div/div/div/div[4]/div/table/tbody/tr/td[1]/input"));
				By.xpath("//table[@class='table table-bordered table-hover']/tbody/tr/td[1]/input"));
	}

	public static WebElement btn_Save(WebDriver driver) {
		return(driver.findElement(By.xpath("//button[text()='Save']")));
	}

	public static WebElement btn_Submit(WebDriver driver) {
		return(driver.findElement(By.xpath("//button[contains(text(), 'Submit')]")));
	}

	public static WebElement btn_Cancel(WebDriver driver) {
		return(driver.findElement(By.xpath("//button[text()='Cancel']")));
	}

	public static void ScrollScreenToElement(WebDriver driver, JavascriptExecutor jse, WebElement element) {
		try {
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void ScrollScreenToSaveButtonAndClick(WebDriver driver, JavascriptExecutor jse) {
		try {
			//element = driver.findElement(By.xpath(
			//		".//*[@ng-controller='newTimeSheetCtrl']/section[@id='main-content']/section/div/div/div/div/div/div/div[2]/button[1]"));
			//element = driver.findElement(By.xpath("//button[text()='Save']"));
			jse.executeScript("arguments[0].scrollIntoView(true);", btn_Save(driver));
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void ScrollScreenToSubmitButtonAndClick(WebDriver driver, JavascriptExecutor jse) {
		try {
			//element = driver.findElement(By.xpath(
			//		".//*[@ng-controller='newTimeSheetCtrl']/section[@id='main-content']/section/div/div/div/div/div/div/div[2]/button[1]"));
			// element = driver.findElement(By.xpath("//button[contains(text(),'Submit')]"));
			element = btn_Submit(driver);
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void ScrollScreenToCancelButtonAndClick(WebDriver driver, JavascriptExecutor jse) {
		try {
//			element = driver.findElement(By.xpath(".//*[@ng-controller='newTimeSheetCtrl']/section[@id='main-content']/section/div/div/div/div/div/div/div[2]/button[2]"));
		    element = btn_Cancel(driver);
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	public static void ScrollScreenToCancelButtonAndClick_Draft(WebDriver driver, JavascriptExecutor jse) {
		try {
			element = driver.findElement(By.xpath("//button[text()='Cancel']"));
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void ScrollScreenToCancelButtonAndClick_Approve(WebDriver driver, JavascriptExecutor jse) {
		try {
		//	element = driver.findElement(By.xpath(".//*[@ng-controller='newTimeSheetCtrl']/section[@id='main-content']/section/div/div/div/div/div/div/div[2]/button"));
			element = driver.findElement(By.xpath("//button[text()='Cancel']"));
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	public static void ScrollToSUBMITSAVECANCEL(WebDriver driver, JavascriptExecutor jse) {
		try {
			element = driver.findElement(By.xpath("//button[contains(text(),'Cancel')]"));
					//".//*[@ng-controller='newTimeSheetCtrl']/section[@id='main-content']/section/div/div/div/div/div/div/div[2]/button"));
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean verifySaveButtonExists(WebDriver driver) {
		try {
			try {
				driver.findElement(By.xpath("//button[contains(text(), 'Save')]"));
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifySubmitButtonExists(WebDriver driver) {
		try {
			try {
				driver.findElement(By.xpath("//button[contains(text(), 'Submit')]"));
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyCancelButtonExists(WebDriver driver) {
		try {
			try {
				driver.findElement(By.xpath("//button[contains(text(), 'Cancel')]"));
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ArrayList<String> ReadWeeklyDatafromGridtoElement(WebDriver driver, WebDriverWait wait,
			JavascriptExecutor jse) {
		String TempStr;
		try {
			objTempGridData = new ArrayList<String>();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//td[@data-title='client']/select")));
					//.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[2]/select")));
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[2]/select"))
							"//td[@data-title='client']/select"))
					.getText();
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
			//		By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input")));
					By.xpath("//td[@data-title='sun']/input")));
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[4]/input"))
							"//td[@data-title='sun']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[5]/input")));
					By.xpath("//td[@data-title='mon']/input")));
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[5]/input"))
							"//td[@data-title='mon']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[6]/input")));
					By.xpath("//td[@data-title='tue']/input")));
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[6]/input"))
							"//td[@data-title='tue']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[7]/input")));
					By.xpath("//td[@data-title='wed']/input")));

			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[7]/input"))
							"//td[@data-title='wed']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[8]/input")));
					By.xpath("//td[@data-title='thu']/input")));
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[8]/input"))
							"//td[@data-title='thu']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					//By.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[9]/input")));
					By.xpath("//td[@data-title='fri']/input")));
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[9]/input"))
							"//td[@data-title='fri']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					//.xpath(".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[10]/input")));
					By.xpath("//td[@data-title='sat']/input")));
					
			TempStr = driver
					.findElement(By.xpath(
							//".//*[@id='timeSheet_save_form']/div/div/div/div[3]/div/div/table/tbody/tr/td[10]/input"))
							"//td[@data-title='sat']/input"))
					.getAttribute("value");
			objTempGridData.add(TempStr);

			CommonMethods.ScrollScreenToElement(driver, jse, ".//*[@id='comment']");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='comment']")));
			TempStr = driver.findElement(By.xpath(".//*[@id='comment']")).getAttribute("value");

			objTempGridData.add(TempStr.trim());

			return objTempGridData;
		} catch (Exception e) {
			e.printStackTrace();
			return objTempGridData;
		}
	}
}
