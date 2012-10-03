package org.ced.web.conference.test.component;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConferenceView {

	@Root
	@SuppressWarnings("unused")
	private WebElement root;
	
	@FindBy(xpath = "//dt[@id='name']/following-sibling::dd[1]")
	private WebElement name;

	@FindBy(xpath = "//dt[@id='tagline']/following-sibling::dd[1]")
	private WebElement tagline;

	public String getName() {
		return name.getText();
	}
	
	public String getTagline() {
		return tagline.getText();
	}
}
