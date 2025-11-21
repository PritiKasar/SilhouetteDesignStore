package com.mystore.testcases;

import com.mystore.base.BaseClass;
import com.mystore.page.HomePage;
import com.mystore.page.Registration;
import com.mystore.utility.ScreenshotUtil;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Listeners(listeners.ExtentTestListener.class)
public class RegistrationTest extends BaseClass {

    private static final Logger log = LogManager.getLogger(RegistrationTest.class);

    private HomePage homepage;
    private Registration register;

    @BeforeMethod(alwaysRun = true)
    public void setup() throws Throwable {
        log.info("🚀 Launching Application...");
        launchApp();

        homepage = new HomePage(getDriver());

        log.info("🧾 Opening Registration popup...");
        register = homepage.clickAndCheckRegisterPopup();

        if (register == null) {
            log.error("❌ Registration popup not opened.");
            Assert.fail("Registration popup/page not opened.");
        }
    }

    @Test(description = "Registration : Verify user registration with valid data", priority = 1)
    public void verifyValidRegistration() {
        String firstName = "John";
        String lastName = "Doe";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String email = "user" + timestamp + "@test.com";
        String password = "Test@1234";
        String expectedMessage = "✔ Registration successful";

        try {
            log.info("🧩 Running: Valid Registration Test");
            enterRegistrationDetails(firstName, lastName, email, password);
            clickRegisterButton();

            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(register.getSuccessMessageElement()));

            Assert.assertTrue(register.isSuccessMessageDisplayed(), "✅ Success message not displayed!");
            log.info("✅ Success message: {}", register.getSuccessMessageText());

        } catch (Exception e) {
            captureFailure("ValidRegistration", e);
        }
    }

    @Test(description = "Registration : Verify registration with already registered email", priority = 2)
    public void verifyExistingEmailRegistration() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "existingemail@test.com"; // Already registered email
        String password = "Test@1234";
        String expectedMessage = "❌ Email already exists";

        try {
            log.info("🧩 Running: Existing Email Registration Test");
            enterRegistrationDetails(firstName, lastName, email, password);
            clickRegisterButton();

            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(register.getErrorElement()));

            Assert.assertTrue(register.isErrorDisplayed(), "❌ Error message not displayed!");
            log.info("❌ Error message: {}", register.getErrorMessageText());

        } catch (Exception e) {
            captureFailure("ExistingEmailRegistration", e);
        }
    }

    @Test(description = "Registration : Verify required field validation", priority = 3)
    public void verifyRequiredFieldValidation() {
        String expectedMessage = "⚠ Required fields error";

        try {
            log.info("🧩 Running: Required Field Validation Test");

            // Leave all fields empty and trigger validation
            enterRegistrationDetails("", "", "", "");
            clickRegisterButton(); // Important to trigger validation

            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(register.getRequiredErrorElement()));

            Assert.assertTrue(register.isrequiredErrorDisplayed(), "⚠ Required field error not displayed!");
            log.info("⚠ Required error message: {}", register.getrequiredErrorMessageText());

        } catch (Exception e) {
            captureFailure("RequiredFieldValidation", e);
        }
    }

    // ======================
    // Utilities
    // ======================

    private void enterRegistrationDetails(String firstName, String lastName, String email, String password) {
        try {
            if (firstName != null && !firstName.isEmpty()) register.enterFirstName(firstName);
            if (lastName != null && !lastName.isEmpty()) register.enterLastName(lastName);
            if (email != null && !email.isEmpty()) register.enterEmail(email);
            if (password != null && !password.isEmpty()) register.enterPassword(password);
            log.info("✅ Entered registration details.");
        } catch (Exception e) {
            log.error("❌ Error entering registration details: {}", e.getMessage());
            Assert.fail("Failed to enter registration details.");
        }
    }

    private void clickRegisterButton() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(register.getCreateAccountButton()));

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-mask")));

            try {
                submitBtn.click();
            } catch (Exception e) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", submitBtn);
                log.warn("⚠ JS Executor used to click Register button.");
            }

            log.info("🖱️ Clicked Register button.");

        } catch (Exception e) {
            log.error("❌ Failed to click Register button: {}", e.getMessage());
            Assert.fail("Click action failed.");
        }
    }

    private void captureFailure(String testName, Exception e) {
        String path = ScreenshotUtil.captureScreenshot(getDriver(), testName, false);
        log.error("❌ Test Failed - Screenshot: {}", path);
        Assert.fail("Test failed: " + e.getMessage());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("🛑 Closing browser...");
        getDriver().quit();
    }
}