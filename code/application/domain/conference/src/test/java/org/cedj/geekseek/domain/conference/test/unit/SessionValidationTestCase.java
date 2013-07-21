package org.cedj.geekseek.domain.conference.test.unit;

import java.lang.reflect.Method;
import java.util.Date;

import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.cedj.geekseek.domain.test.unit.TimestampableSpecification;
import org.junit.Test;

public class SessionValidationTestCase extends TimestampableSpecification<Session> {

    @Override
    protected Session createInstance() throws Exception {
        return new Session("", "", new Duration(new Date(), new Date()));
    }

    @Override
    protected void forceCreated(Session entity) throws Exception {
        // date set during object creation
    }

    @Override
    protected void forceUpdate(Session entity) throws Exception {
        Method m = BaseEntity.class.getDeclaredMethod("onUpdate");
        m.setAccessible(true);
        m.invoke(entity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorTitle() throws Exception {
        new Session(null, "", new Duration(new Date(), new Date()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorOutline() throws Exception {
        new Session("", null, new Duration(new Date(), new Date()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorDuration() throws Exception {
        new Session("", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterTitle() throws Exception {
        Session sess = new Session("", "", new Duration(new Date(), new Date()));
        sess.setTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterOutline() throws Exception {
        Session sess = new Session("", "", new Duration(new Date(), new Date()));
        sess.setOutline(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterDuration() throws Exception {
        Session sess = new Session("", "", new Duration(new Date(), new Date()));
        sess.setDuration(null);
    }
}
