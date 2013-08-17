package org.cedj.geekseek.web.rest.conference.test.unit;

import java.util.Date;

import org.cedj.geekseek.web.rest.conference.model.SessionRepresentation;
import org.cedj.geekseek.web.rest.core.test.unit.NotNullValidationTheory;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

public class SessionRepresentationValidationTestCase extends NotNullValidationTheory {

    @DataPoint
    public static String NOT_NULL_TITLE = "title";

    @DataPoint
    public static String NOT_NULL_OUTLINE = "outline";

    @DataPoint
    public static String NOT_NULL_START = "start";

    @DataPoint
    public static String NOT_NULL_END = "end";

    @Override
    protected Class<?> getValidationClass() {
        return SessionRepresentation.class;
    }

    @Test
    public void shouldNotAllowEndBeforeStart() throws Exception {
        SessionRepresentation rep = new SessionRepresentation();
        rep.setTitle("Title");
        rep.setOutline("Outline");
        rep.setEnd(new Date(System.currentTimeMillis()-3000));
        rep.setStart(new Date());

        assertValidationConstraint(rep, "EndBeforeStart", "end");
    }
}
