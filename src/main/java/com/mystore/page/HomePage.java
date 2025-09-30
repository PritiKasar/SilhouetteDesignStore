/**
 * 
 */
package com.mystore.page;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mystore.base.BaseClass;

/**
 * * HomePage
 * -----------
 * Represents the home page after navigation.
 * Handles search, menu, banners, and logo navigation.
 */
public class HomePage extends BaseClass{
	
	private WebDriver driver;
    private WebDriverWait wait;
    
	@FindBy(id= "search")
    private WebElement Searchbox;
	@FindBy(xpath= "//label[@for='search']/span")
	private WebElement Searchicon;
	@FindBy(css = "div.kuQuickNoResultsMessage")
	private WebElement 	noResultsMessage;
	 private By productNames = By.cssSelector("div.klevuQuickProductName");
	    private By menuItems = By.cssSelector("nav ul li a");   // all menu links
	    private By banners = By.cssSelector(".swiper-slide img"); // banner images
	    private By clickableBanners = By.cssSelector(".swiper-slide a"); // banner links
	    @FindBy(xpath = "//img[@alt=' the design store logo']")
	    private WebElement logo;
	    @FindBy(css = "ul.flex li a.level1-link.has-sub-cat")
	    private List<WebElement> mainMenuItems;

	 
	public HomePage(WebDriver driver) {
		 this.driver = getDriver();  // 🔹 getDriver() comes from BaseClass
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
	}
	
	public void searchTerm(String searchtext) {
		Searchbox.clear();
		Searchbox.sendKeys(searchtext);		
		Searchbox.sendKeys(Keys.ENTER); 
	}
	 // Get all product names displayed
    public List<WebElement> getProductNames() {
        return driver.findElements(productNames);
    }
 // Check if top N products contain the search term
    public boolean doTopProductsContainSearchTerm(String searchTerm, int topN) {
        List<WebElement> products = getProductNames();
        int limit = Math.min(topN, products.size());

        for (int i = 0; i < limit; i++) {
            String productName = products.get(i).getText().toLowerCase();
            if (!productName.contains(searchTerm.toLowerCase())) {
                System.out.println("❌ Product failed: " + productName);
                return false; // if any product doesn't match, fail
            } else {
                System.out.println("✅ Product matched: " + productName);
            }
        }
        return true;
    }
	

	    public boolean isNoResultsMessageDisplayed() throws TimeoutException {
	        wait.until(ExpectedConditions.visibilityOf(noResultsMessage));
			return noResultsMessage.isDisplayed();
	    }

	    public String getNoResultsText() {
	        return noResultsMessage.getText();
	    }
	    
	    // ✅ Get list of all menu item texts
	    public List<String> getMenuItemNames() {
	        List<String> names = new ArrayList<>();
	        for (WebElement item : mainMenuItems) {
	            names.add(item.getText().trim());
	        }
	        return names;
	    }
	    public void verifyAllMenuItemsClickable() {
	        for (int i = 0; i < mainMenuItems.size(); i++) {
	            try {
	                // Re-fetch to avoid stale element issue
	                WebElement menuItem = mainMenuItems.get(i);

	                // Scroll into view
	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menuItem);

	                // Wait for clickability
	                wait.until(ExpectedConditions.elementToBeClickable(menuItem));

	                System.out.println("✅ Menu clickable: " + menuItem.getText());

	            } catch (Exception e) {
	                System.out.println("❌ Menu not clickable at index " + i + " - " + e.getMessage());
	            }}}


	  

	    /** Click site logo to return home */
	    public void clickLogo() {
	        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
	    }}
