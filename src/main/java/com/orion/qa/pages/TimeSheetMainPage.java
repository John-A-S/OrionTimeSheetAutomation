package com.orion.qa.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TimeSheetMainPage {

	public static WebElement btn_NewTimeSheet(WebDriver driver) {
		return(driver.findElement(By.xpath("//a[@class='btn btn-sm btn-default']")));
	}
	
	public static WebElement grd_MonthlyData(WebDriver driver) {
		return(driver.findElement(By.xpath(".//*[@id='no-more-tables']/table/tbody")));
	}
	
	public static WebElement lbl_TimeSheet(WebDriver driver) {
		return (driver.findElement(By.tagName("h3")));
	}
	
	public static WebElement lbl_ListTimeSheet(WebDriver driver) {
		return (driver.findElement(By.tagName("h4")));
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
		return(driver.findElement(By.xpath(".//*[@id='no-more-tables']/table/tbody/tr[" + RowNum + "]/td[1]/a")));
	}
	
	public static WebElement grd_clickReportPeriodLink(WebDriver driver, String RptPeriod) {
		// return(grd_MonthlyData(driver).findElement(By.linkText(RptPeriod)));
		return(driver.findElement(By.linkText(RptPeriod)));
	}

}
