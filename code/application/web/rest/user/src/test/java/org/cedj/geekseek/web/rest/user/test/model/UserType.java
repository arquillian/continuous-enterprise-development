package org.cedj.geekseek.web.rest.user.test.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:user", name = "user")
public class UserType {

    private String handle;
    private String name;
    private String bio;

    public UserType() {
    }

    public String getHandle() {
        return handle;
    }
    public UserType setHandle(String handle) {
        this.handle = handle;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserType setName(String name) {
        this.name = name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public UserType setBio(String bio) {
        this.bio = bio;
        return this;
    }
}
