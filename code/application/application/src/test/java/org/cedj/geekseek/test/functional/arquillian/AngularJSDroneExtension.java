package org.cedj.geekseek.test.functional.arquillian;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.Enhancer;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class AngularJSDroneExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(Enhancer.class, AngularJSEnhancer.class);
    }

    public static class AngularJSEnhancer implements Enhancer<WebDriver> {

        private WebDriverEventListener listener;

        @Override
        public int getPrecedence() {
            return 0;
        }

        @Override
        public boolean canEnhance(Class<?> type, Class<? extends Annotation> qualifier) {
            return WebDriver.class.isAssignableFrom(type);
        }

        @Override
        public WebDriver enhance(WebDriver instance, Class<? extends Annotation> qualifier) {
            //System.out.println("Adding AngularJS capabilities to WebDriver");
            instance.manage().timeouts().setScriptTimeout(2, TimeUnit.SECONDS);
            EventFiringWebDriver driver = new EventFiringWebDriver(instance);
            WebDriverEventListener listener = new AngularJSEventHandler();
            driver.register(listener);
            return driver;
        }

        @Override
        public WebDriver deenhance(WebDriver enhancedInstance, Class<? extends Annotation> qualifier) {
            if(EventFiringWebDriver.class.isInstance(enhancedInstance)) {
                //System.out.println("Removing AngularJS capabilities to WebDriver");
                EventFiringWebDriver driver = (EventFiringWebDriver)enhancedInstance;
                driver.unregister(listener);
                listener = null;
                return driver.getWrappedDriver();
            }
            return enhancedInstance;
        }
    }

    public static class AngularJSEventHandler extends AbstractWebDriverEventListener {

        @Override
        public void afterNavigateTo(String url, WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterNavigateBack(WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterNavigateForward(WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterClickOn(WebElement element, WebDriver driver) {
            waitForLoad(driver);
        }

        private void waitForLoad(WebDriver driver) {
            if(JavascriptExecutor.class.isInstance(driver)) {
                JavascriptExecutor executor = (JavascriptExecutor)driver;
                executor.executeAsyncScript(
                    "var callback = arguments[arguments.length - 1];" +
                    "var el = document.querySelector('body');" +
                    "if (window.angular) {" +
                        "angular.element(el).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);" +
                    "} else {callback()}");
            }
        }

    }
}
