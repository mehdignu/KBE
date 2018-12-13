import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import de.htwBerlin.ai.kbe.pojo.Song;
import de.htwBerlin.ai.kbe.storage.InMemorySongsPersistence;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import de.htwBerlin.ai.kbe.services.Authentication;

public class AuthenticationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(Authentication.class);

    }


    @Test
    public void unvalidUserTest() {
        Response response = target("/auth").queryParam("userId", "muster").request().get();
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void unknownUser() {
        Response response = target("/auth").queryParam("userId", "boo").request().get();
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void validUserTest() {
        Response response = target("/auth").queryParam("userId", "mmuster").request().get();
        Assert.assertEquals(200, response.getStatus());

    }





}