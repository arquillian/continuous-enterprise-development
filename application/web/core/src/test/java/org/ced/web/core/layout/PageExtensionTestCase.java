package org.ced.web.core.layout;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PageExtensionTestCase
{
   @Deployment(testable = false)
   public static WebArchive create() {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(PageExtension.class, Attachment.class) // core
            .addAsWebResource(new File("src/main/webapp/page.xhtml"))
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            
            .addClass(TestAttachment.class)
            .addAsWebResource(new File("src/test/resources/jsf/page_fragment.xhtml"));
      
      System.out.println(war.toString(Formatters.VERBOSE));
      
      return war;
   }
   
   @Test
   public void shouldBeAbleToIncludeDynamicFragments() throws Exception {
      System.out.println("break-point");
   }
}
