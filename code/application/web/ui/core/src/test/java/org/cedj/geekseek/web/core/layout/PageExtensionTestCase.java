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
package org.cedj.geekseek.web.core.layout;

import java.io.File;

import org.cedj.geekseek.web.core.layout.Attachment;
import org.cedj.geekseek.web.core.layout.PageExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PageExtensionTestCase {

    @Deployment(testable = false)
    public static WebArchive create() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(PageExtension.class, Attachment.class)
            // core
            .addAsWebResource(new File("src/main/webapp/page.xhtml"))
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addClass(TestAttachment.class)
            .addAsWebResource(new File("src/test/resources/jsf/page_fragment.xhtml"));

        System.out.println(war.toString(Formatters.VERBOSE));

        return war;
    }

    @Test
    public void shouldBeAbleToIncludeDynamicFragments() throws Exception {
        System.out.println("break-point");
    }
}
