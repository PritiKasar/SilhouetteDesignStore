package com.mystore.page;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mystore.base.BaseClass;

public class AccountCreationPopup extends BaseClass implements Registration{
	
	@FindBy(name= "firstname")
    private WebElement enterFirstName;

    @FindBy(name = "lastname")
    private WebElement enterLastName;

    @FindBy(name = "email")
    private WebElement enterEmail;

    @FindBy(name = "password")
    private WebElement enterPassword;


    @FindBy(id = "remember_meYtMT7AEXUn")
    private WebElement rememberberME;

    @FindBy(xpath = "//button[@title='Create an account']")
    private WebElement clickCreateAccount;

    @FindBy(xpath = "//div[@class='login-register-popup']//div[@class='response-msg']//div[@class='error']")
    private WebElement errorMessage;

    By successMessage = By.xpath("//div[@class='response-msg']/div[@class='success' and contains(text(), 'Registration successful')]");

 
	
	public AccountCreationPopup(WebDriver driver) {
		
        PageFactory.initElements(driver, this);
	}
	
	@Override
	public void enterFirstName(String Fname) {
		enterFirstName.clear();
		enterFirstName.sendKeys(Fname);		
	}

	@Override
	public void enterLastName(String Lname) {
		enterLastName.clear();
		enterLastName.sendKeys(Lname);		
		
	}

	@Override
	public void enterEmail(String email) {
		enterEmail.clear();
		enterEmail.sendKeys(email);
		
	}

	@Override
	public void enterPassword(String password) {
		enterPassword.clear();
		enterPassword.sendKeys(password);
		
	}

	

	@Override
	public boolean isCreateAccountButtonDisplayed() {
		try {
	        return clickCreateAccount.isDisplayed();
	    } catch (Exception e) {
	        return false;
	    }}

	@Override
	public boolean isUserRegistered() {
		  try {
			  WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
		        wait.until(ExpectedConditions.visibilityOf(clickCreateAccount));
	            WebElement msg = getDriver().findElement(successMessage);
	            return msg.isDisplayed();
	        } catch (Exception e) {
	            return false;
	        }
	}

	@Override
	public boolean isErrorDisplayed() {
		try {
	        return errorMessage.isDisplayed();
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}

	@Override
	public String getErrorMessageText() {
		try {
	        return errorMessage.getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}

	@Override
	public boolean waitForErrorDisplayed(int timeoutSeconds) {
		 try {
		        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
		        return wait.until(ExpectedConditions.visibilityOf(enterEmail)).isDisplayed();
		    } catch (Exception e) {
		        return false;
		    }
	}
	@Override
	public boolean isSuccessMessageDisplayed() {
	    try {
	        WebElement successMsg = getDriver().findElement(By.cssSelector("div.response-msg div.success"));
	        return successMsg.isDisplayed();
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}
@Override
	public String getSuccessMessageText() {
	    return getDriver().findElement(By.cssSelector("div.response-msg div.success")).getText();
	}
@Override
public boolean isrequiredErrorDisplayed() {
    try {
    	WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(clickCreateAccount));
        WebElement errorMsg = getDriver().findElement(By.cssSelector("div.mage-error"));
        return errorMsg.isDisplayed();
    } catch (NoSuchElementException e) {
        return false;
    }
}
@Override
public String getrequiredErrorMessageText() {
    return getDriver().findElement(By.cssSelector("div.mage-error")).getText();
}
	@Override
	public void enterConfirmPassword(String confirmpassword) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clickCreateAccount() {
	clickCreateAccount.click();		
	}
	
}
