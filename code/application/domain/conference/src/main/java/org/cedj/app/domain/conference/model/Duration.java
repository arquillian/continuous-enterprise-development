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

import java.util.Date;

import javax.validation.constraints.NotNull;

public class Duration {

    @NotNull
    private Date start;

    @NotNull
    private Date end;

    // hidden constructor for Persistence
    Duration() {
    }

    public Duration(Date start, Date end) {
        if (start == null) {
            throw new IllegalArgumentException("Start must be provided");
        }
        if (end == null) {
            throw new IllegalArgumentException("End must be provided");
        }
        if (end.before(start)) {
            throw new IllegalArgumentException("End can not be before Start");
        }
        this.start = start;
        this.end = end;
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
