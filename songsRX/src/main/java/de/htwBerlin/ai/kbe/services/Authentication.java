package de.htwBerlin.ai.kbe.services;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.htwBerlin.ai.kbe.storage.ITokenCreator;
import de.htwBerlin.ai.kbe.storage.InMemorySongsPersistence;
import de.htwBerlin.ai.kbe.storage.TokenCreator;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/auth")
public class Authentication {

    @Inject
    private ITokenCreator tokenizer;

    private static final Logger LOG = Logger.getLogger(InMemorySongsPersistence.class);

    /**
     * get the token of the saved user
     * @param userId
     * @return
     * @throws IOException
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAuth(@QueryParam("userId") String userId) throws IOException {

        String token = TokenCreator.getInstance().getToken();

        if (tokenizer.authenticate(userId)) {
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("User does not exists ").build();
        }

    }

}
