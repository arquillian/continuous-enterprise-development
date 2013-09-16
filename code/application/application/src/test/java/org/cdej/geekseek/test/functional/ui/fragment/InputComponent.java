package org.cdej.geekseek.test.functional.ui.fragment;


import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InputComponent {

    @Root
    private WebElement root;

    @FindBy(tagName = "input")
    private WebElement input;

    @FindBy(css = ".has-error")
    private WebElement error;

    public InputComponent value(String value) {
        input.sendKeys(value);
        return this;
    }

    public boolean hasError() {
        try {
            return error.isDisplayed();
        }
        catch(NoSuchElementException e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return error.getText();
    }
}
