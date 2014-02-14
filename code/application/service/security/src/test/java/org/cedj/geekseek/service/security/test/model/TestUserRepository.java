package org.cedj.geekseek.service.security.test.model;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.user.model.User;

public class TestUserRepository implements Repository<User> {

	@Override
	public Class<User> getType() {
		return null;
	}

	@Override
	public User store(User entity) {
		return null;
	}

	@Override
	public User get(String id) {
		return null;
	}

	@Override
	public void remove(User entity) {
	}
}
