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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.cedj.geekseek.domain.model.Identifiable;

@Entity
public class Venue implements Identifiable {

    @Id
    private String id;

    @OneToMany(fetch = FetchType.EAGER)
    @Valid
    private Set<Room> rooms;

    public Venue() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Set<Room> getRooms() {
        return Collections.unmodifiableSet(rooms);
    }

    public Venue addRoom(Room room) {
        if (rooms == null) {
            this.rooms = new HashSet<Room>();
        }
        if (!rooms.contains(room)) {
            rooms.add(room);
        }
        return this;
    }
}
