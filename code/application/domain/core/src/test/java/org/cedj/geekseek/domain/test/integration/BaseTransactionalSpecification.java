package org.cedj.geekseek.domain.test.integration;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import junit.framework.Assert;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.model.Timestampable;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Assume;
import org.junit.Test;

@Transactional(value = TransactionMode.DISABLED)
public abstract class BaseTransactionalSpecification<DOMAIN extends Identifiable, REPO extends Repository<DOMAIN>> {

    @Inject
    private UserTransaction tx;

    private Class<DOMAIN> domainClass;

    public BaseTransactionalSpecification(Class<DOMAIN> domainClass) {
        this.domainClass = domainClass;
    }

    protected Class<DOMAIN> getDomainClass() {
        return domainClass;
    }

    /**
     * Get the Repository instance to use.
     */
    protected abstract REPO getRepository();

    /**
     * Create a new unique instance of the Domain Object
     */
    protected abstract DOMAIN createNewDomainObject();

    /**
     * Update some domain object values
     */
    protected abstract void updateDomainObject(DOMAIN domain);

    /**
     * Validate that the update change has occurred.
     * Expecting Assert error when validation does not match.
     */
    protected abstract void validateUpdatedDomainObject(DOMAIN domain);

    @Test
    public void shouldStoreObjectOnCommit() throws Exception {
        final DOMAIN domain = createNewDomainObject();

        commit(Void.class, new Store(domain));

        DOMAIN stored = commit(new Get(domain.getId()));
        Assert.assertNotNull(
            "Object should be stored when transaciton is commited",
            stored);
    }

    @Test
    public void shouldUpdateObjectOnCommit() throws Exception {
        final DOMAIN domain = createNewDomainObject();

        commit(Void.class, new Store(domain));

        DOMAIN stored = commit(new Get(domain.getId()));
        updateDomainObject(stored);
        commit(Void.class, new Store(stored));

        DOMAIN updated = commit(new Get(domain.getId()));
        validateUpdatedDomainObject(updated);
    }

    @Test
    public void shouldRemoveObjectOnCommmit() throws Exception {
        DOMAIN domain = createNewDomainObject();

        commit(Void.class, new Store(domain));
        commit(Void.class, new Remove(domain));

        DOMAIN stored = commit(new Get(domain.getId()));
        Assert.assertNull(
            "Object should be removed when transaciton is committed",
            stored);
    }

    @Test
    public void shouldNotStoreObjectOnRollback() throws Exception {
        final DOMAIN domain = createNewDomainObject();

        rollback(Void.class, new Store(domain));

        DOMAIN stored = commit(getDomainClass(), new Get(domain.getId()));
        Assert.assertNull(
            "Object should not be stored when transaciton is rolledbacked",
            stored);
    }

    @Test
    public void shouldNotUpdateObjectOnRollback() throws Exception {
        final DOMAIN domain = createNewDomainObject();

        commit(Void.class, new Store(domain));

        DOMAIN stored = commit(new Get(domain.getId()));
        updateDomainObject(stored);
        rollback(Void.class, new Store(stored));

        DOMAIN updated = commit(new Get(domain.getId()));
        try {
            validateUpdatedDomainObject(updated);
            Assert.fail("Object should not be updated when transaction is rollbacked");
        } catch(AssertionError error) {
            // no-op, the updated object should not validate as a updated one
        }
    }

    @Test
    public void shouldNotRemoveObjectOnRollback() throws Exception {
        DOMAIN domain = createNewDomainObject();

        commit(Void.class, new Store(domain));
        rollback(Void.class, new Remove(domain));

        DOMAIN stored = commit(new Get(domain.getId()));
        Assert.assertNotNull(
            "Object should not be removed when transaciton is rolledbacked",
            stored);
    }

    @Test
    public void shouldSetCreatedDate() throws Exception {
        DOMAIN domain = createNewDomainObject();
        Assume.assumeTrue(domain instanceof Timestampable);

        commit(Void.class, new Store(domain));
        DOMAIN stored = commit(new Get(domain.getId()));

        Timestampable timed = (Timestampable)stored;

        Assert.assertNotNull(timed.getCreated());
        Assert.assertEquals(timed.getCreated(), timed.getLastModified());
    }

    @Test
    public void shouldSetUpdatedDate() throws Exception {
        DOMAIN domain = createNewDomainObject();
        Assume.assumeTrue(domain instanceof Timestampable);

        commit(Void.class, new Store(domain));
        DOMAIN stored = commit(new Get(domain.getId()));
        updateDomainObject(stored);
        stored = commit(new Get(domain.getId()));

        Timestampable timed = (Timestampable)stored;

        Assert.assertNotNull(timed.getCreated());
        Assert.assertNotSame(timed.getCreated(), timed.getLastModified());
    }

    protected DOMAIN commit(Callable<DOMAIN> callable) throws Exception {
        return commit(getDomainClass(), callable);
    }

    protected DOMAIN rolback(Callable<DOMAIN> callable) throws Exception {
        return rollback(getDomainClass(), callable);
    }

    protected <T> T commit(Class<T> type, Callable<T> callable) throws Exception {
        try {
            tx.begin();
            return callable.call();
        } finally {
            tx.commit();
        }
    }

    protected <T> T rollback(Class<T> type, Callable<T> callable) throws Exception {
        try {
            tx.begin();
            return callable.call();
        } finally {
            tx.rollback();
        }
    }

    private class Store implements Callable<Void> {
        private DOMAIN domain;

        public Store(DOMAIN domain) {
            this.domain = domain;
        }

        @Override
        public Void call() throws Exception {
            getRepository().store(domain);
            return null;
        }
    }

    private class Get implements Callable<DOMAIN> {
        private String id;

        public Get(String id) {
            this.id = id;
        }

        @Override
        public DOMAIN call() throws Exception {
            return getRepository().get(id);
        }
    }

    private class Remove implements Callable<Void> {
        private DOMAIN domain;

        public Remove(DOMAIN domain) {
            this.domain = domain;
        }

        @Override
        public Void call() throws Exception {
            getRepository().remove(domain);
            return null;
        }
    }
}
