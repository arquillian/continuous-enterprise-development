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
package org.ced.java.ch01.continuity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link WelcomeBean} using JUnit
 *
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
public class WelcomeJUnitTest {

    /** To be set by the {@link Before} lifecycle method **/
    private WelcomeBean welcomer;

    /** Called by JUnit before each {@link Test} method **/
    @Before
    public void makeWelcomer() {
        this.welcomer = new WelcomeBean();
    }

    @Test
    public void welcome() {
        final String name = "ALR";
        final String expectedResult = "Hello, " + name;
        final String receivedResult = welcomer.welcome(name);
        Assert.assertEquals("Did not welcome " + name + " correctly", expectedResult, receivedResult);
    }

    @Test
    public void welcomeRequiresInput() {
        boolean gotExpectedException = false;
        try {
            welcomer.welcome(null);
        } catch (final IllegalArgumentException iae) {
            gotExpectedException = true;
        }
        Assert.assertTrue("Should not accept null input", gotExpectedException);
    }
}
