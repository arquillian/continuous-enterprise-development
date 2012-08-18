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

/**
 * Simple welcome service; used to show elementary testing practices
 *
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
public class WelcomeBean {

    public String welcome(final String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name must be specified");
        }
        return "Hello, " + name;
    }

}
