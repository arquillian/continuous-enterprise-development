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
package org.cedj.geekseek.domain.relation.test;

import java.io.File;

import org.cedj.geekseek.domain.relation.RelationRepository;
import org.cedj.geekseek.domain.relation.model.Relation;
import org.cedj.geekseek.domain.relation.neo.GraphDatabaseProducer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class RelationDeployments {

    public static JavaArchive relation() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(false,
                Relation.class.getPackage(),
                RelationRepository.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static JavaArchive relationWithNeo() {
        return relation()
                .addPackage(GraphDatabaseProducer.class.getPackage());
    }

    public static File[] neo4j() {
        long start = System.currentTimeMillis();
        File[] result = Maven.resolver()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.neo4j:neo4j")
                    .withTransitivity()
                    .asFile();
        System.out.println("Neo4j Resovled in " + (System.currentTimeMillis() - start) + " ms");
        return result;
    }
}
