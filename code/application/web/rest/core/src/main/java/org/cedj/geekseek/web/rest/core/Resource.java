package org.cedj.geekseek.web.rest.core;

public interface Resource {

    Class<? extends Resource> getResourceClass();

    String getResourceMediaType();
}
