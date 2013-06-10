package org.cedj.geekseek.domain.model;

import java.util.Date;

public interface Timestampable {

    /**
     * @return the Date when this Entity was created
     */
    Date getCreated();

    /**
     * Returns the LastUpdated, or the Created Date
     * if this Entity has never been updated.
     *
     * @return the Date when this Entity was last modified
     */
    Date getLastModified();
}
