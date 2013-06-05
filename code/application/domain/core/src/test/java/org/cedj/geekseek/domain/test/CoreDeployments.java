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
package org.cedj.geekseek.domain.test;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence10.PersistenceDescriptor;

public class CoreDeployments {
    public static JavaArchive core() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(Identifiable.class.getPackage())
            .addPackage(Repository.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static PersistenceDescriptor persistence() {
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
