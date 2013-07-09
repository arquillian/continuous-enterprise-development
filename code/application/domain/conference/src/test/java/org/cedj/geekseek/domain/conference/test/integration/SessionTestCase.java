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

import java.io.File;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Created;
import org.cedj.geekseek.domain.Removed;
import org.cedj.geekseek.domain.Repository;
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Transactional(TransactionMode.DISABLED)
// our Repository is transactional
@RunWith(Arquillian.class)
public class SessionTestCase {

    // Given
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                ConferenceDeployments.conference().addClasses(SessionTestCase.class, TestUtils.class)
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
    private Repository<Session> repository;

    // Story: As a User I should be able to remove a Session

    @Test
    @UsingDataSet({"conference.yml", "session.yml"})
    @ShouldMatchDataSet({"conference.yml", "session_empty.yml"})
    public void shouldBeAbleToRemoveSession() {

        Session session = repository.get("SA");

        repository.remove(session);
        Assert.assertTrue(removedEventFired);
    }

    // Story: As a User I should be able to change a Session

    @Test
    @UsingDataSet({ "conference.yml", "session.yml" })
    @ShouldMatchDataSet(value = { "conference.yml", "session_updated.yml" })
    public void shouldBeAbleToChangeSession() {

        Session session = repository.get("SA");
        session.setTitle("UPDATED");

        repository.store(session);
        Assert.assertTrue(createdEventFired);
    }

    public void createdEventFired(@Observes @Created Session conference) {
        createdEventFired = true;
    }

    public void removedEventFired(@Observes @Removed Session conference) {
        removedEventFired = true;
    }
}
