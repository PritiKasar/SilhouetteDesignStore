package com.mystore.page;

import org.openqa.selenium.WebElement;

public interface Registration {

    // ============================
    // Input Methods
    // ============================
    void enterFirstName(String firstName);
    void enterLastName(String lastName);
    void enterEmail(String email);
    void enterPassword(String password);
    void enterConfirmPassword(String confirmPassword);

    // ============================
    // Actions
    // ============================
    void clickCreateAccount();

    // ============================
    // Visibility Checks
    // ============================
    boolean isCreateAccountButtonDisplayed();
    boolean isUserRegistered();
    boolean isErrorDisplayed();
    boolean isSuccessMessageDisplayed();
    boolean isrequiredErrorDisplayed();

    // ============================
    // Get Text Methods
    // ============================
    String getErrorMessageText();
    String getSuccessMessageText();
    String getrequiredErrorMessageText();

    // ============================
    // Wait / Validation Helpers
    // ============================
    boolean waitForErrorDisplayed(int timeoutSeconds);
    void triggerFieldValidation();

    // ============================
    // Element Getters
    // ============================
    WebElement getCreateAccountButton();
    WebElement getSuccessMessageElement();
    WebElement getErrorElement();
    WebElement getRequiredErrorElement();
}

