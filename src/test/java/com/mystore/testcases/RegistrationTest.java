/**
 * 
 */
package com.mystore.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mystore.base.BaseClass;
import com.mystore.page.HomePage;
import com.mystore.page.Registration;

/**
 * 
 */
public class RegistrationTest extends BaseClass{
	HomePage homepage;
	Registration register;
	@BeforeMethod
	public void setup() throws Throwable{
		 launchApp();
			homepage = new HomePage(getDriver());

	        // Try opening login popup
	        register = homepage.clickAndCheckRegisterPopup();

	        if (register == null) {
	        	 Assert.fail("❌ Neither login popup nor login page opened.");
	        }}
	    

	 @Test(description = "Verify Registration element presence", groups = { "unit"})
	    public void verifyCreateAccountButtonIsDisplayed() {
	        Assert.assertTrue(register.isCreateAccountButtonDisplayed(), "Create Account button is not displayed!");
	    }

	    @Test(description = "Verify Registration with valid data", groups = { "system"})
	    public void registerWithValidData() throws Throwable{
		    register.enterFirstName("John");
		    register.enterLastName("Doe");
		    register.enterEmail("johndoe" + System.currentTimeMillis() + "@test.com"); // unique email
		    register.enterPassword("Test@1234");
		    register.clickCreateAccount();

		    Assert.assertTrue(register.isSuccessMessageDisplayed(), "Success message not displayed!");
		    System.out.println("Success message: " + register.getSuccessMessageText());
	    }

	    @Test(description = "Verify Registration with existing data", groups = {"system"})
	    public void registerWithExistingEmail() {
	    	register.enterFirstName("John");
	        register.enterLastName("Doe");
	        register.enterEmail("existingemail@test.com"); // email already registered
	        register.enterPassword("Test@1234");
	        register.clickCreateAccount();

	        Assert.assertTrue(register.isErrorDisplayed(), "Error message not displayed!");
	        System.out.println("Error Message: " + register.getErrorMessageText());
	    }

	    @Test(description = "Verify Registration with blank data", groups = {"system"})
	    public void registerWithEmptyFields() {
	    	register.clickCreateAccount();

	        Assert.assertTrue(register.isrequiredErrorDisplayed(), "Error message not displayed for empty fields!");
	        System.out.println("Error Message: " + register.getrequiredErrorMessageText());
	    }
}
