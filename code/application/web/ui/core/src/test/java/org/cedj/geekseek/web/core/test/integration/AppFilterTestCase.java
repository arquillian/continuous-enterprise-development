package org.cedj.geekseek.web.core.test.integration;

import static org.cedj.geekseek.web.core.test.integration.TestUtils.asString;

import java.net.URL;

import junit.framework.Assert;

import org.cedj.geekseek.web.core.servlet.AppFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppFilterTestCase {

    private static final String APP_INDEX_CONTENT = "app_index";
    private static final String APP_RESOURCE_CONTENT = "app_resource";
    private static final String APP_SCRIPT_CONTENT = "script";
    private static final String APP_STYLE_CONTENT = "style";
    private static final String APP_IMAGE_CONTENT = "image";

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(AppFilter.class)
            .addAsWebResource(new StringAsset(APP_INDEX_CONTENT), "app/index.jsp")
            .addAsResource(new StringAsset(APP_RESOURCE_CONTENT), "summary.html")
            .addAsWebResource(new StringAsset(APP_IMAGE_CONTENT), "images/test.png")
            .addAsWebResource(new StringAsset(APP_SCRIPT_CONTENT), "scripts/test.js")
            .addAsWebResource(new StringAsset(APP_STYLE_CONTENT), "styles/test.css");
    }

    @ArquillianResource
    public URL base;

    @Test
    public void shouldLoadResourceFromClasspath() throws Exception {
        String content = asString(new URL(base, "app/summary.html").openStream());
        Assert.assertEquals(APP_RESOURCE_CONTENT, content);
    }

    @Test
    public void shouldRedirectUnknownRequestToIndex() throws Exception {
        String content = asString(new URL(base, "app/jatta").openStream());
        Assert.assertEquals(APP_INDEX_CONTENT, content);
    }

    @Test
    public void shouldLoadScriptResources() throws Exception {
        String content = asString(new URL(base, "app/scripts/test.js").openStream());
        Assert.assertEquals(APP_SCRIPT_CONTENT, content);
    }

    @Test
    public void shouldLoadStyleResources() throws Exception {
        String content = asString(new URL(base, "app/styles/test.css").openStream());
        Assert.assertEquals(APP_STYLE_CONTENT, content);
    }

    @Test
    public void shouldLoadImageResources() throws Exception {
        String content = asString(new URL(base, "app/images/test.png").openStream());
        Assert.assertEquals(APP_IMAGE_CONTENT, content);
    }

}
