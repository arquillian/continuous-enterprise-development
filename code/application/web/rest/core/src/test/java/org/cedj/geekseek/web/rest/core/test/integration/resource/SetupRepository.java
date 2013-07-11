package org.cedj.geekseek.web.rest.core.test.integration.resource;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.arquillian.warp.servlet.BeforeServlet;

public class SetupRepository<T extends Identifiable> extends Inspection {

    private static final long serialVersionUID = 1L;

    private Class<T> domainClass;
    private T domain;

    public SetupRepository(Class<T> domainClass, T domain) {
        this.domainClass = domainClass;
        this.domain = domain;
    }

    @BeforeServlet
    public void storeDomain(BeanManager manager) {
        locateTargetRepository(domainClass, manager).store(domain);
    }

    @AfterServlet
    public void removeDomain(BeanManager manager) {
        locateTargetRepository(domainClass, manager).remove(domain);
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
    public Repository<T> locateTargetRepository(final Class<T> targetType, BeanManager manager) {
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

}
