package com.mystore.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.mystore.actiondriver.Action;
import com.mystore.base.BaseClass;

public class ShoppingCartPage extends BaseClass {

    // Locators
  private By cartPageTitle = By.cssSelector("h1.page-title span.base[data-ui-id='page-title-wrapper']");

    private By productTitleInCart = By.cssSelector("h1.page-title span.base[data-ui-id='page-title-wrapper']");
    private By subtotalPrice = By.cssSelector("tr[class='totals sub'] span[class='price']");
    private By orderTotalPrice = By.cssSelector("strong span[class='price']");

    public ShoppingCartPage() {
        PageFactory.initElements(getDriver(), this);
    }

    /** 🛒 Returns the visible "Shopping Cart" heading text */
    public String getCartPageTitle() {
        WebElement heading = Action.waitForElementVisible(getDriver(), cartPageTitle, 10);
        return heading.getText().trim();
    }


    /** 📦 Returns product name from the cart */
    public String getProductTitleInCart() {
        WebElement titleElement = getDriver().findElement(productTitleInCart);
        return titleElement.getText().trim();
    }

    /** 💲 Returns subtotal price */
    public String getSubtotalPrice() {
        WebElement subtotal = getDriver().findElement(subtotalPrice);
        return subtotal.getText().trim();
    }

    /** 💲 Returns order total */
    public String getOrderTotalPrice() {
        WebElement total = getDriver().findElement(orderTotalPrice);
        return total.getText().trim();
    }
}
