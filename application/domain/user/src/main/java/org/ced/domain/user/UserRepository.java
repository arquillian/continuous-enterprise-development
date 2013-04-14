package org.ced.domain.user;

import javax.ejb.Stateless;

import org.ced.domain.Repository;
import org.ced.domain.user.model.User;

@Stateless
public class UserRepository extends Repository<User> {

    public UserRepository() {
	super(User.class);
    }
}
