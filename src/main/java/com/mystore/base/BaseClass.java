package com.mystore.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import com.mystore.actiondriver.Action;

import io.github.bonigarcia.wdm.WebDriverManager;
/**
 * BaseClass
 * -----------
 * Handles WebDriver initialization, configuration loading, and provides 
 * thread-safe driver access for parallel execution.
 */
public class BaseClass {

	public static Properties prop;
	//public static WebDriver driver;
public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
/** Provides thread-safe access to driver instance */


public static WebDriver getDriver() {
	return driver.get();}
/** Loads configuration from Config.properties */

/** Load config once before tests */
public BaseClass() {
    try {
        if (prop == null) {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir") + "/Configuration/Config.properties");
            prop.load(ip);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    /** Launches browser based on config and opens application URL */

	public static void launchApp() {
		WebDriverManager.chromedriver().setup();
		String browserName = prop.getProperty("browser");
		
		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			//driver = new ChromeDriver();
			driver.set(new ChromeDriver());
		} else if (browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());
		} else if (browserName.equalsIgnoreCase("IE")) {
			WebDriverManager.iedriver().setup();
			//driver = new InternetExplorerDriver();
			driver.set(new InternetExplorerDriver());
		}
		getDriver().manage().window().maximize();
		Action.implicitWait(getDriver(), 5);
		Action.pageLoadTimeOut(getDriver(), 30);
		getDriver().get(prop.getProperty("url"));
	}
	
	 
@AfterTest
public void tearDown() {
    if (getDriver() != null) {
    	getDriver().quit();
        System.out.println("✅ Browser closed after all tests");
    }
}}