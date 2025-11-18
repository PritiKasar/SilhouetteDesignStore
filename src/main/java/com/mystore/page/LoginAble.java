package com.mystore.page;

/**
 * Contract for all login-capable UI components (popup or full page).
 * Ensures consistent interactions across different login flows.
 */
public interface LoginAble {

    void enterEmail(String email);
    void enterPassword(String password);
    void clickSignIn();

    boolean isSignInButtonDisplayed();
    boolean isUserLoggedIn();

    boolean isErrorDisplayed();
    String getErrorMessageText();
    boolean waitForErrorDisplayed(int timeoutSeconds);
}
