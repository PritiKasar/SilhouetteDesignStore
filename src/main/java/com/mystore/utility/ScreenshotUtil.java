package com.mystore.utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

/**
 * ScreenshotUtil
 * -------------------
 * Captures screenshots on failure or when manually triggered.
 */
public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String methodName, boolean isFailed) {
        try {
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String status = isFailed ? "FAIL" : "INFO";

            String screenshotDir = System.getProperty("user.dir") + "/screenshots/";
            File folder = new File(screenshotDir);
            if (!folder.exists()) folder.mkdirs();

            String filePath = screenshotDir + methodName + "_" + status + "_" + timestamp + ".png";

            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileHandler.copy(source, destination);

            return filePath;

        } catch (IOException e) {
            System.err.println("❌ Failed to save screenshot: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Unexpected screenshot error: " + e.getMessage());
            return null;
        }
    }
}
