package org.cedj.geekseek.web.rest.user.model;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

@RequestScoped
public class UserRepresentationConverter extends RepresentationConverter.Base<UserRepresentation, User> {

    public UserRepresentationConverter() {
        super(UserRepresentation.class, User.class);
    }

    @Override
    public UserRepresentation from(UriInfo uriInfo, User source) {
        UserRepresentation rep = new UserRepresentation(uriInfo);
        rep.setHandle(source.getId());
        rep.setName(source.getName());
        rep.setBio(source.getBio());
        return rep;
    }

    @Override
    public User to(UriInfo uriInfo, UserRepresentation representation) {
        return update(uriInfo, representation, new User(representation.getHandle()));
    }

    @Override
    public User update(UriInfo uriInfo, UserRepresentation representation, User target) {
        target.setName(representation.getName());
        target.setBio(representation.getBio());
        return target;
    }
}
