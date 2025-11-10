package com.mystore.page;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mystore.actiondriver.Action;
import com.mystore.base.BaseClass;

public class CheckoutFlowPage extends BaseClass {

    // Cart
    @FindBy(id = "magnetoclearcart")
    WebElement clearCartBtn;

    @FindBy(xpath = "//p[normalize-space()='You have no items in your shopping cart.']")
    WebElement cartEmptyMsg;

    @FindBy(css = "td.col.item div.product-item-details a")
    WebElement productNameInCart;

    // Product
    @FindBy(xpath = "//button[@id='product-addtocart-button']")
    WebElement addToCartBtn;

    @FindBy(xpath = "//span[@class='base']")
    WebElement productNameElement;

    // Checkout
    @FindBy(xpath = "//button[@title='Proceed to checkout']")
    WebElement proceedToCheckoutBtn;

    @FindBy(xpath = "//input[@id='amstorecredit_amount']")
    WebElement creditInput;

    @FindBy(xpath = "//span[contains(text(),'Apply Credit')]")
    WebElement applyCreditBtn;

    @FindBy(xpath = "//span[@data-bind='text: getTitle()']")
    WebElement noPaymentRequiredText;

    @FindBy(xpath = "//button[@title='Place Order']")
    WebElement placeOrderBtn;

    public CheckoutFlowPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public void clearCart() {
        try {
            if (clearCartBtn.isDisplayed()) {
                Action.waitForClickable(getDriver(), clearCartBtn, 5);
                clearCartBtn.click();
                System.out.println("🧹 Clear Cart button clicked.");
            }
        } catch (Exception e) {
            System.out.println("ℹ️ Clear Cart button not visible. Skipping...");
        }

        // Wait for the "empty cart" message, whether we clicked or not
        Action.waitForElement(getDriver(), cartEmptyMsg, 10);
        System.out.println("✅ Cart is empty.");
    }

    public void goToCartPage() {
        getDriver().get("https://www.silhouettedesignstore.com/checkout/cart/");
    }

    public void goToSellerProfilePage() {
        getDriver().get("https://www.silhouettedesignstore.com/marketplace/seller/profile/shop/silhouette");
    }

    public void goToRandomProductFromSeller() {
        List<WebElement> products = getDriver().findElements(By.cssSelector("li.product-item a.product.photo.product-item-photo"));
        if (products.isEmpty()) {
            throw new RuntimeException("❌ No products found on seller profile page.");
        }
        WebElement randomProduct = products.get(new Random().nextInt(products.size()));
        getDriver().get(randomProduct.getAttribute("href"));
    }

    public String getProductNameFromDetailsPage() {
        return productNameElement.getText().trim();
    }

    public void clickAddToCart() {
        Action.waitForLoadingMaskToDisappear(getDriver(), 10);
        Action.waitForClickable(getDriver(), addToCartBtn, 10);
        addToCartBtn.click();
        Action.sleep(2000); // Optional: allow cart update to complete
        System.out.println("✅ Product added to cart.");
    }

    public String getProductNameFromCart() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(50));

        // Wait for product name to be visible in cart
        By productNameLocator = By.cssSelector("td.col.item div.product-item-details a");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNameLocator));

        WebElement productNameInCart = getDriver().findElement(productNameLocator);
        return productNameInCart.getText().trim();
    }
    public void clickProceedToCheckout() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
      /*  String currentUrl = getDriver().getCurrentUrl();
        String correctCartUrl = "https://silhouettedesignstore.com/checkout/cart/";

        try {
            // Step 1: Redirect to correct cart URL if not already there
            if (!currentUrl.equalsIgnoreCase(correctCartUrl)) {
                System.out.println("🔄 Redirecting to correct cart URL...");
                getDriver().get(correctCartUrl);

                // Wait for page and cart items to load
                wait.until(ExpectedConditions.urlToBe(correctCartUrl));
                Action.waitForLoadingMaskToDisappear(getDriver(), 10);
                System.out.println("✅ Successfully redirected to staging cart URL.");
            } else {
                System.out.println("✅ Already on correct cart page.");
            }

            // Step 2: Wait for any loading mask to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-mask")));

            // Step 3: Wait for the button to be clickable
            Action.waitForClickable(getDriver(), proceedToCheckoutBtn, 10);

            // Step 4: Click the Proceed to Checkout button
            proceedToCheckoutBtn.click();
            System.out.println("✅ Proceed to checkout button clicked.");

            // Step 5: Wait briefly for navigation or any loading
            Action.waitForLoadingMaskToDisappear(getDriver(), 15);*/
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-mask")));
        proceedToCheckoutBtn.click();
        System.out.println("Proceed to checkout button clicked");
            Action.sleep(3000);

       /* } catch (TimeoutException e) {
            System.err.println("❌ Timeout while waiting during proceed to checkout process: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error in clickProceedToCheckout(): " + e.getMessage());
            throw e;
        }*/
    }

    public void applyStoreCredit(String amount) {
        Action.type(creditInput, amount);
        applyCreditBtn.click();
        System.out.println("✅ Apply credit button clicked !");

        Action.sleep(5000); // wait for credit to apply
       


        
    }

    public boolean isNoPaymentInfoDisplayed() {
        return noPaymentRequiredText.getText().contains("No Payment Information Required");
       

    }
    

    public void placeOrder() {
    	
        // Wait for any loading mask to disappear
        Action.waitForLoadingMaskToDisappear(getDriver(), 10);

        // Wait for the button to be clickable
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));

        placeOrderBtn.click();

        // Optional: log current URL for debugging
        System.out.println("Current URL after clicking Place Order: " + getDriver().getCurrentUrl());
    }

    public String waitForSuccessAndGetOrderNumber() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));

        try {
            wait.until(ExpectedConditions.urlContains("/checkout/onepage/success"));
        } catch (TimeoutException e) {
            // Log current URL
            System.err.println("❌ Order placement failed. Current URL: " + getDriver().getCurrentUrl());

            // Check if returned to cart
            if (getDriver().getCurrentUrl().contains("/checkout/cart")) {
                throw new RuntimeException("❌ Order was not placed. Returned to cart page.");
            } else {
                throw e;
            }
        }

        WebElement orderMsg = getDriver().findElement(By.xpath("//div[@class='checkout-success']//p[1]"));
        String orderText = orderMsg.getText().trim();
        return orderText.replaceAll("[^0-9]", "");
    }
}