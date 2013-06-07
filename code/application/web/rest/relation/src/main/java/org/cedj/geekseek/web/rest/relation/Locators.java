package org.cedj.geekseek.web.rest.relation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public final class Locators {

    private Locators() {}

    static RepresentationConverter<Object, Object> locateCoverterForType(BeanManager manager, final Class<? extends Identifiable> type) {
        ParameterizedType paramType = new ParameterizedType() {
            @Override
            public Type getRawType() {
                return RepresentationConverter.class;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {createWildCard(), type};
            }
        };

        Set<Bean<?>> beans = manager.getBeans(paramType);
        if(beans.isEmpty()) {
            throw new RuntimeException(
                "Could not find any " + RepresentationConverter.class.getName() + " for type " + type);
        }
        Bean<?> bean = manager.resolve(beans);
        CreationalContext<?> cc = manager.createCreationalContext(null);

        @SuppressWarnings("unchecked")
        RepresentationConverter<Object, Object> repo = (RepresentationConverter<Object, Object>)manager.getReference(bean, paramType, cc);
        return repo;
    }

    static WildcardType createWildCard() {
        return new WildcardType() {

            @Override
            public Type[] getUpperBounds() {
                return new Type[] {Object.class};
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }
        };
    }

    static Repository<? extends Identifiable> locateRepository(Iterable<Repository<? extends Identifiable>> repositories, String obj) {
        for(Repository<? extends Identifiable> repo : repositories) {
            if(repo.getType().getSimpleName().equalsIgnoreCase(obj)) {
                return repo;
            }
        }
        return null;
    }
}
