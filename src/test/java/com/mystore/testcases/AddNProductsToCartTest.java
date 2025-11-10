/**
 * 
 */
package com.mystore.testcases;

/**
 * 
 */
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mystore.base.BaseClass;
import com.mystore.page.DesignsPage;

public class AddNProductsToCartTest extends BaseClass {

    @Test
    public void verifyAdding50ProductsToCart() {
        DesignsPage designsPage = new DesignsPage();
        designsPage.openDesignsPage();
        designsPage.addFirstNProductsToCart(50);

        int finalCount = designsPage.getCartCount();
        System.out.println("🛒 Final cart count: " + finalCount);
        Assert.assertTrue(finalCount >= 50, "Cart should have at least 50 items");
    }
}
