package com.mystore.testcases;

import org.testng.Assert;
import org.testng.annotations.*;

import com.mystore.base.BaseClass;
import com.mystore.dataprovider.DataProviders;
import com.mystore.page.HomePage;
import com.mystore.page.LoginAble;
import com.mystore.page.MyAccount;

@Listeners(listeners.ExtentTestListener.class)
public class Login extends BaseClass {

    private HomePage homepage;
    private LoginAble login;

    @BeforeMethod
    public void setup() throws Throwable {
        launchApp();
        homepage = new HomePage(getDriver());

        login = homepage.clickAndCheckLogin();
        if (login == null) {
            Assert.fail("❌ Neither login popup nor login page was detected.");
        }
    }

    // ----------------------- TESTS ---------------------------

    @Test(
        description = "Login : Verify SignIn button presence in Login Popup/Page",
        groups = { "unit" },
        priority = 1
    )
    public void verifySignInButtonPresence() {
        Assert.assertTrue(
            login.isSignInButtonDisplayed(),
            "❌ Sign In button should be visible (popup or page)."
        );
    }


    @Test(
        description = "Login : Verify login with valid credentials",
        groups = { "sanity" },
        dataProvider = "Credentials",
        dataProviderClass = DataProviders.class,
        priority = 2
    )
    public void loginWithValidCredentials(String uname, String pswd) throws Throwable {

        // Perform login
        login.enterEmail(uname);
        login.enterPassword(pswd);
        login.clickSignIn();

        // Validate login via MyAccount page
        MyAccount myAccountPage = new MyAccount(getDriver());
        Assert.assertTrue(
            myAccountPage.isUserLoggedIn(), 
            "❌ Login failed: User is not logged in."
        );

        System.out.println("✅ Login with valid credentials successful.");
    }


    @Test(
        description = "Login : Verify login with invalid credentials",
        groups = { "sanity" },
        priority = 3
    )
    public void loginWithInvalidCredentials() throws Throwable {

        String username = prop.getProperty("fakeusername");
        String password = prop.getProperty("fakepassword");

        login.enterEmail(username);
        login.enterPassword(password);
        login.clickSignIn();

        // Validate error message
        Assert.assertTrue(
            login.waitForErrorDisplayed(10),
            "❌ Expected error message was not displayed for invalid login."
        );

        Assert.assertEquals(
            login.getErrorMessageText(),
            "This email is not registered with us. Please create an account to continue shopping.",
            "❌ Error message content mismatch!"
        );

        System.out.println("✅ Verified error message for invalid login.");
    }

    // ---------------------------------------------------------

    @AfterMethod
    public void tearDown() {
        getDriver().quit();
    }
}

