package org.cedj.geekseek.web.rest.conference.test.unit;

import java.util.Date;

import org.cedj.geekseek.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.geekseek.web.rest.core.test.unit.NotNullValidationTheory;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

public class ConferenceRepresentationValidationTestCase extends NotNullValidationTheory {

    @DataPoint
    public static String NOT_NULL_NAME = "name";

    @DataPoint
    public static String NOT_NULL_TAGLINE = "tagLine";

    @DataPoint
    public static String NOT_NULL_START = "start";

    @DataPoint
    public static String NOT_NULL_END = "end";

    @Override
    protected Class<?> getValidationClass() {
        return ConferenceRepresentation.class;
    }

    @Test
    public void shouldNotAllowEndBeforeStart() throws Exception {
        ConferenceRepresentation rep = new ConferenceRepresentation();
        rep.setName("Name");
        rep.setTagLine("TagLine");
        rep.setEnd(new Date(System.currentTimeMillis()-3000));
        rep.setStart(new Date());

        assertValidationConstraint(rep, "EndBeforeStart", "end");
    }
}
