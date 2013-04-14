package org.ced.domain.relation.test;

import org.ced.domain.CoreDeployments;
import org.ced.domain.relation.RelationRepository;
import org.ced.domain.relation.model.Relation;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class RelationDeployments
{
   public static JavaArchive relation()
   {
      return CoreDeployments.core()
            .addPackage(Relation.class.getPackage())
            .addPackage(RelationRepository.class.getPackage());
   }
}
