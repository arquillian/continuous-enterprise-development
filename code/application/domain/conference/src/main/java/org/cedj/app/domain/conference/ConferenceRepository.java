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
package org.cedj.app.domain.conference;

import javax.ejb.Stateless;

import org.cedj.app.domain.Repository;
import org.cedj.app.domain.conference.model.Conference;

@Stateless
public class ConferenceRepository extends Repository<Conference> {

    public ConferenceRepository() {
        super(Conference.class);
    }
}
