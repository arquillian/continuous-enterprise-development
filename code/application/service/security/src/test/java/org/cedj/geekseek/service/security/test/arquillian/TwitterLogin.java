package org.cedj.geekseek.service.security.test.arquillian;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class TwitterLogin {

    private static final String PROP_USER_ID = "test.twitter.userid";
    private static final String PROP_USER_PASSWORD = "test.twitter.password";

    private String username;
    private String password;

    public TwitterLogin() {
        this.username = System.getProperty(PROP_USER_ID);
        this.password = System.getProperty(PROP_USER_PASSWORD);
        if(this.username == null || this.password == null) {
            throw new IllegalStateException(
                "Both " + PROP_USER_ID + " and " + PROP_USER_PASSWORD + " must be set to " +
                "perform test logins");
        }
    }

    public String login(String url) {
        String sessionId;
        WebDriver driver = null;
        try {
            driver = new HtmlUnitDriver();
            driver.navigate().to(url);

            WebElement form = driver.findElement(By.id("oauth_form"));
            WebElement userField = form.findElement(By.id("username_or_email"));
            WebElement passwordField = form.findElement(By.id("password"));

            WebElement submit = form.findElement(By.id("allow"));

            userField.sendKeys(username);
            passwordField.sendKeys(password);

            submit.click();

            Cookie session = driver.manage().getCookieNamed("JSESSIONID");
            sessionId = session.getValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(driver != null) {
                driver.close();
            }
        }
        return sessionId;
    }
}
