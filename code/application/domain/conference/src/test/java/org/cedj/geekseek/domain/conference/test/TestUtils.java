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
package org.cedj.geekseek.domain.conference.test;

import java.util.Calendar;
import java.util.Date;

public final class TestUtils {

    private TestUtils() {
    }

    public static Date toDate(int year, int month, int day) {
        return toDate(year, month, day, 0, 0, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int min) {
        return toDate(year, month, day, hour, min, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, min, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
