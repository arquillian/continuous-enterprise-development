package org.cedj.geekseek.web.rest.core.test.integration;

import java.io.File;

import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;
import org.cedj.geekseek.web.rest.core.provider.JSONMappingExceptionHandler;
import org.cedj.geekseek.web.rest.core.provider.JSONProvider;
import org.cedj.geekseek.web.rest.core.root.RootResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class RestCoreDeployments {

    public static void add(WebArchive war) {
        war.addAsLibraries(root());
    }

    public static void addWithJson(WebArchive war) {
        war.addAsLibraries(rootWithJSON());
        war.addAsLibraries(resolveDependencies());
    }

    public static JavaArchive root() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(false,
                Resource.class.getPackage(),
                ResourceModel.class.getPackage())
            .addPackages(true,
                RootResource.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static JavaArchive rootWithJSON() {
        return root()
            .addPackages(true, JSONProvider.class.getPackage())
            .addPackages(true, JSONMappingExceptionHandler.class.getPackage());
    }

    public static File[] resolveDependencies() {
        return Maven.resolver().loadPomFromFile("pom.xml")
            .resolve(
                "com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider")
            .withTransitivity()
            .asFile();
    }
}
