package org.cedj.geekseek.test.functional.ui.fragment;

import java.util.List;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ActionLinks {

    @Root
    private WebElement root;

    @FindBy(tagName = "button")
    private List<WebElement> buttons;

    public WebElement getLink(String name) {
        for(WebElement elem : buttons) {
            if(elem.getText().contains(name) && elem.isDisplayed()) {
                return elem;
            }
        }
        return null;
    }

    public boolean hasLink(String name) {
        return getLink(name) != null;
    }
}
