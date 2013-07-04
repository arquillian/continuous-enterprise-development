package org.cedj.geekseek.web.rest.core.test.integration;

import org.cedj.geekseek.domain.model.Identifiable;

public class TestObject implements Identifiable {

    private String id;
    private String message;

    public TestObject(String id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
