package listeners;

import org.testng.*;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.mystore.utility.ExtentManager;
import com.mystore.utility.ScreenshotUtil;
import com.mystore.base.BaseClass;

import org.openqa.selenium.WebDriver;

/**
 * ExtentTestListener
 * -------------------
 * TestNG Listener to integrate ExtentReports with Selenium.
 * Provides thread-safe logging, screenshots on failure,
 * and supports parallel test execution.
 */
public class ExtentTestListener extends BaseClass implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);

        getTest().info("🟡 Test Started: **" + testName + "**");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().pass("✅ Test Passed Successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().fail("❌ Test Failed");
        getTest().fail(result.getThrowable());

        String screenshotPath = captureScreenshot(result, true);

        if (screenshotPath != null) {
            try {
                getTest().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            } catch (Exception e) {
                getTest().warning("⚠️ Screenshot attachment failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().skip("⚠️ Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    /**
     * Captures a screenshot using ScreenshotUtil
     */
    private String captureScreenshot(ITestResult result, boolean isFailed) {
        try {
            WebDriver driver = getDriver();
            if (driver == null) return null;

            return ScreenshotUtil.captureScreenshot(
                    driver,
                    result.getMethod().getMethodName(),
                    isFailed
            );
        } catch (Exception e) {
            System.err.println("❌ Screenshot capture failed: " + e.getMessage());
            return null;
        }
    }

    /** Provide access to thread-safe ExtentTest */
    public static ExtentTest getTest() {
        return test.get();
    }
}
