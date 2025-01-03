package com.project.pages;

import com.project.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BookPage {
    public BookPage() {

        PageFactory.initElements(Driver.get(), this);
    }


    @FindBy(xpath="//*[@id=\"tbl_books_filter\"]/label/input")
    public WebElement searchField;

    @FindBy(xpath="//*[@id=\"tbl_books\"]/tbody/tr/td[2]")
    public WebElement bookISBN;

    @FindBy(xpath="//*[@id=\"tbl_books\"]/tbody/tr/td[3]")
    public WebElement bookName;

    @FindBy(xpath="//*[@id=\"tbl_books\"]/tbody/tr/td[4]")
    public WebElement bookAuthor;

    @FindBy(xpath="//*[@id=\"tbl_books\"]/tbody/tr/td[5]")
    public WebElement bookCategory;

    @FindBy(xpath="//*[@id=\"tbl_books\"]/tbody/tr/td[6]")
    public WebElement bookYear;

    @FindBy(xpath="//select[@id=\"book_categories\"]")
    public WebElement categoryDropDown;
}
