package org.cdej.geekseek.test.functional.arquillian;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class Deployments {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .merge(ShrinkWrap.createFromZipFile(WebArchive.class,
                new File("target/geekseek-1.0.0-alpha-1-SNAPSHOT.war")));
    }
}