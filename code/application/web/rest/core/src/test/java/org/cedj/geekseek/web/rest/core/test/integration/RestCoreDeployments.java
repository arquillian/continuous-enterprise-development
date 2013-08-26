package org.cedj.geekseek.web.rest.core.test.integration;

import java.io.File;

import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.provider.JSONMappingExceptionHandler;
import org.cedj.geekseek.web.rest.core.provider.JSONProvider;
import org.cedj.geekseek.web.rest.core.root.RootResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
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
        String root = Resource.class.getPackage().getName();
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(false,
                root,
                root + "/annotation",
                root + "/interceptor",
                root + "/exception",
                root + "/validation")
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

    public static StringAsset linkableBeansXml() {
        return new StringAsset("<?xml version=\"1.0\"?>" +
                "<beans>" +
                  "<interceptors>" +
                    "<class>org.cedj.geekseek.web.rest.core.interceptor.RESTInterceptorEnabler</class>" +
                  "</interceptors>" +
                "</beans>");
    }
}
