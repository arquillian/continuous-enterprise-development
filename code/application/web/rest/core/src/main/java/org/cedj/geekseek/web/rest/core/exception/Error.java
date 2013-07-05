package org.cedj.geekseek.web.rest.core.exception;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Error {

    private String message;

    protected Error() {
    }

    public Error(String message) {
        this.message = message;
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
