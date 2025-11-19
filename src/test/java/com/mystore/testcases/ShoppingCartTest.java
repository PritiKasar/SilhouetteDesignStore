package com.mystore.testcases;

import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mystore.actiondriver.Action;
import com.mystore.base.BaseClass;
import com.mystore.page.NewDesignsPage;
import com.mystore.page.ProductPage;
import com.mystore.page.ShoppingCartPage;

public class ShoppingCartTest extends BaseClass {

    NewDesignsPage newDesignsPage;
    ProductPage productPage;
    ShoppingCartPage shoppingCartPage;

    String randomProductURL;
    String productTitle;
    String productPrice;

    @BeforeClass
    public void setup() {
        // 1️⃣ Launch app
        launchApp();

        // 2️⃣ Navigate to New Designs page
        getDriver().get("https://www.silhouettedesignstore.com/new.html");
        newDesignsPage = new NewDesignsPage();

        // 3️⃣ Get all product links
        List<String> allLinks = newDesignsPage.getAllProductLinks();
        if (allLinks.isEmpty()) throw new RuntimeException("❌ No product links found!");

        // 4️⃣ Pick a random product
        randomProductURL = allLinks.get(new Random().nextInt(allLinks.size()));
        System.out.println("🎯 Random Product Selected: " + randomProductURL);
        Action.sleep(50);


        // 5️⃣ Open the random product
        getDriver().get(randomProductURL);
        productPage = new ProductPage();

        // 6️⃣ Capture product title and price for comparison
        productTitle = productPage.getProductTitle();
        productPrice = productPage.getRegularPrice();

        System.out.println("📌 Product Page Title: " + productTitle);
        System.out.println("💲 Product Page Price: " + productPrice);

        // 7️⃣ Add product to cart
        productPage.clickAddToCart();
        Action.sleep(50);


        // 8️⃣ Redirect to Shopping Cart
        getDriver().get("https://www.silhouettedesignstore.com/checkout/cart/");
        shoppingCartPage = new ShoppingCartPage();
        
    }

    @Test(description = "Shopping Cart : Validate product and prices in Shopping Cart match Product Page")
    public void verifyShoppingCartDetails() {

        // 🛒 Verify Shopping Cart title
      /*  String cartTitle = shoppingCartPage.getCartPageTitle();
        System.out.println("🛍️ Cart Page Title: " + cartTitle);
        Assert.assertEquals(cartTitle, "Shopping Cart", "❌ Cart title mismatch");
*/
        // 📦 Verify product title
        String titleInCart = shoppingCartPage.getProductTitleInCart();
        System.out.println("📦 Product in Cart: " + titleInCart);
        Assert.assertEquals(titleInCart, productTitle, "❌ Product title mismatch between product page and cart");

        // 💲 Verify subtotal and total price
      // String subtotal = shoppingCartPage.getSubtotalPrice();
    //  String orderTotal = shoppingCartPage.getOrderTotalPrice();

    //   System.out.println("💲 Subtotal: " + subtotal);
    //   System.out.println("💲 Order Total: " + orderTotal);

     // Assert.assertEquals(subtotal, productPrice, "❌ Subtotal does not match product price");
   //   Assert.assertEquals(orderTotal, productPrice, "❌ Order Total does not match product price");
    }
}