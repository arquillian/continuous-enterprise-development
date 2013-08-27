package org.cedj.geekseek.web.core.test.integration;

import org.jboss.arquillian.qunit.junit.QUnitRunner;
import org.jboss.arquillian.qunit.junit.annotations.QUnitResources;
import org.jboss.arquillian.qunit.junit.annotations.QUnitTest;
import org.junit.runner.RunWith;

@RunWith(QUnitRunner.class)
@QUnitResources("src")
public class GraphTestCase {

    @QUnitTest("test/resources/assets/tests/graph/graph-assertions.html")
    public void testGraph() {
        // empty body
    }
}
