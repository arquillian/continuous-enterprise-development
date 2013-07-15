package org.cedj.geekseek.web.rest.core;

public interface Representation<X> {

    Class<X> getSourceType();

    String getRepresentationType();
}
