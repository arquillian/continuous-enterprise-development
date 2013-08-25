package org.cedj.geekseek.web.rest.relation.test.integration;

import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.servlet.BeforeServlet;

public class ValueInjectionInspection extends Inspection {

    private static final long serialVersionUID = 1L;

    @Inject
    Repository<TargetObject> targetRepo;

    @Inject
    Repository<SourceObject> sourceRepo;

    private TargetObject t;
    private SourceObject s;

    public ValueInjectionInspection(TargetObject t, SourceObject s) {
        this.t = t;
        this.s = s;
    }

    @BeforeServlet
    public void setup() {
        if(t != null) {
            targetRepo.store(t);
        }
        if(s != null) {
            sourceRepo.store(s);
        }
    }
}
