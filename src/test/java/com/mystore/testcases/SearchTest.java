/**
 * 
 */
package com.mystore.testcases;
import java.util.concurrent.TimeoutException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.mystore.base.BaseClass;
import com.mystore.page.HomePage;
import com.mystore.page.IndexPage;

/**
 * 
 */
public class SearchTest extends BaseClass{
	private HomePage homePage;
	@BeforeMethod
    public void setUp() {
        // Launch app from BaseClass
        launchApp();
        // Initialize HomePage object (uses getDriver() from BaseClass)
        homePage = new HomePage(getDriver());
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();  // closes browser after each test
    }
    @Test
    public void testValidSearchShowsResults() throws TimeoutException, InterruptedException {

        String searchTerm = "flower";
        homePage.searchTerm(searchTerm);
       
     // Assume search action already done → lands on SearchResultsPage
        HomePage resultsPage = new HomePage(getDriver());

        boolean status = resultsPage.doTopProductsContainSearchTerm(searchTerm, 5);

        Assert.assertTrue(status, "Some of the first 5 products did not contain the search term!");
        Thread.sleep(3000);
    }

    @Test
    public void testInvalidSearchShowsNoResultsMessage() throws TimeoutException, InterruptedException {
    	 String invalidSearchTerm = "xyzabc123";
    	 homePage.searchTerm(invalidSearchTerm);

        HomePage resultsPage = new HomePage(getDriver());

         Assert.assertTrue(resultsPage.isNoResultsMessageDisplayed(),
                 "❌ No results message was not displayed for invalid search!");

         System.out.println("ℹ️ Message: " + resultsPage.getNoResultsText());
         Thread.sleep(3000);
     }

}
