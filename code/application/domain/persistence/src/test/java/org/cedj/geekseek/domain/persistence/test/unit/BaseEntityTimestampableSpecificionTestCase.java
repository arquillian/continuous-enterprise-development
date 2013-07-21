package org.cedj.geekseek.domain.persistence.test.unit;

import java.util.Date;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.cedj.geekseek.domain.persistence.test.unit.BaseEntityTimestampableSpecificionTestCase.TestBaseEntity;
import org.cedj.geekseek.domain.test.unit.TimestampableSpecification;
import org.junit.Assert;
import org.junit.Test;

public class BaseEntityTimestampableSpecificionTestCase extends TimestampableSpecification<TestBaseEntity> {

    @Override
    protected TestBaseEntity createInstance() {
        return new TestBaseEntity();
    }

    @Override
    protected void forceCreated(TestBaseEntity entity) {
        entity.onCreate();
    }

    @Override
    protected void forceUpdate(TestBaseEntity entity) {
        entity.onUpdate();
    }

    @Test
    public void shouldNotLeakUpdatedDate() throws Exception {
        TestBaseEntity base = new TestBaseEntity();
        base.onUpdate();

        Date date = base.getLastUpdated();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getLastUpdated());
    }

    public static class TestBaseEntity extends BaseEntity {
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
