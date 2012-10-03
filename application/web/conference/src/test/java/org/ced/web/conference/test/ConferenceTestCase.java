package org.ced.web.conference.test;

import java.io.File;
import java.net.URL;

import org.ced.domain.Identifiable;
import org.ced.domain.conference.model.Conference;
import org.ced.web.conference.ConferenceBean;
import org.ced.web.conference.Current;
import org.ced.web.conference.test.component.ConferenceView;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.ClientAction;
import org.jboss.arquillian.warp.ServerAssertion;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.extension.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ConferenceTestCase {

	@Deployment
	public static WebArchive create() {
		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(Identifiable.class, ConferenceBean.class, Current.class, TestConferenceProducer.class)
				.addPackage(Conference.class.getPackage())
				.addAsWebResource(new File("src/main/webapp/conference.xhtml"))
				.addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
				.addAsWebInfResource(new File("src/test/resources/beans.xml"));

		//System.out.println(war.toString(Formatters.VERBOSE));

		return war;
	}

	@FindBy(id = "conference")
	private ConferenceView conferenceView;

	@Drone
	private WebDriver driver;

	@ArquillianResource
	private URL baseUrl;

	@Test
	public void shouldBeAbleToRenderFragment() throws Exception {
		Conference conference = new Conference()
									.setName("Test Conference")
									.setTagLine("Tag");
		execute(conference);

		Assert.assertEquals(conference.getName(), conferenceView.getName());
		Assert.assertEquals(conference.getTagLine(), conferenceView.getTagline());
	}
	
	@Test
	public void shouldBeAbleToRenderFragment2() throws Exception {
		Conference conference = new Conference()
									.setName("Test Conference 2")
									.setTagLine("Tag 2");

		execute(conference);

		Assert.assertEquals(conference.getName(), conferenceView.getName());
		Assert.assertEquals(conference.getTagLine(), conferenceView.getTagline());
	}

	private void execute(Conference conference) throws Exception {
		
		final URL pageUrl = new URL(baseUrl, "conference.jsf");

		Warp.execute(new ClientAction() {
			@Override
			public void action() {
				driver.get(pageUrl.toExternalForm());
				System.out.println(driver.getPageSource());
			}
		}).verify(new SetupConference(conference));
	}
	
	public static class SetupConference extends ServerAssertion {
		private static final long serialVersionUID = 1L;

		private Conference conference;
		
		public SetupConference(Conference conference) {
			this.conference = conference;
		}
		
		@BeforeServlet
		public void create(TestConferenceProducer producer) {
			producer.setConference(conference);
		}
	}
}