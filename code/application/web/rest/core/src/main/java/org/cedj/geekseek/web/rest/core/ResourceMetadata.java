package org.cedj.geekseek.web.rest.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceMetadata {

    private Class<?> model;
    private List<Relation> incoming;
    private List<NamedRelation> outgoing;

    public ResourceMetadata(Class<?> model) {
        this.incoming = new ArrayList<Relation>();
        this.outgoing = new ArrayList<NamedRelation>();
        this.model = model;
    }

    public ResourceMetadata incoming(Relation relation) {
        this.incoming.add(relation);
        return this;
    }

    public ResourceMetadata outgoing(NamedRelation relation) {
        this.outgoing.add(relation);
        return this;
    }

    public Collection<NamedRelation> match(ResourceMetadata source) {
        List<NamedRelation> relations = new ArrayList<NamedRelation>();
        for(NamedRelation sourceRel : source.outgoing) {
            for(Relation targetRel : this.incoming) {
                if(targetRel.getType().equals(sourceRel.getType())) {
                    relations.add(sourceRel);
                }
            }
        }
        return relations;
    }

    public Class<?> getModel() {
        return model;
    }

    public static class Relation {
        private String type;

        public Relation(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public static class NamedRelation extends Relation {
        private String name;

        public NamedRelation(String name, String type) {
            super(type);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
