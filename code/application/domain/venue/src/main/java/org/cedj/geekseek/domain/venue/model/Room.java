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
package org.cedj.geekseek.domain.venue.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;

@Entity
public class Room extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    public Room() {
        super(UUID.randomUUID().toString());
    }

    public String getName() {
        return name;
    }
}
