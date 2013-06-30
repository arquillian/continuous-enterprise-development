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
package org.cedj.geekseek.domain.conference.test;

import static org.cedj.geekseek.domain.conference.test.TestUtils.toDate;

import java.util.Calendar;
import java.util.Date;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;

public final class TestUtils {

    private TestUtils() {
    }

    public static Date toDate(int year, int month, int day) {
        return toDate(year, month, day, 0, 0, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int min) {
        return toDate(year, month, day, hour, min, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, min, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Conference createConference() {
        Conference conference = new Conference();
        conference.setName("Devoxx Belgium 2013");
        conference.setTagLine("We Code In Peace");
        conference.setDuration(new Duration(toDate(2013, 11, 11), toDate(2013, 11, 15)));
        return conference;
    }

    public static Session createSession() {
        Session session = new Session();
        session.setTitle("Testing the Enterprise layers - The A, B, C’s of integration testing");
        session
            .setOutline("For years we’ve been exploring how to layer and separate our code to test in isolation on the unit level. We’ve kept integration and functional testing as a big ball of mud; jumping straight from unit to full system testing. But can we apply some of the same lessons learned from unit to integration testing?\\n\\nThis session explore the different technologies within the Java Enterprise specification and see how our application can be tested in isolation; layer for layer, module for module and component for component.\\n\\nCan we isolate and stay real at the same time? Does mocks, stubs and test doubles have a place in the world of integration testing? Are there other lessons to be learned?");
        session.setDuration(new Duration(toDate(2013, 11, 11, 15, 00), toDate(2013, 11, 11, 16, 00)));
        return session;
    }
}
