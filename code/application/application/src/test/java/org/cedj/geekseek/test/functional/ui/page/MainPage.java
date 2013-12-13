package org.cedj.geekseek.test.functional.ui.page;

import org.cedj.geekseek.test.functional.ui.fragment.ActionLinks;
import org.jboss.arquillian.graphene.enricher.PageFragmentEnricher;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("app/")
public class MainPage {

    @FindBy(id = "action-links")
    private ActionLinks actionLinks;

    @FindBy(id = "user-action-links")
    private ActionLinks userActionLinks;

    @FindBy(id = "resource")
    private WebElement resource;

    public ActionLinks getActionLinks() {
        return actionLinks;
    }

    public ActionLinks getUserActionLinks() {
        return userActionLinks;
    }

    public <T extends SelfAwareFragment> boolean isResource(Class<T> fragment) {
        try {
            return getResource(fragment).is();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public <T extends SelfAwareFragment> T getResource(Class<T> fragment) {
        return PageFragmentEnricher.createPageFragment(fragment, resource);
    }
}
