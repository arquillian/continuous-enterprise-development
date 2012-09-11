package org.ced.web.core.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class PageExtension
{
   @Inject
   private Instance<Attachment> attachments;
   
   public List<Attachment> getAttachments()
   {
      List<Attachment> att = new ArrayList<Attachment>();
      Iterator<Attachment> ait = attachments.iterator();
      while(ait.hasNext()) {
         att.add(ait.next());
      }
      return att;
   }

}
