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
package org.cedj.app.domain.relation.test;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.cedj.app.domain.model.Identifiable;

@Entity
public class SourceObject implements Identifiable {

    @Id
    public String id;

    @SuppressWarnings("unused")
    private SourceObject() {
    } // JPA

    public SourceObject(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
