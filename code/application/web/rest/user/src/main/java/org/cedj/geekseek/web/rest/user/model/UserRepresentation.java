package org.cedj.geekseek.web.rest.user.model;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;

@XmlRootElement(name = "user", namespace = "urn:ced:user")
public class UserRepresentation extends LinkableRepresenatation<User> {

    private String handle;
    private String name;
    private String bio;

    public UserRepresentation() {
    }

    public UserRepresentation(UriInfo uriInfo) {
        super(User.class, "user", uriInfo);
    }

    @XmlElement
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    public User to() {
        return new User(handle).setName(name).setBio(bio);
    }
}
