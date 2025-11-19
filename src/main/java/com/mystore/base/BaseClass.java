package com.mystore.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.mystore.actiondriver.Action;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * BaseClass
 * ----------
 * Handles:
 *  - WebDriver initialization
 *  - Config loading
 *  - Thread-safe driver (ThreadLocal)
 */
public class BaseClass {

    public static Properties prop;
    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    /** Thread-safe WebDriver getter */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /** Configure Log4j before entire suite */
    @BeforeSuite
    public void beforeSuite() {
        DOMConfigurator.configure("log4j.xml");
    }

    /** Load Config properties only once */
    @BeforeTest
    public void loadConfig() {
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

    /** Launch Browser + Open Application URL */
    public static void launchApp() {

        String browserName = prop.getProperty("browser").trim().toLowerCase();

        switch (browserName) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                break;

            case "ie":
                WebDriverManager.iedriver().setup();
                driver.set(new InternetExplorerDriver());
                break;

            default:
                throw new RuntimeException("❌ Unsupported Browser in Config: " + browserName);
        }

        getDriver().manage().window().maximize();
        Action.implicitWait(getDriver(), 5);
        Action.pageLoadTimeOut(getDriver(), 30);
        getDriver().get(prop.getProperty("url"));
    }

    /** Close browser after all tests */
    @AfterTest
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            System.out.println("✅ Browser closed after all tests");
        }
    }
}
