package com.mystore.page;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mystore.base.BaseClass;

/**
 * AccountCreationPopup
 * --------------------
 * Handles registration popup actions with:
 *  - Selenium 4.24+ compatible waits
 *  - Clean input helpers
 *  - Robust success/error message handling
 *  - Full Java 21 compliance
 */
public class AccountCreationPopup extends BaseClass implements Registration {

    // ===========================
    // Locators
    // ===========================
    @FindBy(name = "firstname")
    private WebElement firstNameField;

    @FindBy(name = "lastname")
    private WebElement lastNameField;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(id = "remember_meYtMT7AEXUn")
    private WebElement rememberMeCheckbox;

    @FindBy(xpath = "//button[@title='Create an account']")
    private WebElement createAccountBtn;

    @FindBy(xpath = "//div[@class='login-register-popup']//div[@class='response-msg']//div[@class='error']")
    private WebElement errorMessage;

    private final By successMessage = By.xpath(
            "//div[@class='response-msg']/div[@class='success' and contains(text(), 'Registration successful')]");

    private final By requiredFieldError = By.cssSelector("div.mage-error");

    // ===========================
    // Constructor
    // ===========================
    public AccountCreationPopup() {
        PageFactory.initElements(BaseClass.getDriver(), this);
    }

    // ===========================
    // Wait Helper (Selenium 4.24+)
    // ===========================
    private WebDriverWait waitFor(int seconds) {
        WebDriverWait wait = new WebDriverWait(BaseClass.getDriver(), Duration.ofSeconds(seconds));
        wait.pollingEvery(Duration.ofMillis(300));
        return wait;
    }

    private WebDriverWait defaultWait() {
        return waitFor(10);
    }

    private WebElement waitUntilVisible(WebElement element) {
        return defaultWait().until(ExpectedConditions.visibilityOf(element));
    }

    // ===========================
    // Field Input Handling
    // ===========================
    private void fillField(WebElement field, String value) {
        WebElement visibleField = waitUntilVisible(field);
        visibleField.clear();
        visibleField.sendKeys(value);
    }

    @Override
    public void enterFirstName(String value) {
        fillField(firstNameField, value);
    }

    @Override
    public void enterLastName(String value) {
        fillField(lastNameField, value);
    }

    @Override
    public void enterEmail(String value) {
        fillField(emailField, value);
    }

    @Override
    public void enterPassword(String value) {
        fillField(passwordField, value);
    }

    @Override
    public void enterConfirmPassword(String confirmpassword) {
        // No confirm password field in UI (as of now)
    }

    // ===========================
    // Actions
    // ===========================
    @Override
    public void clickCreateAccount() {
        try {
            defaultWait().until(ExpectedConditions.elementToBeClickable(createAccountBtn));
            defaultWait().until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-mask")));

            try {
                createAccountBtn.click();
            } catch (Exception e) {
                // fallback JavaScript click
                ((JavascriptExecutor) BaseClass.getDriver())
                        .executeScript("arguments[0].click();", createAccountBtn);
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Create Account: " + e.getMessage());
        }
    }

    // ===========================
    // Button Visibility
    // ===========================
    @Override
    public WebElement getCreateAccountButton() {
        return createAccountBtn;
    }

    @Override
    public boolean isCreateAccountButtonDisplayed() {
        try {
            return createAccountBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ===========================
    // Success / Error Handling
    // ===========================
    @Override
    public boolean isUserRegistered() {
        try {
            return defaultWait()
                    .until(ExpectedConditions.visibilityOfElementLocated(successMessage))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getErrorMessageText() {
        try {
            return errorMessage.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean isSuccessMessageDisplayed() {
        try {
            return BaseClass.getDriver().findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSuccessMessageText() {
        try {
            return BaseClass.getDriver().findElement(successMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean isrequiredErrorDisplayed() {
        try {
            return defaultWait()
                    .until(ExpectedConditions.visibilityOfElementLocated(requiredFieldError))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getrequiredErrorMessageText() {
        try {
            return BaseClass.getDriver().findElement(requiredFieldError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ===========================
    // Trigger Validation
    // ===========================
    @Override
    public void triggerFieldValidation() {
        JavascriptExecutor js = (JavascriptExecutor) BaseClass.getDriver();

        blur(js, firstNameField);
        blur(js, lastNameField);
        blur(js, emailField);
        blur(js, passwordField);
    }

    private void blur(JavascriptExecutor js, WebElement field) {
        try {
            if (field != null && field.isDisplayed()) {
                js.executeScript("arguments[0].focus(); arguments[0].blur();", field);
            }
        } catch (Exception ignored) {}
    }

    // ===========================
    // Error Wait
    // ===========================
    @Override
    public boolean waitForErrorDisplayed(int timeoutSeconds) {
        try {
            waitFor(timeoutSeconds).until(ExpectedConditions.visibilityOf(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===========================
    // Element Getters
    // ===========================
    @Override
    public WebElement getSuccessMessageElement() {
        return defaultWait()
                .until(ExpectedConditions.visibilityOfElementLocated(successMessage));
    }

    @Override
    public WebElement getErrorElement() {
        return defaultWait().until(ExpectedConditions.visibilityOf(errorMessage));
    }

    @Override
    public WebElement getRequiredErrorElement() {
        return defaultWait()
                .until(ExpectedConditions.visibilityOfElementLocated(requiredFieldError));
    }
}

