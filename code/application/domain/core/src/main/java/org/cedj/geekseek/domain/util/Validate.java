package org.cedj.geekseek.domain.util;

public final class Validate {

    /**
     * Simple validation helper method to validate if a given
     * Object is not null
     *
     * @param obj Object to verify is not null
     * @param message Message to describe which object should not be null
     */
    public static void requireNonNull(Object obj, String message) {
        if(obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
