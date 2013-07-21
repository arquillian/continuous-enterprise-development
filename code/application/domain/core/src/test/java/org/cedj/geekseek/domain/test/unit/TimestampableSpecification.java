package org.cedj.geekseek.domain.test.unit;

import java.util.Date;

import org.cedj.geekseek.domain.model.Timestampable;
import org.junit.Assert;
import org.junit.Test;

public abstract class TimestampableSpecification<ENTITY extends Timestampable> {

    protected abstract ENTITY createInstance() throws Exception;

    protected abstract void forceUpdate(ENTITY entity) throws Exception;
    protected abstract void forceCreated(ENTITY entity) throws Exception;

    @Test
    public void shouldReturnCreatedDate() throws Exception {
        ENTITY entity = createInstance();
        forceCreated(entity);

        Assert.assertNotNull(entity.getCreated());
    }

    @Test
    public void shouldUseUpdatedAsModifiedDateIfUpdated() throws Exception {
        ENTITY entity = createInstance();
        forceCreated(entity);
        Date created = entity.getCreated();

        Date modified = entity.getLastModified();
        Assert.assertEquals(created, modified);

        Thread.sleep(10); // force a tiny sleep to throw the dates off
        forceUpdate(entity);

        modified = entity.getLastModified();
        Assert.assertNotEquals(created, modified);
    }

    @Test
    public void shouldNotLeakCreatedDate() throws Exception {
        ENTITY entity = createInstance();
        forceCreated(entity);

        Date date = entity.getCreated();
        date.setTime(100);

        Assert.assertNotEquals(date, entity.getCreated());
    }

    @Test
    public void shouldNotLeakModifiedDate() throws Exception {
        ENTITY entity = createInstance();
        forceCreated(entity);
        forceUpdate(entity);

        Date date = entity.getLastModified();
        date.setTime(100);

        Assert.assertNotEquals(date, entity.getLastModified());
    }
}