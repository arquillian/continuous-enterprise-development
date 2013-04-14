package org.ced.domain.conference;

import static org.ced.domain.conference.TestUtils.toDate;

import javax.inject.Inject;

import org.ced.domain.CoreDeployments;
import org.ced.domain.conference.model.Conference;
import org.ced.domain.conference.model.Duration;
import org.ced.domain.conference.model.Session;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@Transactional(TransactionMode.DISABLED) // our Repository is transactional
@RunWith(Arquillian.class)
public class ConferenceTestCase {
    
    // Given
    @Deployment
    public static JavaArchive deploy() {
        return ConferenceDeployments.conference()
                .addClasses(ConferenceTestCase.class, TestUtils.class)
                .addAsManifestResource(
                        new StringAsset(CoreDeployments.persistence().exportAsString()), "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private ConferenceRepository repository;
    
    // Story: As a User I should be able to create a Conference
    
    @Test
    @ShouldMatchDataSet(value = {"conference.yml"}, excludeColumns = {"*id"})
    public void shouldBeAbleToCreateConference() {
        
        Conference conference = createConference();
        
        repository.store(conference);
    }
    
    // Story: As a User I should be able to create a Conference with a Session
    
    @Test
    @ShouldMatchDataSet(value = {"conference.yml", "session.yml"}, excludeColumns = {"*id"})
    public void shouldBeAbleToCreateConferenceWithSession() {
        
        Conference conference = createConference();
        conference.addSession(createSession());
        
        repository.store(conference);
    }

    // Story: As a User I should be able to add a Session to a existing Conference
    
    @Test
    @UsingDataSet("conference.yml")
    @ShouldMatchDataSet(value = {"conference.yml", "session.yml"}, excludeColumns = {"*id"})
    public void shouldBeAbleToAddSessionToConference() {
        
        Conference conference = repository.get("CA");
        conference.addSession(createSession());
        
        repository.store(conference);
    }

    // Story: As a User I should be able to remove a Conference
    
    @Test
    @UsingDataSet("conference.yml")
    @ShouldMatchDataSet("conference_empty.yml")
    public void shouldBeAbleToRemoveConference() {
        
        Conference conference = repository.get("CA");
        
        repository.remove(conference);
    }
    
    // Story: As a User I should be able to remove a Session from a Conference
    
    @Test
    @UsingDataSet({"conference.yml", "session.yml"})
    @ShouldMatchDataSet({"conference.yml", "session_empty.yml"})
    public void shouldBeAbleToRemoveConferenceWithSession() {
        
        Conference conference = repository.get("CA");
        Session session = conference.getSessions().toArray(new Session[0])[0];
        conference.removeSession(session);
        
        repository.store(conference);
    }

    // Story: As a User I should be able to change a Conference

    @Test
    @UsingDataSet("conference.yml")
    @ShouldMatchDataSet(value = {"conference_updated.yml"})
    public void shouldBeAbleToChangeConference() {

        Conference conference = repository.get("CA");
        conference.setName("UPDATED");
        
        repository.store(conference);
    }
    
    // Story: As a User I should be able to change a Session
    
    @Test
    @UsingDataSet({"conference.yml", "session.yml"})
    @ShouldMatchDataSet(value = {"conference.yml", "session_updated.yml"})
    public void shouldBeAbleToChangeSession() {
        
        Conference conference = repository.get("CA");
        conference.getSessions().toArray(new Session[0])[0].setTitle("UPDATED");
        
        repository.store(conference);
    }

    // TODO: Move to reusable util ? How to not mix "in test data" vs "external dataset"? 
    
    private Conference createConference() {
        Conference conference = new Conference();
        conference.setName("Devoxx Belgium 2013");
        conference.setTagLine("We Code In Peace");
        conference.setDuration(new Duration(toDate(2013, 11, 11), toDate(2013, 11, 15)));
        return conference;
    }
    
    private Session createSession() {
        Session session = new Session();
        session.setTitle("Testing the Enterprise layers - The A, B, C’s of integration testing");
        session.setOutline("For years we’ve been exploring how to layer and separate our code to test in isolation on the unit level. We’ve kept integration and functional testing as a big ball of mud; jumping straight from unit to full system testing. But can we apply some of the same lessons learned from unit to integration testing?\\n\\nThis session explore the different technologies within the Java Enterprise specification and see how our application can be tested in isolation; layer for layer, module for module and component for component.\\n\\nCan we isolate and stay real at the same time? Does mocks, stubs and test doubles have a place in the world of integration testing? Are there other lessons to be learned?");
        session.setDuration(new Duration(toDate(2013, 11, 11, 15, 00), toDate(2013, 11, 11, 16, 00)));
        return session;
    }
}
