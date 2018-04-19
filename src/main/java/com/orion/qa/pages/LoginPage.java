package com.orion.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
	private static WebElement element = null;

	public static WebElement txtbx_UserName(WebDriver driver) {
		element = driver.findElement(By.xpath("//input[@placeholder='User ID']"));
		return element;
	}

	public static WebElement txtbx_Password(WebDriver driver) {
		element = driver.findElement(By.xpath("//input[@placeholder='Password']"));
		return element;
	}

	public static WebElement btnLogin(WebDriver driver) {
		element = driver.findElement(By.xpath("//button[text()='Login']"));
		return element;
	}

}
