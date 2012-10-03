package org.ced.domain.relation.test;

import java.util.List;

import javax.inject.Inject;

import org.ced.domain.relation.Relation;
import org.ced.domain.relation.RelationRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RelationTestCase {

	@Deployment
	public static JavaArchive deploy() {
		return Deployments
				.relation()
				.addPackage(RelationTestCase.class.getPackage())
				.addAsManifestResource(
						new StringAsset(Deployments.persistence().exportAsString()), "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	private static final String SOURCE_ID = "11";
	private static final String TARGET_ID = "1";

	private SourceObject source;
	private TargetObject target;
	private Relation.Type type;

	@Inject
	private RelationRepository repository;

	@Before
	public void createTypes() {
		source = new SourceObject(SOURCE_ID);
		target = new TargetObject(TARGET_ID);
		type = Relation.Type.SPEAKING;
	}

	@Test
	@UsingDataSet("test_objects.yml")
	public void shouldBeAbleToCreateRelation() {

		Relation relation = repository.add(source, type, target);

		Assert.assertEquals(
				"Verify retuned object has same source id",
				relation.getSourceId(), source.getId());
		Assert.assertEquals(
				"Verify retuned object has same target id",
				relation.getTargetId(), target.getId());
		Assert.assertEquals(
				"Verify retuned object has same type",
				relation.getType(), type);

		Assert.assertNotNull("Verify created date was set",
				relation.getCreated());
	}

	@Test
	@UsingDataSet({ "test_objects.yml", "test_object_relations.yml" })
	public void shouldBeAbleToFindTargetedRelations() {

		List<TargetObject> tagets = repository.findTargets(source, type,
				TargetObject.class);

		Assert.assertNotNull(
				"Verify a non null list returned", tagets);
		Assert.assertEquals(
				"Verify expected targets count", 1, tagets.size());

		Assert.assertEquals(
				"Verify expected target returned",
				TARGET_ID, tagets.get(0).getId());
	}
}
