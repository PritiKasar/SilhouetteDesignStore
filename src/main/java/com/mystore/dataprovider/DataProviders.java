package com.mystore.dataprovider;

import org.testng.annotations.DataProvider;
import com.mystore.utility.ExcelUtility;

public class DataProviders {

    // Load Excel file ONCE
    ExcelUtility excel = new ExcelUtility(
            System.getProperty("user.dir") + "/TestData/TestData.xlsx"
    );

    @DataProvider(name = "Credentials")
    public Object[][] getCredentials() {

        String sheetName = "Credentials";
        return excel.getSheetData(sheetName);
    }


    @DataProvider(name = "email")
    public Object[][] getEmail() {

        String sheetName = "Email";
        return excel.getSheetData(sheetName);
    }


    @DataProvider(name = "searchProduct")
    public Object[][] getProductPrice() {

        String sheetName = "SearchProduct";
        return excel.getSheetData(sheetName);
    }


    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        return new Object[][] {
                {
                    "John", "Doe1",
                    "johndoe1" + System.currentTimeMillis() + "@test.com",
                    "Test@1234",
                    "success",
                    "✅ Registration successful"
                },
                {
                    "John", "Doe1",
                    "existingemail@test.com",
                    "Test@1234",
                    "error",
                    "❌ Email already exists"
                },
                {
                    "", "", "",
                    "", "requiredError",
                    "⚠ Required fields error"
                }
        };
    }
}
