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
package org.cedj.geekseek.domain.conference;

import java.util.Collection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Typed;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.cedj.geekseek.domain.SearchableRepository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.ConferenceCriteria;
import org.cedj.geekseek.domain.persistence.PersistenceRepository;

/**
 * This EJB is @Typed to a specific type to avoid being picked up by
 * CDI under Repository<Conference> due to limitations/error in the CDI EJB
 * interactions. A EJB Beans is always resolved as Repository<T>, which means
 * two EJBs that implements the Repository interface both respond to
 * the InjectionPoint @Inject Repository<X> and making the InjectionPoint
 * ambiguous.
 *
 * As a WorkAround we wrap the EJB that has Transactional properties in CDI bean
 * that can be used by the Type system. The EJB is to be considered a internal
 * implementation detail. The CDI Type provided by the ConferenceCDIDelegateRepository
 * is the real Repository api.
 */
@Stateless
@LocalBean
@Typed(ConferenceRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ConferenceRepository extends PersistenceRepository<Conference>
    implements SearchableRepository<Conference, ConferenceCriteria> {

    public ConferenceRepository() {
        super(Conference.class);
    }

    @Override
    public Collection<Conference> search(ConferenceCriteria criteria) {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Conference> cq = cb.createQuery(Conference.class);
        Root<Conference> root = cq.from(Conference.class);

        Path<String> name = root.get("name");

        if(criteria.getName() != null) {
            cq.where(cb.like(name, "*" + criteria.getName() + "*"));
        }
        return getManager().createQuery(cq)
            .setFirstResult(criteria.getFirstResult())
            .setMaxResults(criteria.getMaxResult()).getResultList();
    }
}
