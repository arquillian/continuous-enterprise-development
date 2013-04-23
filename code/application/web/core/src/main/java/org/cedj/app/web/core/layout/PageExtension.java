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
package org.cedj.app.web.core.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class PageExtension {
    @Inject
    private Instance<Attachment> attachments;

    public List<Attachment> getAttachments() {
        List<Attachment> att = new ArrayList<Attachment>();
        Iterator<Attachment> ait = attachments.iterator();
        while (ait.hasNext()) {
            att.add(ait.next());
        }
        return att;
    }

}
