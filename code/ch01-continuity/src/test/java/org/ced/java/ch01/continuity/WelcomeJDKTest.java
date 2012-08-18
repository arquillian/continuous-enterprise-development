package org.ced.java.ch01.continuity;

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

/**
 * Test cases for {@link WelcomeBean} using JDK constructs
 *
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
public class WelcomeJDKTest {

    /** WelcomeBean instance to be tested **/
    private final WelcomeBean welcomer;

    private WelcomeJDKTest(WelcomeBean welcomer) {
        this.welcomer = welcomer;
    }

    public static void main(String... args) {

        /** Make a test client, then execute its tests **/
        WelcomeJDKTest tester = new WelcomeJDKTest(new WelcomeBean());
        tester.testWelcome();
        tester.testWelcomeRequiresInput();

    }

    private void testWelcome() {
        String name = "ALR";
        String expectedResult = "Hello, " + name;
        String receivedResult = welcomer.welcome(name);
        if (!expectedResult.equals(receivedResult)) {
            throw new AssertionError("Did not welcome " + name + " correctly");
        }
    }

    private void testWelcomeRequiresInput() {
        boolean gotExpectedException = false;
        try {
            welcomer.welcome(null);
        } catch (IllegalArgumentException iae) {
            gotExpectedException = true;
        }
        if (!gotExpectedException) {
            throw new AssertionError("Should not accept null input");
        }
    }

}
