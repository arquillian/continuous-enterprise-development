package org.cedj.geekseek.web.rest.core.test;

import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;
import org.cedj.geekseek.web.rest.core.root.RootResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class RestCoreDeployments {

    public static JavaArchive root() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(false,
                Resource.class.getPackage(),
                ResourceModel.class.getPackage())
            .addPackages(true,
                RootResource.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
