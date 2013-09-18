package org.cdej.geekseek.test.functional.ui;

import org.cdej.geekseek.test.functional.ui.fragment.ActionLinks;
import org.cdej.geekseek.test.functional.ui.fragment.Conference;
import org.cdej.geekseek.test.functional.ui.page.MainPage;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class AddConferenceStory {

    @Drone
    private WebDriver driver;

    @Test @InSequence(1)
    public void shouldShowErrorMessageOnMissingDatesInConferenceForm(@InitialPage MainPage page) {

        ActionLinks links = page.getActionLinks();
        Assert.assertTrue(
            "Add Conference action should be available",
            links.hasLink("conference"));

        links.getLink("conference").click();

        Assert.assertTrue(
            "Should have been directed to Conference Form",
            page.isResource(Conference.Form.class));

        Conference.Form form = page.getResource(Conference.Form.class);
        form
            .name("Test")
            .tagLine("Tag line")
            .start("")
            .end("")
            .submit();

        Assert.assertFalse("Should not display error", form.name().hasError());
        Assert.assertFalse("Should not display error", form.tagLine().hasError());
        Assert.assertTrue("Should display error on null input", form.start().hasError());
        Assert.assertTrue("Should display error on null input", form.end().hasError());
    }
}
