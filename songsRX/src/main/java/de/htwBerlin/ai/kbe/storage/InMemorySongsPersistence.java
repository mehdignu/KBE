package de.htwBerlin.ai.kbe.storage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import de.htwBerlin.ai.kbe.pojo.Song;

public class InMemorySongsPersistence {

    private static Map<Integer, Song> songsStorage;
    private static InMemorySongsPersistence singleInstance = null;
    private static final Logger LOG = Logger.getLogger(InMemorySongsPersistence.class);

    private InMemorySongsPersistence() {
        LOG.info("Constructing InMemorySongsPersistance");
        songsStorage = new HashMap<>();
        init();
    }

    public synchronized static InMemorySongsPersistence getInstance() {
        if (singleInstance == null) {
            singleInstance = new InMemorySongsPersistence();
        }
        return singleInstance;
    }

    /**
     * init the songs list
     */
    private void init() {
        LOG.info("Init songs persistence in memory");

        List<Song> songs = new ArrayList<>();
        try {

            String fileName = getClass().getClassLoader().getResource("songs.json").getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
                songs = objectMapper.readValue(is, new TypeReference<List<Song>>() {
                });
            }

        } catch (IOException e) {
            LOG.debug("Songs Init error");
        }
        for (Song s : songs) {
            songsStorage.put(s.getId(), s);
        }
        LOG.info("Songs stored in memory");

    }

    /**
     * get all the saved songs
     *
     * @return
     */
    public Collection<Song> getAllSongs() {
        return songsStorage.values();
    }

    /**
     * get a song by id
     *
     * @param id id of the wanted song
     * @return
     */
    public Song getSongByID(Integer id) {
        return songsStorage.get(id);
    }


    /**
     * add a song to the list
     *
     * @param song
     * @return
     */
    public Integer addSong(Song song) {
        LOG.info("song added with id " + song.getId());
        songsStorage.put(song.getId(), song);
        return song.getId();
    }


    /**
     * update a song in the list
     *
     * @param song
     * @return
     */
    public boolean updateSong(Song song) {

        //in the case of the song doesn't exists
        if (!songExists(song.getId()))
            return false;

        songsStorage.replace(song.getId(), song);
        LOG.info("song updated with id " + song.getId());
        return true;

    }

    // returns deleted song
    public Song deleteSong(Integer id) {

        if (!songExists(id))
            return null;

        Song deletedSong;
        deletedSong = songsStorage.get(id);
        songsStorage.remove(id);
        LOG.info("song deleted with id " + id);
        return deletedSong;
    }

    private boolean songExists(Integer id) {
        List<Integer> ids = new ArrayList<>();
        songsStorage.entrySet().forEach(e -> ids.add(e.getKey()));
        return ids.contains(id);


    }


}
