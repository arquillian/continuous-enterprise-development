package org.cedj.geekseek.web.rest.core;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.UriInfo;

public interface RepresentationConverter<REST, SOURCE> {

    Class<REST> getRepresentationClass();

    Class<SOURCE> getSourceClass();

    REST from(UriInfo uriInfo, SOURCE source);

    Collection<REST> from(UriInfo uriInfo, Collection<SOURCE> sources);

    SOURCE to(UriInfo uriInfo, REST representation);

    SOURCE update(UriInfo uriInfo, REST representation, SOURCE target);

    Collection<SOURCE> to(UriInfo uriInfo, Collection<REST> representations);


    public abstract static class Base<REST, SOURCE> implements RepresentationConverter<REST, SOURCE> {

        private Class<REST> representationClass;
        private Class<SOURCE> sourceClass;

        protected Base() {}

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
        public Collection<REST> from(UriInfo uriInfo, Collection<SOURCE> ins) {
            Collection<REST> out = new ArrayList<REST>();
            for(SOURCE in : ins) {
                out.add(from(uriInfo, in));
            }
            return out;
        }

        @Override
        public Collection<SOURCE> to(UriInfo uriInfo, Collection<REST> ins) {
            Collection<SOURCE> out = new ArrayList<SOURCE>();
            for(REST in : ins) {
                out.add(to(uriInfo, in));
            }
            return out;
        }
    }
}
