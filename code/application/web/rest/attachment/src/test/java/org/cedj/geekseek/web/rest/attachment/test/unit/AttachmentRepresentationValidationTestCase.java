package org.cedj.geekseek.web.rest.attachment.test.unit;

import org.cedj.geekseek.web.rest.attachment.model.AttachmentRepresentation;
import org.cedj.geekseek.web.rest.core.test.unit.NotNullValidationTheory;
import org.junit.experimental.theories.DataPoint;

public class AttachmentRepresentationValidationTestCase extends NotNullValidationTheory {

    @DataPoint
    public static String NOT_NULL_TITLE = "title";
    @DataPoint
    public static String NOT_NULL_MIME_TYPE = "mimeType";
    @DataPoint
    public static String NOT_NULL_URL = "url";

    @Override
    protected Class<?> getValidationClass() {
        return AttachmentRepresentation.class;
    }
}
