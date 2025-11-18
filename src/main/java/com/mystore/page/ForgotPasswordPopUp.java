package com.mystore.page;
//Wrong code-temporary
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mystore.base.BaseClass;

public class ForgotPasswordPopUp extends BaseClass {

    @FindBy(id = "email_address")
    private WebElement emailInput;

    @FindBy(css = "button.action.submit")
    private WebElement submitButton;

    @FindBy(css = "div.message-success")
    private WebElement successMessage;

    @FindBy(css = "div.message-error")
    private WebElement errorMessage;

    public ForgotPasswordPopUp() {
        PageFactory.initElements(getDriver(), this);
    }

    public void enterEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void clickSubmit() {
        submitButton.click();
    }

    public boolean isSuccessDisplayed() {
        return successMessage != null && successMessage.isDisplayed();
    }

    public boolean isErrorDisplayed() {
        return errorMessage != null && errorMessage.isDisplayed();
    }

    public String getSuccessMessage() {
        return successMessage.getText().trim();
    }

    public String getErrorMessage() {
        return errorMessage.getText().trim();
    }
}