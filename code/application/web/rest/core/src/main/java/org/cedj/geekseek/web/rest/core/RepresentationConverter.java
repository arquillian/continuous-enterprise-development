package org.cedj.geekseek.web.rest.core;

import java.util.ArrayList;
import java.util.Collection;

public interface RepresentationConverter<REST, SOURCE> {

    Class<REST> getRepresentationClass();

    Class<SOURCE> getSourceClass();

    REST from(SOURCE source);

    Collection<REST> from(Collection<SOURCE> sources);

    SOURCE to(REST representation);

    SOURCE update(REST representation, SOURCE target);

    Collection<SOURCE> to(Collection<REST> representations);


    public abstract static class Base<REST, SOURCE> implements RepresentationConverter<REST, SOURCE> {

        private Class<REST> representationClass;
        private Class<SOURCE> sourceClass;

        public Base(Class<REST> representationClass, Class<SOURCE> sourceClass) {
            this.representationClass = representationClass;
            this.sourceClass = sourceClass;
        }

        @Override
        public Class<REST> getRepresentationClass() {
            return representationClass;
        }

        @Override
        public Class<SOURCE> getSourceClass() {
            return sourceClass;
        }

        @Override
        public Collection<REST> from(Collection<SOURCE> ins) {
            Collection<REST> out = new ArrayList<REST>();
            for(SOURCE in : ins) {
                out.add(from(in));
            }
            return out;
        }

        @Override
        public Collection<SOURCE> to(Collection<REST> ins) {
            Collection<SOURCE> out = new ArrayList<SOURCE>();
            for(REST in : ins) {
                out.add(to(in));
            }
            return out;
        }
    }
}
