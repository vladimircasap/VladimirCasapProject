package com.project.pages;

import com.project.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public  class TopNavigationBar {

    public TopNavigationBar() {
        PageFactory.initElements(Driver.get(), this);
    }

    @FindBy(xpath = "//*[@id=\"menu_item\"]/li[1]")
    public WebElement dashboard;

    @FindBy(xpath = "//*[@id=\"menu_item\"]/li[2]")
    public WebElement users;

    @FindBy(xpath = "//*[@id=\"menu_item\"]/li[3]")
    public WebElement books;

    @FindBy(xpath ="//*[@id=\"navbarDropdown\"]/span")
    public WebElement userName;


}
