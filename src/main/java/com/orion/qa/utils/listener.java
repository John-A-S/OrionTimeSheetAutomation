package com.orion.qa.utils;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class listener implements WebDriverEventListener{

	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeAlertAccept");
	}

	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterAlertAccept");
		
	}

	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterAlertDismiss");
		
	}

	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeAlertDismiss");
		
	}

	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeNavigateTo");
	}

	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterNavigateTo");
		
	}

	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeNavigateBack");
		
	}

	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterNavigateBack");
		
	}

	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeNavigateForward");
		
	}

	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterNavigateForward");
		
	}

	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeNavigateRefresh");
		
	}

	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterNavigateRefresh");
		
	}

	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeFindBy");
		
	}

	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterFindBy");
		
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeClickOn");
		
	}

	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterClickOn");
		System.out.println(element.toString());
		
	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		System.out.println("beforeChangeValueOf");
		
	}

	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		System.out.println("afterChangeValueOf");
		
	}

	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeScript");
		
	}

	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterScript");
		
	}

	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeSwitchToWindow");
		
	}

	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterSwitchToWindow");
		
	}

	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("onException ");
		
	}

}
