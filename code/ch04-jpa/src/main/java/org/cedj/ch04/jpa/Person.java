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
package org.cedj.ch04.jpa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Represents a {@link Person} in a family with JPA metadata to show relational mapping
 *
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
@Entity
public class Person {

    // Instance members
    private Long id;
    private String name;
    private Boolean male;

    private Person father;
    private Person mother;
    @ManyToOne(fetch=FetchType.LAZY)
    private List<Person> children;

    // Accessors / Mutators
    public Long getId() {
        return id;
    }
    public void setId(final Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public Boolean isMale() {
        return male;
    }
    public void setMale(final Boolean male) {
        this.male = male;
    }
    public Person getFather() {
        return father;
    }
    public void setFather(final Person father) {
        this.father = father;
    }
    public Person getMother() {
        return mother;
    }
    public void setMother(final Person mother) {
        this.mother = mother;
    }
    public List<Person> getChildren() {
        return children;
    }
    public void setChildren(final List<Person> children) {
        this.children = children;
    }
}
