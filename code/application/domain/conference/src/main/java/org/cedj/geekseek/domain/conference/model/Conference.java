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
package org.cedj.geekseek.domain.conference.model;

import static org.cedj.geekseek.domain.util.Validate.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;

@Entity
public class Conference extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String tagLine;

    @Embedded
    private Duration duration;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "conference", cascade = CascadeType.ALL)
    private Set<Session> sessions;

    // JPA
    protected Conference() {}

    public Conference(String name, String tagLine, Duration duration) {
        super(UUID.randomUUID().toString());
        requireNonNull(name, "Name must be specified)");
        requireNonNull(tagLine, "TagLine must be specified");
        requireNonNull(duration, "Duration must be specified");
        this.name = name;
        this.tagLine = tagLine;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public Conference setName(String name) {
        requireNonNull(name, "Name must be specified)");
        this.name = name;
        return this;
    }

    public String getTagLine() {
        return tagLine;
    }

    public Conference setTagLine(String tagLine) {
        requireNonNull(tagLine, "TagLine must be specified");
        this.tagLine = tagLine;
        return this;
    }

    public Conference setDuration(Duration duration) {
        requireNonNull(duration, "Duration must be specified");
        this.duration = duration;
        return this;
    }

    public Duration getDuration() {
        return duration;
    }

    public Set<Session> getSessions() {
        if (sessions == null) {
            this.sessions = new HashSet<Session>();
        }
        return Collections.unmodifiableSet(sessions);
    }

    public Conference addSession(Session session) {
        requireNonNull(session, "Session must be specified");
        if (sessions == null) {
            this.sessions = new HashSet<Session>();
        }
        sessions.add(session);
        session.setConference(this);
        return this;
    }

    public void removeSession(Session session) {
        if(session == null) {
            return;
        }
        if (sessions.remove(session)) {
            session.setConference(null);
        }
    }
}
