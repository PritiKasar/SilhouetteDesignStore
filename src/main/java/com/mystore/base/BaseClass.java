package com.mystore.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.mystore.actiondriver.Action;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * BaseClass - Chrome only (Windows + Linux support)
 */
public class BaseClass {

    public static Properties prop;
    private static final Logger log = LogManager.getLogger(BaseClass.class);

    // Thread-safe WebDriver
    private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();


    public static WebDriver getDriver() {
        return driver.get();
    }


    @BeforeSuite
    public void initSuite() {
        log.info("===== Test Suite Execution Started =====");
    }


    @BeforeTest
    public void loadConfig() {
        try {
            if (prop == null) {
                prop = new Properties();
                FileInputStream ip = new FileInputStream(
                        System.getProperty("user.dir") + "/Configuration/Config.properties");
                prop.load(ip);
                log.info("Configuration loaded successfully");
            }
        } catch (IOException e) {
            log.error("Failed to load configuration file", e);
        }
    }


    /** Launch Chrome on Windows or Linux */
    public static void launchApp() {

        log.info("Starting Chrome browser...");

        // WebDriverManager auto-detects OS (Windows / Linux)
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Important – avoid automation errors on servers
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");

        // If running on server without GUI → headless mode
        if (isLinuxServer()) {
            log.info("Linux server detected → Running Chrome in headless mode");
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        driver.set(new ChromeDriver(options));

        getDriver().manage().window().maximize();
        Action.implicitWait(getDriver(), 5);
        Action.pageLoadTimeOut(getDriver(), 30);

        String url = prop.getProperty("url");
        log.info("Navigating to: " + url);
        getDriver().get(url);
    }


    // Detect Linux server environment
    private static boolean isLinuxServer() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }


    @AfterTest
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            log.info("Browser closed successfully.");
        }
    }
}
