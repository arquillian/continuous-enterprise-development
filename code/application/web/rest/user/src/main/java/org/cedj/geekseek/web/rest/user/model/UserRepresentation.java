package org.cedj.geekseek.web.rest.user.model;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;

@XmlRootElement(name = "user", namespace = "urn:ced:user")
public class UserRepresentation extends LinkableRepresentation<User> implements Identifiable {

    @NotNull
    private String handle;
    @NotNull
    private String name;
    @NotNull
    private String bio;

    private String avatarUrl;

    public UserRepresentation() {
        this(null);
    }

    public UserRepresentation(UriInfo uriInfo) {
        super(User.class, "user", uriInfo);
    }

    @Override @XmlTransient
    public String getId() {
        return handle;
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

    @XmlElement
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
