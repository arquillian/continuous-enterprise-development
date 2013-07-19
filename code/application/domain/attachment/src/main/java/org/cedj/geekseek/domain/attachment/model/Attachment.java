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

    private String id;

    private String title;

    private String mimeType;

    private URL url;

    private Date created;

    private Date updated;

    protected Attachment() {
        this.id = UUID.randomUUID().toString();
        this.created = new Date();
    }

    public Attachment(String title, String mimeType, URL url) {
        this();
        requireNonNull(title, "Title must be specified)");
        requireNonNull(mimeType, "MimeType must be specified)");
        requireNonNull(url, "Url must be specified)");
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
        requireNonNull(title, "Title must be specified)");
        this.title = title;
        updated();
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Attachment setMimeType(String mimeType) {
        requireNonNull(mimeType, "MimeType must be specified)");
        this.mimeType = mimeType;
        updated();
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public Attachment setUrl(URL url) {
        requireNonNull(url, "Url must be specified)");
        this.url = url;
        updated();
        return this;
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

    private void updated() {
        this.updated = new Date();
    }
}
