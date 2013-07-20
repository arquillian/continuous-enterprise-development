package org.cedj.geekseek.service.security.picketlink;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.domain.util.Validate;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.Partition;

public class UserAccount implements Account {

    private static final long serialVersionUID = 1L;

    private User user;

    public UserAccount(User user) {
        Validate.requireNonNull(user, "User must be specified");
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public Date getCreatedDate() {
        return user.getCreated();
    }

    @Override
    public void setCreatedDate(Date createdDate) {
    }

    @Override
    public Date getExpirationDate() {
        return null;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
    }

    @Override
    public Partition getPartition() {
        return new TwitterPartition();
    }

    @Override
    public void setPartition(Partition partition) {
    }

    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public void setAttribute(Attribute<? extends Serializable> attribute) {
    }

    @Override
    public void removeAttribute(String name) {
    }

    @Override
    public <T extends Serializable> Attribute<T> getAttribute(String name) {
        return null;
    }

    @Override
    public Collection<Attribute<? extends Serializable>> getAttributes() {
        return null;
    }
}
