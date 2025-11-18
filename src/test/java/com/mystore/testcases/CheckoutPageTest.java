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

    private HomePage homepage;
    private LoginAble loginPopup;
    private MyAccount myAccount;
    private CheckoutFlowPage checkoutPage;

    @Test(priority = 1, description = "Verify the complete checkout process from login to order placement.")
    public void verifyFullCheckoutFlow() throws Throwable {

        // -------------------------
        // STEP 1: Launch Application
        // -------------------------
        launchApp();
        homepage = new HomePage(getDriver());
        
        loginPopup = homepage.clickAndCheckLogin();
        Assert.assertNotNull(loginPopup, "❌ Login popup did not appear.");
        System.out.println("✅ Login popup displayed.");

        // -------------------------
        // STEP 2: Login
        // -------------------------
        loginPopup.enterEmail("priti.kasar+2@magnetoitsolutions.com");
        loginPopup.enterPassword("Priti@123");
        loginPopup.clickSignIn();

        myAccount = new MyAccount(getDriver());
        Assert.assertTrue(myAccount.isUserLoggedIn(), "❌ Login failed.");
        System.out.println("✅ Login successful.");

        // -------------------------
        // STEP 3: Clear Cart
        // -------------------------
        checkoutPage = new CheckoutFlowPage();

        checkoutPage.goToCartPage();
        checkoutPage.clearCart();
        System.out.println("🧹 Cart cleared.");

        // -------------------------
        // STEP 4: Go to Seller & Pick Random Product
        // -------------------------
        checkoutPage.goToSellerProfilePage();
        checkoutPage.goToRandomProductFromSeller();

        String expectedProductName = checkoutPage.getProductNameFromDetailsPage();
        Assert.assertNotNull(expectedProductName, "❌ No product name retrieved.");
        System.out.println("🛒 Selected Product: " + expectedProductName);

        // -------------------------
        // STEP 5: Add to Cart & Validate
        // -------------------------
        checkoutPage.clickAddToCart();
        checkoutPage.goToCartPage();

        String actualCartProductName = checkoutPage.getProductNameFromCart();
        Assert.assertEquals(actualCartProductName, expectedProductName, "❌ Product mismatch in cart.");
        System.out.println("🛍️ Product correctly reflected in cart.");

        // -------------------------
        // STEP 6: Proceed to Checkout
        // -------------------------
        checkoutPage.clickProceedToCheckout();
        System.out.println("➡️ Proceeded to checkout.");

        // -------------------------
        // STEP 7: Apply Store Credit
        // -------------------------
        checkoutPage.applyStoreCredit("10");

        Assert.assertTrue(
                checkoutPage.isNoPaymentInfoDisplayed(),
                "❌ Store credit not applied or payment info still visible."
        );
        System.out.println("💳 Store credit applied successfully.");

        Action.sleep(40);  // If this is waiting for balance update, replace later with a dynamic wait.

        // -------------------------
        // STEP 8: Place the Order
        // -------------------------
        checkoutPage.placeOrder();
        System.out.println("📝 Place Order clicked.");

        // -------------------------
        // STEP 9: Validate Order Success Page
        // -------------------------
        String orderNumber = checkoutPage.waitForSuccessAndGetOrderNumber();
        Assert.assertFalse(orderNumber.isEmpty(), "❌ Failed to retrieve order number.");
        System.out.println("🎉 Order Placed Successfully! Order No: " + orderNumber);

        // -------------------------
        // STEP 10: Record in Excel
        // -------------------------
        ExcelUtility.appendOrderRecord(orderNumber);
        System.out.println("📁 Order recorded in Excel.");
    }
}
