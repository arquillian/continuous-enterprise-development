/*
 * Licensed by the authors under the Creative Commons
 * Attribution-ShareAlike 2.0 Generic (CC BY-SA 2.0)
 * License:
 *
 * http://creativecommons.org/licenses/by-sa/2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cedj.geekseek.web.conference.test.integration;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.cedj.geekseek.web.conference.test.model.ConferenceView;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Ignore
@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ConferenceTestCase {

    @Deployment
    public static WebArchive create() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackages(false, Identifiable.class.getPackage(), BaseEntity.class.getPackage())
            .addPackage(Conference.class.getPackage())
            .addAsWebResource(new File("src/main/webapp/conference.xhtml"))
            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
            .addAsWebInfResource(new File("src/test/resources/beans.xml"));

        // System.out.println(war.toString(Formatters.VERBOSE));

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
        Conference conference = new Conference("Test Conference", "Tag", new Duration(new Date(), new Date()));
        execute(conference);

        Assert.assertEquals(conference.getName(), conferenceView.getName());
        Assert.assertEquals(conference.getTagLine(), conferenceView.getTagline());
    }

    @Test
    public void shouldBeAbleToRenderFragment2() throws Exception {
        Conference conference = new Conference("Test Conference 2", "Tag 2", new Duration(new Date(), new Date()));

        execute(conference);

        Assert.assertEquals(conference.getName(), conferenceView.getName());
        Assert.assertEquals(conference.getTagLine(), conferenceView.getTagline());
    }

    private void execute(Conference conference) throws Exception {

        final URL pageUrl = new URL(baseUrl, "conference.jsf");

        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                driver.get(pageUrl.toExternalForm());
                System.out.println(driver.getPageSource());
            }
        }).inspect(new SetupConference(conference));
    }

    public static class SetupConference extends Inspection {
        private static final long serialVersionUID = 1L;

        private Conference conference;

        public SetupConference(Conference conference) {
            this.conference = conference;
        }

        /*
        @BeforeServlet
        public void create(TestConferenceProducer producer) {
            producer.setConference(conference);
        }
        */
    }
}