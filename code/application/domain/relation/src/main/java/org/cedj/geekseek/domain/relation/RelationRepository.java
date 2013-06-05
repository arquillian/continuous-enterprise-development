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
package org.cedj.geekseek.domain.relation;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.model.Relation;

@Stateless
public class RelationRepository {

    @PersistenceContext
    private EntityManager manager;

    public Relation add(Identifiable source, Relation.Type type, Identifiable target) {
        Relation rel = new Relation(source.getId(), target.getId(), type);
        manager.persist(rel);
        return rel;
    }

    // TODO: implement as criteria query ?
    public <T extends Identifiable> List<T> findTargets(Identifiable source, Relation.Type type, Class<T> targetType) {

        StringBuilder q = new StringBuilder();
        q.append("select t from ").append(targetType.getSimpleName()).append(" t , Relation r ");
        q.append("where t.id = r.key.targetId ");
        q.append("and r.key.sourceId = :source ");
        q.append("and r.key.type = :type");

        TypedQuery<T> query = manager.createQuery(q.toString(), targetType);
        query.setParameter("source", source.getId());
        query.setParameter("type", type);

        return query.getResultList();
    }
}
