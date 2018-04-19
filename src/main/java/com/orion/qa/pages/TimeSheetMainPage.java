package com.orion.qa.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TimeSheetMainPage {

	private static WebElement element = null;

	public static WebElement btn_NewTimeSheet(WebDriver driver) {
//		element = driver.findElement(By.xpath(".//*[@id='main-content']/section/div[1]/div[1]/div/fieldset/form/div/a[1]"));
		element = driver.findElement(By.xpath("//a[@class='btn btn-sm btn-default']"));
		return element;
	}
	
	public static WebElement btn_Logout(WebDriver driver) {
		element = driver.findElement(By.xpath("//a[contains(text(),'Logout')]"));
		return element;
	}
	
	public static WebElement lbl_LoginUserIcon(WebDriver driver) {
		element = driver.findElement(By.xpath("//i[@class='material-icons' and contains(text(), 'perm_identity')]"));
		return element;
	}


	public static WebElement grd_MonthlyData(WebDriver driver) {
		element = driver.findElement(By.xpath(".//*[@id='no-more-tables']/table/tbody"));
		return element;
	}

	public static int ReadMonthlyDatafromGridtoElement(WebDriver driver, char strStatus) {
		int i = 0;
		boolean RowFound = false;
		try {
			List<WebElement> Rows = grd_MonthlyData(driver).findElements(By.tagName("tr"));
			for (i = 0; i <= Rows.size() - 1; i++) {
				List<WebElement> Cols = Rows.get(i).findElements(By.tagName("td"));
				if (strStatus == 'D') {
					if (Cols.get(2).getText().equals("Draft")) {
						RowFound = true;
						break;
					}
				} else if (strStatus == 'A') {
					if (Cols.get(2).getText().equals("Approved")) {
						RowFound = true;
						break;
					}
				} else if (strStatus == 'S') {
					if (Cols.get(2).getText().equals("Submitted")) {
						RowFound = true;
						break;
					}
				} else if (strStatus == 'R') {
					if (Cols.get(2).getText().equals("Rejected")) {
						RowFound = true;
						break;
					}
				} else if (strStatus == 'P') {
					if (Cols.get(2).getText().equals("Pre-approved")) {
						RowFound = true;
						break;
					}
				}
			}
			if (RowFound) {
				return (i + 1);
			}
			else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static WebElement getGrdElement(WebDriver driver, int RowNum) {
		element = driver.findElement(By.xpath(".//*[@id='no-more-tables']/table/tbody/tr[" + RowNum + "]/td[1]/a"));
		return element;
	}
	
	public static WebElement grd_clickReportPeriodLink(WebDriver driver, String RptPeriod) {
		element =  grd_MonthlyData(driver).findElement(By.linkText(RptPeriod));
		return element;
	}

}
