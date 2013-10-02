package org.cedj.geekseek.domain.relation.neo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

@ApplicationScoped
public class GraphDatabaseProducer {

    private String DATABASE_PATH_PROPERTY = "NEO4J.PATH";

    private static Logger log = Logger.getLogger(GraphDatabaseProducer.class.getName());

    @Produces
    public GraphDatabaseService createGraphInstance() throws Exception {
        String databasePath = getDataBasePath();
        log.info("Using Neo4j database at " + databasePath);
        return new GraphDatabaseFactory().newEmbeddedDatabase(databasePath);
    }

    @Produces
    public ExecutionEngine createQueryEngine(GraphDatabaseService service) {
        return new ExecutionEngine(service);
    }

    public void shutdownGraphInstance(@Disposes GraphDatabaseService service) throws Exception {
        service.shutdown();
    }

    private String getDataBasePath() {
        String path = System.getenv(DATABASE_PATH_PROPERTY);
        if(path == null || path.isEmpty()) {
            try {
                File tmp = File.createTempFile("neo", "geekseek");
                File parent = tmp.getParentFile();
                tmp.delete();
                parent.mkdirs();
                path = parent.getAbsolutePath();
            } catch (IOException e) {
                throw new RuntimeException(
                    "Could not create temp location for Nepo4j Database. " +
                    "Please provide environment variable " + DATABASE_PATH_PROPERTY + " with a valid path", e);
            }
        }
        return path;
    }
}
