package com.mystore.page;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mystore.base.BaseClass;

/**
 * LoginPage:
 * Handles full-page login form (standard login view).
 * Implements LoginAble to maintain consistency with popup login.
 */
public class LoginPage extends BaseClass implements LoginAble {

    public LoginPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // -----------------------------------------------------------------------
    // Web Elements
    // -----------------------------------------------------------------------

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "pass")
    private WebElement passwordField;

    @FindBy(id = "send2")
    private WebElement signInButton;

    @FindBy(xpath = "//strong[@id='block-customer-login-heading']")
    private WebElement accountHeading;

    @FindBy(xpath = "//div[@data-ui-id='message-error']")
    private WebElement loginErrorMessage;

    private final By loginErrorMessageBy = By.xpath("//div[@data-ui-id='message-error']");

    // -----------------------------------------------------------------------
    // Implemented Methods
    // -----------------------------------------------------------------------

    @Override
    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    @Override
    public void enterPassword(String pwd) {
        passwordField.clear();
        passwordField.sendKeys(pwd);
    }

    @Override
    public void clickSignIn() {
        signInButton.click();
    }

    @Override
    public boolean isSignInButtonDisplayed() {
        try {
            return signInButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isUserLoggedIn() {
        try {
            return accountHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isErrorDisplayed() {
        try {
            return getDriver().findElements(loginErrorMessageBy).size() > 0 &&
                   getDriver().findElement(loginErrorMessageBy).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getErrorMessageText() {
        try {
            return loginErrorMessage.getText().trim();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    @Override
    public boolean waitForErrorDisplayed(int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessageBy));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

