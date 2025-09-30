/**
 * 
 */
package com.mystore.testcases;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mystore.base.BaseClass;
import com.mystore.page.IndexPage;
import com.mystore.page.LoginAble;
import com.mystore.page.LoginPopUp;
import com.mystore.page.MyAccount;

/**
 * 
 */
public class Login extends BaseClass{
    IndexPage indexPage;
    LoginAble login;
	
	@BeforeMethod
	public void setup() throws Throwable{
		 launchApp();
	        indexPage = new IndexPage(getDriver());

	        // Try opening login popup
	        login = indexPage.clickAndCheckLogin();

	        if (login == null) {
	        	 Assert.fail("❌ Neither login popup nor login page opened.");
	        }
	    }
	
	@Test(description = "Verify SignIn element presence")
    public void verifySignInButtonPresence() {
        Assert.assertTrue(login.isSignInButtonDisplayed(),
            "❌ Sign In button should be displayed (popup or page).");
    }

	/*
	@Test(description = "Verify Login popup element")
	public void verifyLoginPopupElements() {
		login = new LoginPopUp();
		Assert.assertTrue(login.isSignInButtonDisplayed(), "Sign In button is not displayed");
        // Check other elements (optional)
       // loginpopup.toggleRememberMe();
		System.out.println("✅ Login popup elements verified.");
	}*/
	
	@Test(description = "Verify Login popup with valid credentials")
	public void loginWithValidCredentials() throws Throwable {
		
	    // Read credentials from config.properties
	    String username = prop.getProperty("username");
	    String password = prop.getProperty("password");
		

	    // Perform login
	    login.enterEmail(username);
	    login.enterPassword(password);
	    login.clickSignIn();

	    // Assertion: example check (update locator/page object as per your app)
	    // e.g., after login user’s account/profile icon should be displayed
	    MyAccount myAccountPage = new MyAccount(getDriver());
	    Assert.assertTrue(myAccountPage.isUserLoggedIn(), "Login failed: User not logged in.");
        System.out.println("✅ Login with valid credentials successful.");

	}
	
	@Test(description = "Verify Login popup with invalid credentials")
	public void loginWithInValidCredentials() throws Throwable {
	    
	    // Read invalid credentials from config.properties
	    String username = prop.getProperty("fakeusername");
	    String password = prop.getProperty("fakepassword");

	    // Perform login
	    login.enterEmail(username);
	    login.enterPassword(password);
	    login.clickSignIn();

	    // Assertion using Page Object
	    Assert.assertTrue(login.waitForErrorDisplayed(10),
                "❌ Error message was not displayed for invalid login.");
        Assert.assertEquals(
                login.getErrorMessageText(),
                "This email is not registered with us. Please create an account to continue shopping.",
                "❌ Error message text did not match!"
        );

        System.out.println("✅ Verified error message is displayed for invalid login.");
    }

	
	@AfterMethod
	public void tearDown() {
		getDriver().quit();
	}

}
