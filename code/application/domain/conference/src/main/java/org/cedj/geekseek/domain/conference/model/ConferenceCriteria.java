package org.cedj.geekseek.domain.conference.model;

import org.cedj.geekseek.domain.SearchableCriteria;

public class ConferenceCriteria extends SearchableCriteria.PagedBase {

    private String name;

    public ConferenceCriteria() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
