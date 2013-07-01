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
package org.cedj.geekseek.domain.conference.test.integration;

import static org.cedj.geekseek.domain.conference.test.TestUtils.createConference;
import static org.cedj.geekseek.domain.conference.test.TestUtils.createSession;

import java.io.File;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import junit.framework.Assert;

import org.cedj.geekseek.domain.Created;
import org.cedj.geekseek.domain.Removed;
import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.ConferenceRepository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.conference.test.TestUtils;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@Transactional(TransactionMode.COMMIT)
@RunWith(Arquillian.class)
public class ConferenceTestCase {

    // Given
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                ConferenceDeployments.conference().addClasses(ConferenceTestCase.class, TestUtils.class)
                    .addAsManifestResource(new StringAsset(
                        CoreDeployments.persistence().exportAsString()), "persistence.xml")
                    .addAsManifestResource(new File("src/main/resources/META-INF/beans.xml")))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // these fields are static because Events observed by this TestClass are not
    // are not observed on the same TestClass instance as @Test is running.
    private static boolean createdEventFired = false;
    private static boolean removedEventFired = false;

    @After
    public void cleanUpEventState() {
        createdEventFired = false;
        removedEventFired = false;
    }

    @Inject
    private Repository<Conference> repository;

    // Story: As a User I should be able to create a Conference

    @Test
    @ShouldMatchDataSet(value = { "conference.yml" }, excludeColumns = { "*id" })
    public void shouldBeAbleToCreateConference() {

        Conference conference = createConference();

        repository.store(conference);
        Assert.assertTrue(createdEventFired);
    }

    // Story: As a User I should be able to create a Conference with a Session

    @Test
    @ShouldMatchDataSet(value = { "conference.yml", "session.yml" }, excludeColumns = { "*id" })
    public void shouldBeAbleToCreateConferenceWithSession() {

        Conference conference = createConference();
        conference.addSession(createSession());

        repository.store(conference);
    }

    // Story: As a User I should be able to add a Session to a existing Conference

    @Test
    @UsingDataSet("conference.yml")
    @ShouldMatchDataSet(value = { "conference.yml", "session.yml" }, excludeColumns = { "*id" })
    public void shouldBeAbleToAddSessionToConference() {

        Conference conference = repository.get("CA");
        conference.addSession(createSession());

        repository.store(conference);
    }

    // Story: As a User I should be able to remove a Conference

    @Test
    @UsingDataSet("conference.yml")
    @ShouldMatchDataSet("conference_empty.yml")
    public void shouldBeAbleToRemoveConference() {

        Conference conference = repository.get("CA");

        repository.remove(conference);
        Assert.assertTrue(removedEventFired);
    }

    // Story: As a User I should be able to remove a Session from a Conference

    @Test
    @UsingDataSet({ "conference.yml", "session.yml" })
    @ShouldMatchDataSet({ "conference.yml", "session_empty.yml" })
    public void shouldBeAbleToRemoveConferenceWithSession() {

        Conference conference = repository.get("CA");
        Session session = conference.getSessions().toArray(new Session[0])[0];
        conference.removeSession(session);

        repository.store(conference);
    }

    // Story: As a User I should be able to change a Conference

    @Test
    @UsingDataSet("conference.yml")
    @ShouldMatchDataSet(value = { "conference_updated.yml" })
    public void shouldBeAbleToChangeConference() {

        Conference conference = repository.get("CA");
        conference.setName("UPDATED");

        repository.store(conference);
    }

    // Story: As a User I should be able to change a Session

    @Test
    @UsingDataSet({ "conference.yml", "session.yml" })
    @ShouldMatchDataSet(value = { "conference.yml", "session_updated.yml" })
    public void shouldBeAbleToChangeSession() {

        Conference conference = repository.get("CA");
        conference.getSessions().toArray(new Session[0])[0].setTitle("UPDATED");

        repository.store(conference);
    }

    public void createdEventFired(@Observes @Created Conference conference) {
        createdEventFired = true;
    }

    public void removedEventFired(@Observes @Removed Conference conference) {
        removedEventFired = true;
    }
}
