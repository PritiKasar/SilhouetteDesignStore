/**
 * 
 */
package com.mystore.testcases;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mystore.base.BaseClass;
import com.mystore.page.HomePage;
import com.mystore.page.IndexPage;

/* IndexPageTest
 * ---------------
 * Sanity & end-to-end tests for index page features:
 * - Logo
 * - Title
 * - Broken links
 * - Navigation
 * - Menu
 * - Banners
 */
public class IndexPageTest extends BaseClass{
	IndexPage indexpage;
	HomePage homepage;
@BeforeMethod
	public void setup() {
	launchApp();
}
@Test(description = "Verify the homepage logo")
public void verifyLogo() throws Throwable{
	indexpage = new IndexPage(getDriver());
	boolean result=indexpage.verifyLogo();
	Assert.assertTrue(result);
}
@Test(description = "Verify the homepage title")
public void verifyTitle() {
	indexpage = new IndexPage(getDriver());

	String actualtitle=indexpage.verifyTitle();
	Assert.assertEquals(actualtitle, "Crafting Made Easy with Digital Designs & Fonts", 
            "Page title did not match!");
}
@Test(description = "Verify no broken links on homepage")
public void verifyBrokenLinks() {
    indexpage = new IndexPage(getDriver());
    List<String> brokenLinks = indexpage.getBrokenLinks();

    Assert.assertTrue(brokenLinks.isEmpty(),
            "Broken links found: " + String.join(", ", brokenLinks));
}
@Test(description = "Verify navigation to homepage via logo")
public void testNavigationToHomeViaLogo() {
    homepage = new HomePage(getDriver());
    homepage.clickLogo();
    Assert.assertEquals(getDriver().getCurrentUrl(), 
            "https://www.silhouettedesignstore.com/");
}



@AfterMethod
public void tearDown() {
	if (getDriver() != null) {
    	getDriver().quit();
        System.out.println("✅ Browser closed after all tests");
}}}