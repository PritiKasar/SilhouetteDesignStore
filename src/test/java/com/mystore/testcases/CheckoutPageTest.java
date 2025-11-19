package com.mystore.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mystore.actiondriver.Action;
import com.mystore.base.BaseClass;
import com.mystore.page.CheckoutFlowPage;
import com.mystore.page.HomePage;
import com.mystore.page.LoginAble;
import com.mystore.page.MyAccount;
import com.mystore.utility.ExcelUtility;

public class CheckoutPageTest extends BaseClass {
    HomePage homepage;
    LoginAble login;
    MyAccount myAccount;
    CheckoutFlowPage checkoutPage;

    @Test(priority = 1, description = "Verify full checkout flow")
    public void verifyFullCheckoutFlow() throws Throwable {
        // Step 1-4: Open App & Login
        launchApp();
        homepage = new HomePage(getDriver());
        login = homepage.clickAndCheckLogin();
        Assert.assertNotNull(login, "Login popup not displayed");

        login.enterEmail("priti.kasar+2@magnetoitsolutions.com");
        login.enterPassword("Priti@123");
        login.clickSignIn();

        myAccount = new MyAccount(getDriver());
        Assert.assertTrue(myAccount.isUserLoggedIn(), "❌ Login failed");
        System.out.println("✅ Logged in successfully.");

        // Step 6-8: Clear Cart
        checkoutPage = new CheckoutFlowPage();
        checkoutPage.goToCartPage();
        checkoutPage.clearCart();
        System.out.println("✅ Cart cleared.");

        // Step 9-11: Go to seller page & select random product
        checkoutPage.goToSellerProfilePage();
        checkoutPage.goToRandomProductFromSeller();
        String expectedProductName = checkoutPage.getProductNameFromDetailsPage();
        System.out.println("✅ Random Product: " + expectedProductName);

        // Step 11-13: Add to cart & verify in cart
        checkoutPage.clickAddToCart();
        checkoutPage.goToCartPage();
        String cartProductName = checkoutPage.getProductNameFromCart();

        Assert.assertEquals(cartProductName, expectedProductName, "❌ Product name mismatch in cart");
        System.out.println("✅ Product verified in cart.");

        // Step 16-17: Proceed to checkout and wait for payment URL
        checkoutPage.clickProceedToCheckout();
        System.out.println("✅ Navigated to payment step.");
        Action.sleep(50);

        // Step 18-20: Apply store credit
        checkoutPage.applyStoreCredit("10");
        Assert.assertTrue(checkoutPage.isNoPaymentInfoDisplayed(), "❌ Store credit not applied properly.");
        System.out.println("✅ Store credit applied.");
        Action.sleep(50);

        // Step 21: Place order
        checkoutPage.placeOrder();
        System.out.println("✅ Place order clicked.");

        // Step 22: Wait for success and get order number
        String orderNumber = checkoutPage.waitForSuccessAndGetOrderNumber();
        Assert.assertFalse(orderNumber.isEmpty(), "❌ Order number not retrieved");
        System.out.println("✅ Order placed successfully. Order Number: " + orderNumber);

        // Step 23: Write order number + timestamp to Excel
        ExcelUtility.appendOrderRecord(orderNumber);
        System.out.println("✅ Order recorded in Excel.");
    }
}