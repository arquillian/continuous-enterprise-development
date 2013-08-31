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
package org.cedj.geekseek.domain.relation.neo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.RelationRepository;
import org.cedj.geekseek.domain.relation.model.Relation;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.UniqueFactory;

@ApplicationScoped
public class GraphRelationRepository implements RelationRepository {

    private static final String PROP_INDEX_NODE = "all_nodes";
    private static final String PROP_INDEX_REL = "all_relations";
    private static final String PROP_ID = "id";
    private static final String PROP_NODE_CLASS = "_classname";
    private static final String PROP_CREATED = "created";
    private static final String REL_TYPE_ALL = "all";

    @Inject
    private GraphDatabaseService graph;

    @Inject
    private BeanManager manager;

    /* (non-Javadoc)
     * @see org.cedj.geekseek.domain.relation.RelationRepositoryTe#add(org.cedj.geekseek.domain.model.Identifiable, java.lang.String, org.cedj.geekseek.domain.model.Identifiable)
     */
    @Override
    public Relation add(Identifiable source, final String type, Identifiable target) {

        Transaction tx = graph.beginTx();
        try {
            Node root =graph.getNodeById(0);
            String sourceTypeName = source.getClass().getSimpleName();
            String targetTypeName = target.getClass().getSimpleName();
            Node sourceTypeNode = getOrCreateNodeType(sourceTypeName);
            Node targetTypeNode = getOrCreateNodeType(targetTypeName);
            getOrCreateRelationship(root, sourceTypeNode, Named.relation(sourceTypeName));
            getOrCreateRelationship(root, targetTypeNode, Named.relation(targetTypeName));

            Node sourceNode = getOrCreateNode(source, sourceTypeName);
            getOrCreateRelationship(sourceTypeNode, sourceNode, Named.relation(REL_TYPE_ALL));
            Node targetNode = getOrCreateNode(target, targetTypeName);
            getOrCreateRelationship(targetTypeNode, targetNode, Named.relation(REL_TYPE_ALL));

            getOrCreateRelationship(sourceNode, targetNode, Named.relation(type));

            tx.success();
        } catch(Exception e) {
            tx.failure();
            throw new RuntimeException(
                "Could not add relation of type " + type + " between " + source + " and " + target, e);
        } finally {
          tx.finish();
        }
        return new Relation(source.getId(), target.getId(), type);
    }

    @Override
    public void remove(Identifiable source, String type, Identifiable target) {

        Transaction tx = graph.beginTx();
        try {
            Index<Node> nodeIndex = graph.index().forNodes(PROP_INDEX_NODE);
            Index<Relationship> relationIndex = graph.index().forRelationships(PROP_INDEX_REL);

            Node sourceNode = nodeIndex.get(PROP_ID, source.getId()).getSingle();
            Node targetNode = nodeIndex.get(PROP_ID, target.getId()).getSingle();
            for(Relationship rel : sourceNode.getRelationships(Named.relation(type))) {
                if(rel.getEndNode().equals(targetNode)) {
                    rel.delete();
                    relationIndex.remove(rel);
                }
            }

            tx.success();
        } catch(Exception e) {
            tx.failure();
            throw new RuntimeException(
                "Could not add relation of type " + type + " between " + source + " and " + target, e);
        } finally {
          tx.finish();
        }
    }

    /* (non-Javadoc)
     * @see org.cedj.geekseek.domain.relation.RelationRepositoryTe#findTargets(org.cedj.geekseek.domain.model.Identifiable, java.lang.String, java.lang.Class)
     */
    @Override
    public <T extends Identifiable> List<T> findTargets(Identifiable source, final String type, final Class<T> targetType) {

        Repository<T> repo = locateTargetRepository(targetType);
        if(repo == null) {
            throw new RuntimeException("Could not locate a " + Repository.class.getName() + " instance for Type " + targetType.getName());
        }

        List<T> targets = new ArrayList<T>();
        Index<Node> index = graph.index().forNodes(PROP_INDEX_NODE);
        Node node = index.get(PROP_ID, source.getId()).getSingle();
        if(node == null) {
            return targets;
        }
        Transaction tx = graph.beginTx();

        try {
            Iterable<Relationship> relationships = node.getRelationships(Named.relation(type));
            for(Relationship relation : relationships) {
                String targetId = relation.getOtherNode(node).getProperty(PROP_ID).toString();
                T target = repo.get(targetId);
                if(target == null) {
                    // Do some auto clean up if target node not found in Target repo
                    relation.getOtherNode(node).delete();
                    for(Relationship other : relation.getOtherNode(node).getRelationships()) {
                        other.delete();
                    }
                } else {
                    targets.add(target);
                }
            }
            tx.success();
        } catch(Exception e) {
            tx.failure();
            throw new RuntimeException(
                "Could not clean up relation of type " + type + " from " + source, e);
        } finally {
          tx.finish();
        }
        return targets;
    }

    /**
     * Helper method that looks in the BeanManager for a Repository that match signature
     * Repository<T>.
     *
     * Used to dynamically find repository to load targets from.
     *
     * @param targetType Repository object type to locate
     * @return Repository<T>
     */
    private <T extends Identifiable> Repository<T> locateTargetRepository(final Class<T> targetType) {
        ParameterizedType paramType = new ParameterizedType() {
            @Override
            public Type getRawType() {
                return Repository.class;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {targetType};
            }
        };

        Set<Bean<?>> beans = manager.getBeans(paramType);
        Bean<?> bean = manager.resolve(beans);
        CreationalContext<?> cc = manager.createCreationalContext(null);

        @SuppressWarnings("unchecked")
        Repository<T> repo = (Repository<T>)manager.getReference(bean, paramType, cc);
        return repo;
    }

    private Node getOrCreateNodeType(String type) {
        UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graph, PROP_INDEX_NODE) {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty(PROP_ID, properties.get(PROP_ID));
            }
        };
        return factory.getOrCreate(PROP_ID, type);
    }

    private Node getOrCreateNode(Identifiable source, final String nodeClassType) {
        UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graph, PROP_INDEX_NODE) {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty(PROP_ID, properties.get(PROP_ID));
                created.setProperty(PROP_NODE_CLASS, nodeClassType);
            }
        };
        return factory.getOrCreate(PROP_ID, source.getId());
    }

    private Relationship getOrCreateRelationship(final Node source, final Node target, final RelationshipType type) {
        final String key = generateKey(source, target, type);

        UniqueFactory<Relationship> factory = new UniqueFactory.UniqueRelationshipFactory(graph, PROP_INDEX_REL) {

            @Override
            protected Relationship create(Map<String, Object> properties) {
                Relationship rel = source.createRelationshipTo(target, type);
                rel.setProperty(PROP_ID, properties.get(PROP_ID));
                return rel;
            }

            @Override
            protected void initialize(Relationship rel, Map<String, Object> properties) {
                rel.setProperty(PROP_CREATED, System.currentTimeMillis());
            }
        };
        return factory.getOrCreate(PROP_ID, key);
    }

    /**
     * Generate some unique key we can identify a relationship with.
     */
    private String generateKey(Node source, Node target, RelationshipType type) {
        return source.getProperty(PROP_ID, "X") + "-" + type.name() + "-" + target.getProperty(PROP_ID, "X");
    }

    private static class Named implements RelationshipType {

        public static RelationshipType relation(String name) {
            return new Named(name);
        }

        private String name;

        private Named(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }
    }
}
