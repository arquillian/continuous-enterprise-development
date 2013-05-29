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
package org.cedj.app.domain.conference.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

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
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
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

    void setConference(Conference conference) {
        this.conference = conference;
    }
}
