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
package org.cedj.ch01.continuity;

import org.cedj.ch01.continuity.WelcomeBean;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Test cases for {@link WelcomeBean} using TestNG
 *
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
public class WelcomeTestNGTest {

    /** To be set by the {@link @BeforeTest} lifecycle method **/
    private WelcomeBean welcomer;

    /** Called by TestNG before each {@link Test} method **/
    @BeforeTest
    public void makeWelcomer() {
        this.welcomer = new WelcomeBean();
    }

    @Test
    public void welcome() {
        final String name = "ALR";
        final String expectedResult = "Hello, " + name;
        final String receivedResult = welcomer.welcome(name);
        Assert.assertEquals(receivedResult, expectedResult, "Did not welcome " + name + " correctly");
    }

    @Test
    public void welcomeRequiresInput() {
        boolean gotExpectedException = false;
        try {
            welcomer.welcome(null);
        } catch (final IllegalArgumentException iae) {
            gotExpectedException = true;
        }
        Assert.assertTrue(gotExpectedException, "Should not accept null input");
    }
}
