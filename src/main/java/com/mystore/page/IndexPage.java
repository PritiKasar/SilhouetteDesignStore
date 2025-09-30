package com.mystore.page;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.mystore.actiondriver.Action;
import com.mystore.base.BaseClass;

public class IndexPage extends BaseClass {

    @FindBy(xpath = "//img[@alt=' the design store logo']")
    private WebElement logo;

    @FindBy(css = "a.user-link-dropdown.lm")
    private WebElement signInRegisterHover;

    @FindBy(css = "a.signin_link")
    private WebElement signInButton;

    // Popup containers
    private By loginPopupLocator = By.cssSelector("form.popup-login-form, div#login-popup, .login-popup-class");
    private String loginPageUrlFragment = "/customer/account/login";
    private By registerPopupLocator = By.cssSelector("form.popup-register-form, div#register-popup, .register-popup-class");
    private String registerPageUrlFragment = "/customer/account/create";


    @FindBy(css = "a[title='Register']")
    private WebElement registerButton;

    @FindBy(xpath = "//span[text()='Sign in / Register']")
    private WebElement signInRegisterLink;

    WebDriver driver;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean verifyLogo() {
        boolean isLogoDisplayed = logo.isDisplayed();
        System.out.println(isLogoDisplayed ? "✅ Logo is displayed" : "❌ Logo is NOT displayed");
        return isLogoDisplayed;
    }

    public String verifyTitle() {
        return driver.getTitle();
    }
    
    
    public LoginAble clickAndCheckLogin() {
        try {
            // Step 1: Hover
            Actions actions = new Actions(driver);
            actions.moveToElement(signInRegisterHover).pause(Duration.ofMillis(1000)).perform();

            // Step 2: Click with JS fallback
            try {
            	signInButton.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signInButton);
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

            // Step 3: Check popup first
            try {
                WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPopupLocator));
                if (popup.isDisplayed()) {
                    log("✅ Login popup detected.");
                    return new LoginPopUp();
                }
            } catch (Exception ignored) {
                // Popup not found within timeout
            }

            // Step 4: Check redirect page
            try {
                boolean redirected = wait.until(ExpectedConditions.urlContains(loginPageUrlFragment));
                if (redirected) {
                    log("✅ Redirected to login page.");
                    return new LoginPage();
                }
            } catch (Exception ignored) {
                // No redirect
            }

            log("❌ Unknown state after login attempt.");
            throw new IllegalStateException("Neither popup nor login page detected.");

        } catch (Exception e) {
            log("❌ Error during login attempt: " + e.getMessage());
            throw new RuntimeException("Login flow failed", e);
        }
    }

    /** Helper method */
    private boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Replace with logger if available */
    private void log(String msg) {
        System.out.println(msg);
    }


    public Registration clickAndCheckRegisterPopup() {
    	 try {
    	        // Step 1: Hover over Sign In / Register
    	        Actions actions = new Actions(driver);
    	        actions.moveToElement(signInRegisterHover).pause(Duration.ofMillis(500)).perform();

    	        // Step 2: Click the Register button (with JS fallback)
    	        try {
    	            registerButton.click();
    	        } catch (Exception e) {
    	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", registerButton);
    	        }

    	        // Step 3: Wait for either popup or redirect
    	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    	        wait.until(ExpectedConditions.or(
    	                ExpectedConditions.visibilityOfElementLocated(registerPopupLocator),
    	                ExpectedConditions.urlContains(registerPageUrlFragment)
    	        ));

    	        // Step 4: Decide which one appeared
    	        if (isElementVisible(registerPopupLocator)) {
    	            log("✅ Register popup detected.");
    	            return new AccountCreationPopup(driver);
    	        } else if (driver.getCurrentUrl().contains(registerPageUrlFragment)) {
    	            log("✅ Redirected to registration page.");
    	            return new AccountCreationPage(driver);
    	        }

    	        log("❌ Unknown state after registration attempt.");
    	        throw new IllegalStateException("Neither popup nor registration page detected.");

    	    } catch (Exception e) {
    	        log("❌ Error during registration attempt: " + e.getMessage());
    	        throw new RuntimeException("Register flow failed", e);
    	    }
    	}
    
    public List<String> getBrokenLinks() {
        List<String> brokenLinks = new ArrayList<>();

        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("🔗 Total links found: " + links.size());

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url == null || url.isEmpty()) {
                continue;
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode >= 400) {
                    brokenLinks.add(url + " → " + responseCode);
                    System.out.println("❌ Broken link: " + url + " → " + responseCode);
                } else {
                    System.out.println("✅ Valid link: " + url);
                }
            } catch (Exception e) {
                brokenLinks.add(url + " → Exception: " + e.getMessage());
                System.out.println("⚠️ Error checking link: " + url + " → " + e.getMessage());
            }
        }
        return brokenLinks;
    }}
