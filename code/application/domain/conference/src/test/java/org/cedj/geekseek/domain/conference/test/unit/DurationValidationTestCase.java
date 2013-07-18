package org.cedj.geekseek.domain.conference.test.unit;

import java.util.Date;

import org.cedj.geekseek.domain.conference.model.Duration;
import org.junit.Assert;
import org.junit.Test;

public class DurationValidationTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorStart() throws Exception {
        new Duration(null, new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorEnd() throws Exception {
        new Duration(new Date(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorEndBeforeStart() throws Exception {
        new Duration(new Date(), new Date(System.currentTimeMillis()-4000));
    }

    @Test
    public void shouldNotLeakOnConststructStart() throws Exception {
        Date start = new Date();
        Duration dur = new Duration(start, new Date());
        start.setTime(100);

        Assert.assertNotEquals(start, dur.getStart());
    }

    @Test
    public void shouldNotLeakOnConststructEnd() throws Exception {
        Date end = new Date();
        Duration dur = new Duration(new Date(), end);
        end.setTime(100);

        Assert.assertNotEquals(end, dur.getEnd());
    }

    @Test
    public void shouldNotLeakOnGetStart() throws Exception {
        Duration dur = new Duration(new Date(), new Date());
        Date start = dur.getStart();
        start.setTime(100);

        Assert.assertNotEquals(start, dur.getStart());
    }

    @Test
    public void shouldNotLeakOnGetEnd() throws Exception {
        Duration dur = new Duration(new Date(), new Date());
        Date end = dur.getEnd();
        end.setTime(100);

        Assert.assertNotEquals(end, dur.getEnd());
    }
}
