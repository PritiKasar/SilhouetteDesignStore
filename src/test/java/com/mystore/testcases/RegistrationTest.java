/**
 * 
 */
package com.mystore.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mystore.base.BaseClass;
import com.mystore.page.IndexPage;
import com.mystore.page.MyAccount;
import com.mystore.page.Registration;

/**
 * 
 */
public class RegistrationTest extends BaseClass{
    IndexPage indexPage;
	Registration register;
	@BeforeMethod
	public void setup() throws Throwable{
		 launchApp();
	        indexPage = new IndexPage(getDriver());
	        // Try opening login popup
	        register = indexPage.clickAndCheckRegisterPopup();

	        if (register == null) {
	        	 Assert.fail("❌ Neither login popup nor login page opened.");
	        }}
	    

	 @Test(priority = 1)
	    public void verifyCreateAccountButtonIsDisplayed() {
	        Assert.assertTrue(register.isCreateAccountButtonDisplayed(), "Create Account button is not displayed!");
	    }

	    @Test(priority = 2)
	    public void registerWithValidData() throws Throwable{
		    MyAccount myAccountPage = new MyAccount(getDriver());

		    register.enterFirstName("John");
		    register.enterLastName("Doe");
		    register.enterEmail("johndoe" + System.currentTimeMillis() + "@test.com"); // unique email
		    register.enterPassword("Test@1234");
		    register.clickCreateAccount();

		    Assert.assertTrue(register.isSuccessMessageDisplayed(), "Success message not displayed!");
		    System.out.println("Success message: " + register.getSuccessMessageText());
	    }

	    @Test(priority = 3)
	    public void registerWithExistingEmail() {
	    	register.enterFirstName("John");
	        register.enterLastName("Doe");
	        register.enterEmail("existingemail@test.com"); // email already registered
	        register.enterPassword("Test@1234");
	        register.clickCreateAccount();

	        Assert.assertTrue(register.isErrorDisplayed(), "Error message not displayed!");
	        System.out.println("Error Message: " + register.getErrorMessageText());
	    }

	    @Test(priority = 4)
	    public void registerWithEmptyFields() {
	    	register.clickCreateAccount();

	        Assert.assertTrue(register.isrequiredErrorDisplayed(), "Error message not displayed for empty fields!");
	        System.out.println("Error Message: " + register.getrequiredErrorMessageText());
	    }
}
