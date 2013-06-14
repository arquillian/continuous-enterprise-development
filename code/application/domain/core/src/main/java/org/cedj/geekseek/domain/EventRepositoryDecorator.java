package org.cedj.geekseek.domain;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.cedj.geekseek.domain.model.Identifiable;

@Decorator
public abstract class EventRepositoryDecorator<T extends Identifiable> implements Repository<T> {

    @Inject
    private Event<Identifiable> event;

    @Inject @Any @Delegate
    private Repository<T> delegate;

    @Override
    public T store(T entity) {
        T stored = delegate.store(entity);
        event.select(delegate.getType(), new Created.Literal()).fire(entity);
        return stored;
    }

    @Override
    public void remove(T entity) {
        delegate.remove(entity);
        event.select(delegate.getType(), new Removed.Literal()).fire(entity);
    }
}
