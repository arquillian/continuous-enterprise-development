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
package org.cedj.app.web.conference.test.component;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConferenceView {

    @Root
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
