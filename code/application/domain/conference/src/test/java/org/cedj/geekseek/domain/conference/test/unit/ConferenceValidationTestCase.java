package org.cedj.geekseek.domain.conference.test.unit;

import java.lang.reflect.Method;
import java.util.Date;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.cedj.geekseek.domain.test.unit.TimestampableSpecification;
import org.junit.Test;

public class ConferenceValidationTestCase extends TimestampableSpecification<Conference> {

    @Override
    protected Conference createInstance() {
        return new Conference("", "", new Duration(new Date(), new Date()));
    }

    @Override
    protected void forceCreated(Conference entity) {
        // date set during object creation
    }

    @Override
    protected void forceUpdate(Conference entity) throws Exception {
        Method m = BaseEntity.class.getDeclaredMethod("onUpdate");
        m.setAccessible(true);
        m.invoke(entity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorName() throws Exception {
        new Conference(null, "", new Duration(new Date(), new Date()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorTagline() throws Exception {
        new Conference("", null, new Duration(new Date(), new Date()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorDuration() throws Exception {
        new Conference("", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterName() throws Exception {
        Conference conf = new Conference("", "", new Duration(new Date(), new Date()));
        conf.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterTagLine() throws Exception {
        Conference conf = new Conference("", "", new Duration(new Date(), new Date()));
        conf.setTagLine(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterDuration() throws Exception {
        Conference conf = new Conference("", "", new Duration(new Date(), new Date()));
        conf.setDuration(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowToAddNullSession() throws Exception {
        Conference conf = new Conference("", "", new Duration(new Date(), new Date()));
        conf.addSession(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotAllowToAddSessionToSessions() throws Exception {
        Conference conf = new Conference("", "", new Duration(new Date(), new Date()));
        Session sess = new Session("", "", new Duration(new Date(), new Date()));
        conf.getSessions().add(sess);
    }
}
