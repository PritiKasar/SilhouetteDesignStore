/**
 * 
 */
package com.mystore.page;

/**
 * Common contract for both AccountCreationPopup and AccountCreationPage
 */
public interface Registration {
	void enterFirstName(String Fname);
	void enterLastName(String Lname);
	void enterEmail(String email);
	void enterPassword(String password);
	void enterConfirmPassword(String confirmpassword);
	void clickCreateAccount();
	
	boolean isCreateAccountButtonDisplayed();
	boolean isUserRegistered();
	
	  boolean isErrorDisplayed();
	    String getErrorMessageText();
	    boolean waitForErrorDisplayed(int timeoutSeconds);
		boolean isSuccessMessageDisplayed();
		String getSuccessMessageText();
		boolean isrequiredErrorDisplayed();
		String getrequiredErrorMessageText();
		

}
