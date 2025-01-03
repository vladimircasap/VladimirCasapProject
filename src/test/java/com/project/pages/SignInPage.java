package com.project.pages;

import com.project.utilities.BrowserUtils;
import com.project.utilities.Driver;
import com.project.utilities.LibraryUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class SignInPage {

    public SignInPage() {
        PageFactory.initElements(Driver.get(), this);
    }

    @FindBy(id="inputEmail")
    public WebElement emailField;

    @FindBy(id = "inputPassword")
    public WebElement passwordField;

    @FindBy(xpath = "//button[.='Sign in']")
    public WebElement signInButton;



    public void login(String role) {

        // Get Credentials
        Map<String, String> roleCredentials = LibraryUtil.returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        // login
        login(email,password);

    }

    public void login(String email,String password) {

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        BrowserUtils.waitFor(1);
        signInButton.click();

    }
}
