package com.orion.qa.utils;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CommonMethods {

	private static WebElement element = null;
	public static String URL_TimeSheet;
	public static String Chrome_Browser_Location;
	public static String IE_Browser_Location;
	public static String Attachment_File_Download_Location;
	public static String Sample_FileNamewithPath;
	
	public static void ScrollScreenToElement(WebDriver driver, JavascriptExecutor jse, String strPath) {
		try {
			element = driver.findElement(By.xpath(strPath));
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void ScrollScreenToElementAndClick(WebDriver driver, JavascriptExecutor jse, String strPath) {
		try {
			element = driver.findElement(By.xpath(strPath));
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// These two elements are common to all the pages, hence kept it here
	public static WebElement btn_Logout(WebDriver driver) {
		element = driver.findElement(By.xpath("//a[contains(text(),'Logout')]"));
		return element;
	}

	public static WebElement lbl_LoginUserIcon(WebDriver driver) {
		element = driver.findElement(By.xpath("//i[@class='material-icons' and contains(text(), 'perm_identity')]"));
		return element;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean compareList(List ls1, List ls2) {
		return ls1.toString().contentEquals(ls2.toString()) ? true : false;
	}

	public static boolean CompareFilesbyByte(String file1, String file2) {
		try {
			System.out.println("file1 : " + file1);
			System.out.println("file2 : " + file2);
			File f1 = new File(file1);
			File f2 = new File(file2);
			System.out.println("file1 : " + f1.getAbsolutePath());
			System.out.println("file2 : " + f2.getAbsolutePath());

			System.out.println("file1 : " + f1.exists());
			System.out.println("file2 : " +f2.exists());
			FileInputStream fis1 = new FileInputStream(f1);
			FileInputStream fis2 = new FileInputStream(f2);
			Thread.sleep(2000);
			if (f1.length() == f2.length()) {
				int n = 0;
				byte[] b1;
				byte[] b2;
				while ((n = fis1.available()) > 0) {
					if (n > 80)
						n = 80;
					b1 = new byte[n];
					b2 = new byte[n];
					fis1.read(b1);
					fis2.read(b2);
					if (Arrays.equals(b1, b2) == false) {
						fis1.close();
						fis2.close();
						// f2.delete(); // deleting the downloaded file
						return false;
					}
				}
			} else {
				fis1.close();
				fis2.close();
			    // f2.delete(); // deleting the downloaded file
				return false;
			}
			fis1.close();
			fis2.close();
			// f2.delete(); // deleting the downloaded file
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@DataProvider(name = "credentials")
	public Object[][] readExcel_Credentials() throws BiffException, IOException {
		String strCurrentPath = System.getProperty("user.dir");
	//	File f = new File(strCurrentPath+"\\src\\main\\input\\inputdata.xls");
		File f = new File(strCurrentPath+"//src//main//input//inputdata.xls");
		// System.out.println("Inside readExcel_Credentials "+ f.getAbsolutePath());
		Workbook wb = Workbook.getWorkbook(f);
		Sheet sh = wb.getSheet("credentials");

		int rows = sh.getRows();
		int cols = sh.getColumns();

		String inputData[][] = new String[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell cel = sh.getCell(j, i);
				inputData[i][j] = cel.getContents();
			}
		}
		return inputData;
	}

	public static void readExcel_Paths() {
		try {
			String strCurrentPath = System.getProperty("user.dir");
			//File f = new File(strCurrentPath+"\\src\\main\\input\\inputdata.xls");
			File f = new File(strCurrentPath+"//src//main//input//inputdata.xls");
			// System.out.println("Inside ReadExcel_Paths");	
			// System.out.println("File Name : "+ f.getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(f);
			Sheet sh = wb.getSheet("paths");

			int rows = sh.getRows();
			int cols = sh.getColumns();

			String inputData[][] = new String[rows][cols];

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					Cell cel = sh.getCell(j, i);
					inputData[i][j] = cel.getContents();
				}
			}
			
			URL_TimeSheet = inputData[0][1].toString();
			Chrome_Browser_Location = inputData[1][1].toString();
			Attachment_File_Download_Location = strCurrentPath + inputData[2][1].toString();
			Sample_FileNamewithPath = strCurrentPath + inputData[3][1].toString();
			IE_Browser_Location = inputData[4][1].toString();
			
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String readTestData(String SheetName, String fieldName) {
		try {
			String strCurrentPath = System.getProperty("user.dir");
			//File f = new File(strCurrentPath+"\\src\\main\\input\\inputdata.xls");
			
			File f = new File(strCurrentPath+"//src//main//input//inputdata.xls");
			// System.out.println("Inside readTestData "+ f.getAbsolutePath());

			Workbook wb = Workbook.getWorkbook(f);
			Sheet sh = wb.getSheet(SheetName);

			int rows = sh.getRows();
			int cols = sh.getColumns();

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					Cell cel = sh.getCell(j, i);
					if (cel.getContents().equals(fieldName)) {
						Cell cel2 = sh.getCell(j+1, i);
						return cel2.getContents();
					}
				}
			}
			
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
