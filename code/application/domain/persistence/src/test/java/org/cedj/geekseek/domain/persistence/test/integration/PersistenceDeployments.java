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
package org.cedj.geekseek.domain.persistence.test.integration;

import org.cedj.geekseek.domain.persistence.PersistenceRepository;
import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence10.PersistenceDescriptor;

public class PersistenceDeployments {
    public static JavaArchive persistence() {
        return ShrinkWrap.create(JavaArchive.class)
            .merge(model())
            .merge(repository());
    }
    
    public static JavaArchive model() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(BaseEntity.class.getPackage());
    }
    
    public static JavaArchive repository() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(PersistenceRepository.class.getPackage());
    }

    public static PersistenceDescriptor descriptor() {
        return Descriptors.create(PersistenceDescriptor.class)
                .createPersistenceUnit()
                    .name("test")
                    .getOrCreateProperties()
                        .createProperty()
                            .name("hibernate.hbm2ddl.auto")
                            .value("create-drop").up()
                        .createProperty()
                            .name("hibernate.show_sql")
                            .value("true").up().up()
                    .jtaDataSource("java:jboss/datasources/ExampleDS").up();
                    //.jtaDataSource("ExampleDS").up();
    }
}
