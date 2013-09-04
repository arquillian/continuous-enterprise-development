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
package org.cedj.geekseek.domain.attachment.model;

import static org.cedj.geekseek.domain.util.Validate.requireNonNull;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.model.Timestampable;

public class Attachment implements Identifiable, Timestampable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;

    private final String title;

    private final String mimeType;

    private final URL url;

    private final Date created;

    private final Date updated;

    public Attachment(String title, String mimeType, URL url) {
        this(UUID.randomUUID().toString(), title, mimeType, url, new Date());
    }

    private Attachment(String id, String title, String mimeType, URL url, Date created) {
        requireNonNull(title, "Title must be specified)");
        requireNonNull(mimeType, "MimeType must be specified)");
        requireNonNull(url, "Url must be specified)");
        this.id = id;
        this.created = created;
        this.updated = new Date();
        this.title = title;
        this.mimeType = mimeType;
        this.url = url;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Attachment setTitle(String title) {
        return new Attachment(this.id, title, this.mimeType, this.url, this.created);
    }

    public String getMimeType() {
        return mimeType;
    }

    public Attachment setMimeType(String mimeType) {
        return new Attachment(this.id, this.title, mimeType, this.url, this.created);
    }

    public URL getUrl() {
        return url;
    }

    public Attachment setUrl(URL url) {
        return new Attachment(this.id, this.title, this.mimeType, url, this.created);
    }

    public Date getLastUpdated() {
        return updated == null ? null:(Date)updated.clone();
    }

    @Override
    public Date getCreated() {
        return created == null ? null:(Date)created.clone();
    }

    @Override
    public Date getLastModified() {
        return getLastUpdated() == null ? getCreated():getLastUpdated();
    }
}
