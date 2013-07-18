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

import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;

@Entity
public class Session extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Embedded
    private Duration duration;

    private String title;

    @Lob
    private String outline;

    @ManyToOne
    private Conference conference;

    // JPA
    protected Session() {}

    public Session(String title, String outline, Duration duration) {
        super(UUID.randomUUID().toString());
        requireNonNull(title, "Title must be specified");
        requireNonNull(outline, "Outline must be specified");
        requireNonNull(duration, "Duration must be specified");
        this.title = title;
        this.outline = outline;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public Session setDuration(Duration duration) {
        requireNonNull(duration, "Duration must be specified");
        this.duration = duration;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Session setTitle(String title) {
        requireNonNull(title, "Title must be specified");
        this.title = title;
        return this;
    }

    public String getOutline() {
        return outline;
    }

    public Session setOutline(String outline) {
        requireNonNull(outline, "Outline must be specified");
        this.outline = outline;
        return this;
    }

    public Conference getConference() {
        return conference;
    }

    void setConference(Conference conference) {
        this.conference = conference;
    }

    @PreRemove
    public void removeConferenceRef() {
        if(conference != null) {
            conference.removeSession(this);
        }
    }
}
