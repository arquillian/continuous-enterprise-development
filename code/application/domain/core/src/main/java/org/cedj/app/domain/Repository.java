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
package org.cedj.app.domain;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.cedj.app.domain.model.Identifiable;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class Repository<T extends Identifiable> {

    @PersistenceContext
    private EntityManager manager;

    private Class<T> type;

    public Repository(Class<T> type) {
        this.type = type;
    }

    public T store(T entity) {
        T merged = merge(entity);
        manager.persist(merged);
        return merged;
    }

    public T get(String id) {
        return manager.find(type, id);
    }

    public void remove(T entity) {
        manager.remove(merge(entity));
    }

    private T merge(T entity) {
        return manager.merge(entity);
    }

    protected EntityManager getManager() {
        return manager;
    }
}