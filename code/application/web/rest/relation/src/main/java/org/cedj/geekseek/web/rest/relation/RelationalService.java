package org.cedj.geekseek.web.rest.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cedj.geekseek.web.rest.core.MetadataResource;
import org.cedj.geekseek.web.rest.core.ResourceMetadata;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.NamedRelation;

@ApplicationScoped
public class RelationalService {

    @Inject
    private Instance<MetadataResource> resources;

    private Map<Class<?>, ResourceMetadata> metadata;

    public RelationalService() {
        this.metadata = new HashMap<Class<?>, ResourceMetadata>();
    }

    @PostConstruct
    public void initialize() {
        for(MetadataResource resource : resources) {
            ResourceMetadata meta = resource.getResourceMetadata();
            this.metadata.put(meta.getModel(), meta);
        }
    }

    public Collection<RelationMatch> getMatchingRelations(Class<?> sourceType) {
        List<RelationMatch> result = new ArrayList<RelationMatch>();
        ResourceMetadata sourceMeta = this.metadata.get(sourceType);
        for(ResourceMetadata targetMeta : this.metadata.values()) {
            for(NamedRelation match : targetMeta.match(sourceMeta)) {
                result.add(new RelationMatch(
                    sourceMeta.getModel(), match, targetMeta.getModel()));
            }
        }
        return result;
    }

    public class RelationMatch {
        private Class<?> sourceModel;
        private NamedRelation source;
        private Class<?> targetModel;

        public RelationMatch(Class<?> sourceModel, NamedRelation source, Class<?> targetModel) {
            super();
            this.sourceModel = sourceModel;
            this.source = source;
            this.targetModel = targetModel;
        }

        public Class<?> getSourceModel() {
            return sourceModel;
        }

        public NamedRelation getSource() {
            return source;
        }

        public Class<?> getTargetModel() {
            return targetModel;
        }
    }
}
