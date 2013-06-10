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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;

@Entity
public class Conference extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String tagLine;

    @Embedded
    @Valid
    @NotNull
    private Duration duration;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "conference", cascade = CascadeType.ALL)
    @Valid
    private Set<Session> sessions;

    public Conference() {
        super(UUID.randomUUID().toString());
    }

    public String getName() {
        return name;
    }

    public Conference setName(String name) {
        this.name = name;
        return this;
    }

    public String getTagLine() {
        return tagLine;
    }

    public Conference setTagLine(String tagLine) {
        this.tagLine = tagLine;
        return this;
    }

    public Conference setDuration(Duration duration) {
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
        if (sessions == null) {
            this.sessions = new HashSet<Session>();
        }
        if (!sessions.contains(session)) {
            sessions.add(session);
            session.setConference(this);
        }
        return this;
    }

    public void removeSession(Session session) {
        if (sessions.remove(session)) {
            session.setConference(null);
        }
    }
}
