package com.orion.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

	public static WebElement txtbx_UserName(WebDriver driver) {
		return (driver.findElement(By.xpath("//input[@placeholder='User ID']")));
	}

	public static WebElement txtbx_Password(WebDriver driver) {
		return (driver.findElement(By.xpath("//input[@placeholder='Password']")));
	}

	public static WebElement btnLogin(WebDriver driver) {
		return (driver.findElement(By.xpath("//button[text()='Login']")));
	}

}
