import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import de.htwBerlin.ai.kbe.pojo.Song;
import de.htwBerlin.ai.kbe.services.Authentication;
import de.htwBerlin.ai.kbe.services.Songs;

public class SongsRequestsTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(Songs.class);
    }


    @Test
    public void updateSongWithExistingIdShouldReturn204 () {
        Song mySong = new Song();
        mySong.setId(30);
        mySong.setArtist("boo");
        mySong.setTitle("boo");
        mySong.setAlbum("boo");
        mySong.setReleased(2015);
        Response response = target("/songs/10").request().header("Authorization", "toktok").put(Entity.xml(mySong));
        Assert.assertEquals(204, response.getStatus());
    }

    @Test
    public void UpdateSongWithFalseIDShouldReturn204() {
        Song song = new Song();
        song.setId(000);
        song.setTitle("boo");
        song.setAlbum("boo");
        song.setArtist("boo");
        song.setReleased(2015);
        Response response = target("/songs/000").request().header("Authorization", "toktok").put(Entity.json(song));
        Assert.assertEquals(204, response.getStatus());
    }

    @Test
    public void updateSongWithNonMatchingIdShouldReturn400 () {
        Song mySong = new Song();
        mySong.setId(10);
        mySong.setArtist("boo");
        mySong.setTitle("boo");
        mySong.setAlbum("boo");
        mySong.setReleased(2015);
        Response response = target("/songs/14").request().header("Authorization", "toktok").put(Entity.xml(mySong));
        Assert.assertEquals(400, response.getStatus());
    }


    @Test
    public void UpdateSongWithWrongSongFormat01ShouldReturn400() {
        Response response = target("/songs/10").request().put(Entity.json("{\n"
                + "    \"id\": 20,\n"
                + "    \"title\": \"boohoo\",\n"
                + "    \"artist\": \"Miley Cyrus\",\n"
                + "    \"WrongSongFormat\": \"222\",\n"
                + "    \"released\": 2000\n"
                + "}"));
        Assert.assertEquals(400, response.getStatus());
    }
}