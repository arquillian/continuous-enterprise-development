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

import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;

@Entity
public class Session extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Embedded
    @NotNull
    @Valid
    private Duration duration;

    @NotNull
    private String title;

    @Lob
    private String outline;

    @ManyToOne
    private Conference conference;

    public Session() {
        super(UUID.randomUUID().toString());
    }

    public Duration getDuration() {
        return duration;
    }

    public Session setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Session setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOutline() {
        return outline;
    }

    public Session setOutline(String outline) {
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
