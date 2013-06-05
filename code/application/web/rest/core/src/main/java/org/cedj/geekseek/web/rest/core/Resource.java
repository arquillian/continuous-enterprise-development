package org.cedj.geekseek.web.rest.core;

import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
public interface Resource {

    Class<? extends Resource> getResourceClass();

    String getResourceMediaType();
}
