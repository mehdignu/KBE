package de.htw.ai.kbe.servlet;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import de.htw.ai.kbe.servlet.SongsServlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import de.htw.ai.kbe.servlet.Song;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SongsServletTest {

    private SongsServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private static final String DATASOURCE_FILE = "songs.json";


    @Before
    public void setUp() {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        MockServletConfig config = new MockServletConfig();
        servlet = new SongsServlet();
        servlet.init(config);
    }


    @After
    public void tearDown() {
        System.out.println("##########################################################");
    }


    @Test
    public void testDogetInvalidSongId() throws IOException {
        request.addParameter("songId", "-.-");
        servlet.doGet(request, response);
        String s = response.getContentAsString().trim();
        assertEquals("invalid songID", s);
    }

    @Test
    public void testDogetWithoutParameter() throws IOException {

        servlet.doGet(request, response);
        String s = response.getContentAsString().trim();
        assertEquals("Please enter some parameters", s);
    }

    @Test
    public void testDogetWitInvalidParameters() throws IOException {
        request.addParameter("ded", "5");
        servlet.doGet(request, response);
        String s = response.getContentAsString().trim();
        assertEquals("invalid parameter", s);
    }

    @Test
    public void testDoPostWithEmptyBody() throws IOException {
        String stupidJson = "";
        request.setContent(stupidJson.getBytes());
        servlet.doPost(request, response);
        String s = response.getContentAsString().trim();
        assertEquals("song value is empty", s);
    }


    @Test
    public void testDoPostWithBrokenJsonPayload() throws IOException {
        String stupidJson = "boohoo";
        request.setContent(stupidJson.getBytes());
        servlet.doPost(request, response);
        String s = response.getContentAsString().trim();
        System.out.println(s);
        assertEquals("Please Post a valid request", s);
    }


}
