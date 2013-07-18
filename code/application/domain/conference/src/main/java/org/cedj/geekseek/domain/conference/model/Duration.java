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

import java.io.Serializable;
import java.util.Date;

public class Duration implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date start;

    private Date end;

    // hidden constructor for Persistence
    Duration() {
    }

    public Duration(Date start, Date end) {
        requireNonNull(start, "Start must be specified");
        requireNonNull(end, "End must be specified");
        if (end.before(start)) {
            throw new IllegalArgumentException("End can not be before Start");
        }
        this.start = (Date)start.clone();
        this.end = (Date)end.clone();
    }

    public Date getEnd() {
        return (Date) end.clone();
    }

    public Date getStart() {
        return (Date) start.clone();
    }

    public Integer getNumberOfDays() {
        return -1;
    }

    public Integer getNumberOfHours() {
        return -1;
    }
}
