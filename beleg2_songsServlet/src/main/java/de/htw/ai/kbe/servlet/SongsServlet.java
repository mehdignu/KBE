package de.htw.ai.kbe.servlet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.htw.ai.kbe.servlet.Utils.Constants;

@WebServlet(name = "SongsServlet", urlPatterns = "/*", initParams = {
        @WebInitParam(name = "songFile", value = "songs.json")})

public class SongsServlet extends HttpServlet {


    //serial version
    private static final long serialVersionUID = 1L;

    //set up the logger
    private static final Logger log = Logger.getLogger(SongsServlet.class.getName());


    private String songsSource = null;

    private Map<Integer, Song> songsData = null;

    private AtomicInteger currentID = null;


    /**
     * load songs from json file
     *
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig config) {

        songsSource = config.getInitParameter("songFile");

        if (songsSource == null) {
            songsSource = "songs.json";
        }

        InputStream input = this.getClass().getClassLoader().getResourceAsStream(songsSource);

        List<Song> songList = null;
        try {
            songList = new ObjectMapper().readValue(input, new TypeReference<List<Song>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        songsData = new ConcurrentHashMap<>();

        songList.forEach(e -> this.songsData.put(e.getId(), e));

        currentID = new AtomicInteger(songsData.size());
        log.info("init ()");

    }


    private Map<String, String> readRequest(Enumeration<String> parameterVals, HttpServletRequest request) {


        Map<String, String> parameters = new ConcurrentHashMap<>();

        String param;

        while (parameterVals.hasMoreElements()) {
            param = parameterVals.nextElement();
            parameters.put(param, request.getParameter(param));
        }

        return parameters;

    }


    private void handleRequest(Map<String, String> parameters, HttpServletResponse response, PrintWriter out) throws JsonProcessingException {

        if (parameters.get(Constants.SEARCH_ALL) != null && parameters.get(Constants.SEARCH_ALL).equals("")) {

            //show all songs as json
            response.setContentType(Constants.CONTENT_TYPE_JSON);
            out.println(new ObjectMapper().writeValueAsString(songsData));

        } else if (parameters.get(Constants.SEARCH_SONGID) != null) {

            String value = parameters.get(Constants.SEARCH_SONGID);

            try {

                //get the songID
                int songID = Integer.parseInt(value);

                if (songsData.get(songID) != null) {

                    response.setContentType(Constants.CONTENT_TYPE_JSON);
                    out.println(new ObjectMapper().writeValueAsString(songsData.get(songID)));

                } else {

                    response.setContentType(Constants.CONTENT_TYPE_TEXT);
                    out.println("Song is not Found");
                    log.info("searched song not found");

                }
            } catch (NumberFormatException e) {

                response.setContentType(Constants.CONTENT_TYPE_TEXT);
                out.println("invalid songID");
                log.info("invalid songID" + e);
            }

        } else {
            response.setContentType(Constants.CONTENT_TYPE_TEXT);
            out.println("invalid parameter");
            log.info("invalid parameter");

        }

    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("doGet ()");

        Enumeration<String> parameterVals = request.getParameterNames();

        Map<String, String> parameters = readRequest(parameterVals, request);

        try (PrintWriter out = response.getWriter()) {

            if (parameters.size() == 1) {

                handleRequest(parameters, response, out);

            } else if (parameters.size() > 1) {
                response.setContentType(Constants.CONTENT_TYPE_TEXT);
                out.println("Parameters are invalid");
            } else {
                response.setContentType(Constants.CONTENT_TYPE_TEXT);
                out.println("Please enter some parameters");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("doPost()");

        response.setContentType(Constants.CONTENT_TYPE_TEXT);

        try (PrintWriter out = response.getWriter()) {

            String body = request.getReader() // Get the buffered reader for reading request body
                    .lines() // Stream it
                    .reduce("", (x, y) -> x + y); // Convert lines to one String

//            out.println(body);

            if (body.equals("null") || body.isEmpty()) {

                out.println("song value is empty");
                log.info("song value is empty");

            } else {
                saveSong(body, out);
            }
        }
    }


    private void saveSong(String body, PrintWriter out) {

        try {

            Song song = new ObjectMapper().readValue(body, Song.class);

            song.setId(currentID.incrementAndGet());
            songsData.put(currentID.get(), song);
            out.println("new song is saved under the ID: " + currentID);

        } catch (JsonParseException e) {
            out.println("Please Post a valid request");
            log.info("POST request not valid" + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        log.info("destroy ()");
        try {

            ObjectMapper m = new ObjectMapper();
            m.enable(SerializationFeature.INDENT_OUTPUT);
            String str = this.getClass().getClassLoader().getResource(songsSource).getPath();
            FileOutputStream out = new FileOutputStream(str);
            m.writeValue(out, songsData.values());

        } catch (IOException e) {
            log.info("can't write songsData to a file  " + e);
        }
    }

} 