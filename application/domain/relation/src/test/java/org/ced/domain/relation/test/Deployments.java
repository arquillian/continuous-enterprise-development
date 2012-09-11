package org.ced.domain.relation.test;

import org.ced.domain.Identifiable;
import org.ced.domain.relation.Relation;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence10.PersistenceDescriptor;

public class Deployments {

    public static JavaArchive relation() {
	return ShrinkWrap.create(JavaArchive.class)
		.addPackage(Identifiable.class.getPackage())
		.addPackage(Relation.class.getPackage());
    }
    
    public static PersistenceDescriptor persistence() {
	return Descriptors.create(PersistenceDescriptor.class)
		.createPersistenceUnit()
			.name("test")
			.getOrCreateProperties()
				.createProperty()
					.name("hibernate.hbm2ddl.auto")
					.value("create-drop").up()
				.createProperty()
					.name("hibernate.show_sql")
					.value("true").up().up()
			.jtaDataSource("java:jboss/datasources/ExampleDS")
		.up();
    }
}
