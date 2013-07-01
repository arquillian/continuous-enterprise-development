package org.cedj.geekseek.domain.conference;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Typed;

import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.persistence.PersistenceRepository;

@Stateless
@LocalBean
@Typed(SessionRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SessionRepository extends PersistenceRepository<Session> {

    public SessionRepository() {
        super(Session.class);
    }
}
