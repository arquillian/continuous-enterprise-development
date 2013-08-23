package org.cedj.geekseek.service.security.test.arquillian;

import java.net.URI;
import java.util.Collection;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.event.container.AfterDeploy;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.LoadableExtension;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SessionConfig;

/**
 * Arquillian Extension that will do a authorization request
 * after deployment to create a active session and set the session
 * id as 'current session' for the RestAssured client lib.
 *
 * Since security is only active if the Security module is deployed,
 * rest clients in the test suite shouldn't have to tie them selves
 * into how security is done, if at all active.
 *
 * This Extension enables/activates security in the Test suite
 * if the security module is included.
 *
 * This behavior can be disabled by configuring the authsession
 * extension in arquillian.xml, using the disabled=true property.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
public class AuthorizedSessionExtension implements LoadableExtension {

    private static final String EXT_NAME = "authsession";
    private static final String EXT_DISABLE = "disabled";

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(CreateAuthorizedSession.class);
    }

    public static class CreateAuthorizedSession {

        @Inject
        private Instance<ArquillianDescriptor> desc;

        public void performLogin(@Observes AfterDeploy event, ProtocolMetaData data) {

            if(isDisabled()) {
                return;
            }

            URI authUri = locateAuthURI(data);
            String sessionId = authenticate(authUri);

            RestAssured.config = RestAssuredConfig.config().sessionConfig(
                SessionConfig.sessionConfig().sessionIdValue(sessionId));
        }

        private String authenticate(URI authUri) {
            return new TwitterLogin().login(authUri.toASCIIString());
        }

        private URI locateAuthURI(ProtocolMetaData data) {
            Collection<HTTPContext> contexts = data.getContexts(HTTPContext.class);
            if(contexts == null || contexts.size() == 0 || contexts.size() > 1) {
                throw new RuntimeException("Could not determine auth URL: " + contexts);
            }

            HTTPContext context = contexts.iterator().next();
            return URI.create(context.getServlets().get(0).getBaseURI()+ "auth");
        }

        private boolean isDisabled() {
            for(ExtensionDef def : desc.get().getExtensions()) {
                if(EXT_NAME.equals(def.getExtensionName())) {
                    if(def.getExtensionProperties().containsKey(EXT_DISABLE)) {
                        if(def.getExtensionProperties().get(EXT_DISABLE).equals("true")) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
