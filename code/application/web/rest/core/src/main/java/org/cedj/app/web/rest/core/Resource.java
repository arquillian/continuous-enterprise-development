package org.cedj.app.web.rest.core;

import org.cedj.app.web.rest.core.annotation.ResourceModel;

@ResourceModel
public interface Resource {
    Class<? extends Resource> getResourceClass();

    String getResourceMediaType();
}
