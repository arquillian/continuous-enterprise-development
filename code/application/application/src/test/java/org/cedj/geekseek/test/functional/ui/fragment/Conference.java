package org.cedj.geekseek.test.functional.ui.fragment;

import java.util.List;

import org.cedj.geekseek.test.functional.ui.page.SelfAwareFragment;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Conference {

    public static class Form implements SelfAwareFragment {
        @Root
        private WebElement root;

        @FindBy(css = ".content.conference")
        private WebElement conference;

        @FindBy(tagName = "form")
        private WebElement form;

        @FindBy(css = "#name")
        private InputComponent name;
        @FindBy(css = "#tagLine")
        private InputComponent tagLine;
        @FindBy(css = "#start")
        private InputComponent start;
        @FindBy(css = "#end")
        private InputComponent end;

        @FindBy(tagName = "button")
        private List<WebElement> buttons;

        @Override
        public boolean is() {
            return conference.isDisplayed() && form.isDisplayed();
        }

        public Form name(String name) {
            this.name.value(name);
            return this;
        }

        public InputComponent name() {
            return name;
        }

        public Form tagLine(String tagLine) {
            this.tagLine.value(tagLine);
            return this;
        }

        public InputComponent tagLine() {
            return tagLine;
        }

        public Form start(String start) {
            this.start.value(start);
            return this;
        }

        public InputComponent start() {
            return start;
        }

        public Form end(String end) {
            this.start.value(end);
            return this;
        }

        public InputComponent end() {
            return end;
        }

        public void submit() {
            for(WebElement button : buttons) {
                if(button.isDisplayed()) {
                    button.click();
                    break;
                }
            }
        }
    }

    public static class ConferenceView {

    }

    public static class ConferenceList {

    }
}
