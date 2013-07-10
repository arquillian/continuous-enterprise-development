package org.cedj.geekseek.web.rest.user.test.integration;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.user.model.User;

@ApplicationScoped
public class TestRepository implements Repository<User> {

    private Set<User> users = new HashSet<User>();

    @Override
    public Class<User> getType() {
        return User.class;
    }

    protected Set<User> getStored() {
        return users;
    }

    @Override
    public User store(User entity) {
        users.add(entity);
        return entity;
    }

    @Override
    public User get(String id) {
        for(User user: users) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void remove(User entity) {
        users.remove(entity);
    }
}
