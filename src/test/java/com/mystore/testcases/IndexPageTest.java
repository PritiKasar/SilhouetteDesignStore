/**
 * 
 */
package com.mystore.testcases;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mystore.base.BaseClass;
import com.mystore.page.HomePage;

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
	
	HomePage homepage;
@BeforeMethod
	public void setup() {
	launchApp();
}
@Test(description = "Verify the homepage logo", groups = {"sanity", "smoke"})
public void verifyLogo() throws Throwable{
	homepage = new HomePage(getDriver());
	boolean result=homepage.verifyLogo();
	Assert.assertTrue(result);
}
@Test(description = "Verify the homepage title", groups = {"sanity", "smoke"})
public void verifyTitle() {
	homepage = new HomePage(getDriver());

	String actualtitle=homepage.verifyTitle();
	Assert.assertEquals(actualtitle, "Crafting Made Easy with Digital Designs & Fonts", 
            "Page title did not match!");
}
@Test(description = "Verify no broken links on homepage", groups = {"system"})
public void verifyBrokenLinks() {
	homepage = new HomePage(getDriver());
    List<String> brokenLinks = homepage.getBrokenLinks();

    Assert.assertTrue(brokenLinks.isEmpty(),
            "Broken links found: " + String.join(", ", brokenLinks));
}
@Test(description = "Verify navigation to homepage via logo", groups = {"sanity", "smoke"})
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