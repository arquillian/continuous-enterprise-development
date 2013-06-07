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

import java.util.List;

import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.relation.model.Relation;
import org.cedj.geekseek.domain.relation.neo.GraphRelationRepository;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.domain.test.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RelationTestCase {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                RelationDeployments.relationWithNeo(),
                CoreDeployments.core())
            .addAsLibraries(RelationDeployments.neo4j())
            .addPackage(SourceObject.class.getPackage())
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private static final String SOURCE_ID = "11";
    private static final String TARGET_ID = "1";

    private SourceObject source;
    private TargetObject target;
    private String type;

    @Inject
    private GraphRelationRepository repository;

    @Before
    public void createTypes() {
        source = new SourceObject(SOURCE_ID);
        target = new TargetObject(TARGET_ID);
        type = "SPEAKING";
    }

    @Test @InSequence(0)
    public void shouldBeAbleToCreateRelation() {

        Relation relation = repository.add(source, type, target);

        Assert.assertEquals("Verify retuned object has same source id", relation.getSourceId(), source.getId());
        Assert.assertEquals("Verify retuned object has same target id", relation.getTargetId(), target.getId());
        Assert.assertEquals("Verify retuned object has same type", relation.getType(), type);

        Assert.assertNotNull("Verify created date was set", relation.getCreated());
    }

    @Test @InSequence(1)
    public void shouldBeAbleToFindTargetedRelations(Repository<TargetObject> targetRepo, Repository<SourceObject> sourceRepo) {
        targetRepo.store(target);
        sourceRepo.store(source);

        List<TargetObject> tagets = repository.findTargets(source, type, TargetObject.class);

        Assert.assertNotNull("Verify a non null list returned", tagets);
        Assert.assertEquals("Verify expected targets count", 1, tagets.size());

        Assert.assertEquals("Verify expected target returned", TARGET_ID, tagets.get(0).getId());
    }

    @Test @InSequence(2)
    public void shouldBeAbleToDeleteRelations() {

        repository.remove(source, type, target);

        List<TargetObject> tagets = repository.findTargets(source, type, TargetObject.class);
        Assert.assertNotNull("Verify a non null list returned", tagets);
        Assert.assertEquals("Verify expected targets count", 0, tagets.size());
    }

    @Test @InSequence(3)
    public void shouldOnlyFindGivenRelation() {

        repository.add(source, type, target);
        repository.add(source, type + "X", target);

        List<TargetObject> tagets = repository.findTargets(source, type, TargetObject.class);

        Assert.assertNotNull("Verify a non null list returned", tagets);
        Assert.assertEquals("Verify expected targets count", 1, tagets.size());

        Assert.assertEquals("Verify expected target returned", TARGET_ID, tagets.get(0).getId());
    }
}
