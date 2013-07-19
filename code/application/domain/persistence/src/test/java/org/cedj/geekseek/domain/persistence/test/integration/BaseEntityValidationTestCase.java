package org.cedj.geekseek.domain.persistence.test.integration;

import java.util.Date;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.junit.Assert;
import org.junit.Test;

// Timestampable tests
public class BaseEntityValidationTestCase {

    @Test
    public void shouldNotLeakCreatedDate() throws Exception {
        TestBaseEntity base = new TestBaseEntity();
        base.onCreate();

        Date date = base.getCreated();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getCreated());
    }

    @Test
    public void shouldNotLeakUpdatedDate() throws Exception {
        TestBaseEntity base = new TestBaseEntity();
        base.onUpdate();

        Date date = base.getLastUpdated();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getLastUpdated());
    }

    @Test
    public void shouldNotLeakModifiedDate() throws Exception {
        TestBaseEntity base = new TestBaseEntity();
        base.onCreate();
        base.onUpdate();

        Date date = base.getLastModified();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getLastModified());
    }

    @Test
    public void shouldUseUpdatedAsModifiedDateIfUpdated() throws Exception {
        TestBaseEntity base = new TestBaseEntity();
        base.onCreate();
        Date created = base.getCreated();

        Date modified = base.getLastModified();
        Assert.assertEquals(created, modified);

        Thread.sleep(10); // force a tiny sleep to throw the dates off
        base.onUpdate();

        modified = base.getLastModified();
        Assert.assertNotEquals(created, modified);
    }

    private static class TestBaseEntity extends BaseEntity {
        private static final long serialVersionUID = 1L;

        public TestBaseEntity() {
            super("test");
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
        }
    }
}
