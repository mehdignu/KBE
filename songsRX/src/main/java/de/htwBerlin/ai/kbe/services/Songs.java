package de.htwBerlin.ai.kbe.services;

import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import de.htwBerlin.ai.kbe.pojo.Song;
import de.htwBerlin.ai.kbe.storage.ITokenCreator;
import de.htwBerlin.ai.kbe.storage.InMemorySongsPersistence;
import de.htwBerlin.ai.kbe.storage.TokenCreator;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.Response.Status;
import de.htwBerlin.ai.kbe.Params.Constants;
import org.apache.log4j.Logger;

@Path("/songs")
public class Songs {


    @Context
    UriInfo uriInfo;


    private static TokenCreator tokenManager = TokenCreator.getInstance();
    private static final Logger LOG = Logger.getLogger(InMemorySongsPersistence.class);

    /**
     * get all the songs
     *
     * @param auth
     * @return
     * @throws IOException
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllSongs(@HeaderParam(Constants.AUTH_HEADER) String auth) throws IOException {

        //check user authentication
        if (!tokenManager.checkAuth(auth))
            return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.FAIL_MESSAGE).build();

        LOG.info("getting all the songs");
        //collection to save all the songs
        Collection<Song> songs;
        //get all the saved songs
        songs = InMemorySongsPersistence.getInstance().getAllSongs();

        List list;
        if (songs instanceof List)
            list = (List)songs;
        else
            list = new ArrayList(songs);

        //send them back
        return Response.ok(list).build();
    }

    /**
     * get song with ID
     *
     * @param id
     * @param auth
     * @return
     * @throws IOException
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getSongs(@PathParam("id") Integer id,
                             @HeaderParam(Constants.AUTH_HEADER) String auth) {

        //check user authentication
        try {
            if (!tokenManager.checkAuth(auth))
                return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.FAIL_MESSAGE).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Song song = InMemorySongsPersistence.getInstance().getSongByID(id);

        if (song != null) {
            LOG.info("get back song with id  " + id);

            return Response.ok(song).build();

        } else {

            return Response.status(Response.Status.NOT_FOUND).entity("no song found with id " + id).build();

        }
    }


    /**
     * post a song and return the location back
     *
     * @param auth
     * @param song
     * @return
     * @throws IOException
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.TEXT_PLAIN)
    public Response createSong(@HeaderParam(Constants.AUTH_HEADER) String auth,
                               Song song) {
        //check user authentication
        try {
            if (!tokenManager.checkAuth(auth))
                return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.FAIL_MESSAGE).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("created song" + song.toString());

        Integer newId = InMemorySongsPersistence.getInstance().addSong(song);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(newId.toString());

        return Response.created(uriBuilder.build()).entity("Song added (new id: " + newId + ")").build();
    }


    /**
     * put some songs
     * @param id
     * @param auth
     * @param song
     * @return
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id}")
    public Response updateSong(@PathParam("id") Integer id,
                               @HeaderParam(Constants.AUTH_HEADER) String auth, Song song) {

        //check user authentication
        try {
            if (!tokenManager.checkAuth(auth))
                return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.FAIL_MESSAGE).build();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (song.getId() != null && !id.equals(song.getId()))
            throw new BadRequestException("invalid arguments");


        song.setId(id);
        InMemorySongsPersistence.getInstance().updateSong(song);

        return Response.status(Status.NO_CONTENT).build();


    }

    /**
     * delete some song
     * @param id
     * @param auth
     * @return
     * @throws IOException
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id,
                           @HeaderParam(Constants.AUTH_HEADER) String auth) throws IOException {

        //check user authentication
        if (!tokenManager.checkAuth(auth))
            return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.FAIL_MESSAGE).build();

        Song song = InMemorySongsPersistence.getInstance().deleteSong(id);

        if (song != null) {
            return Response.status(Response.Status.NO_CONTENT).entity("Song is deleted, id = " + id).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("no song found with id =" + id).build();
        }
    }


}