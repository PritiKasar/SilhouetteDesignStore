package com.mystore.page;

import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mystore.base.BaseClass;
import com.mystore.actiondriver.Action;

/**
 * LoginPopUp:
 * Handles login form displayed inside a popup modal.
 * Implements LoginAble for unified login operations.
 */
public class LoginPopUp extends BaseClass implements LoginAble {

    @FindBy(id = "username")
    private WebElement emailField;

    @FindBy(id = "popup-password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@title='Sign In']")
    private WebElement signInButton;

    @FindBy(xpath = "//a[text()=' Create an account']")
    private WebElement createAccountLink;

    @FindBy(xpath = "//a[text()='Forgot Password?']")
    private WebElement forgotPasswordLink;

    @FindBy(id = "remember_me")
    private WebElement rememberMeCheckbox;

    @FindBy(css = "div#user-logged-in a.user-link-dropdown.lm")
    private WebElement accountLink;

    @FindBy(xpath = "//div[@class='login-register-popup']//div[@class='response-msg']//div[@class='error']")
    private WebElement loginErrorMessage;

    public LoginPopUp() {
        PageFactory.initElements(getDriver(), this);
    }

    // -----------------------------------------------------------------------
    // Actions
    // -----------------------------------------------------------------------

    @Override
    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    @Override
    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    @Override
    public void clickSignIn() {
        signInButton.click();
    }

    public AccountCreationPopup openCreateAccountPopup() {
        Action.click(getDriver(), createAccountLink);
        return new AccountCreationPopup();
    }

    public ForgotPasswordPopUp openForgotPasswordPopup() {
        Action.click(getDriver(), forgotPasswordLink);
        return new ForgotPasswordPopUp();
    }

    public void toggleRememberMe() {
        rememberMeCheckbox.click();
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
            return accountLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isErrorDisplayed() {
        try {
            return loginErrorMessage != null && loginErrorMessage.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public String getErrorMessageText() {
        try {
            return loginErrorMessage.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean waitForErrorDisplayed(int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOf(loginErrorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
