package org.cedj.geekseek.web.core.test.integration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

    static String asString(final InputStream in) throws IllegalArgumentException {
        return new String(asByteArray(in));
    }

    static byte[] asByteArray(final InputStream in) throws IllegalArgumentException {
        // Precondition check
        if (in == null) {
            throw new IllegalArgumentException("stream must be specified");
        }

        // Get content as an array of bytes
        final ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
        final int len = 4096;
        final byte[] buffer = new byte[len];
        int read = 0;
        try {
            while (((read = in.read(buffer)) != -1)) {
                out.write(buffer, 0, read);
            }
        } catch (final IOException ioe) {
            throw new RuntimeException("Error in obtainting bytes from " + in, ioe);
        } finally {
            try {
                in.close();
            } catch (final IOException ignore) {
            }
            // We don't need to close the outstream, it's a byte array out
        }

        // Represent as byte array
        final byte[] content = out.toByteArray();

        // Return
        return content;
    }

}
