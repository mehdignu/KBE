package de.htwBerlin.ai.kbe.storage;

import java.util.Collection;
import java.util.NoSuchElementException;

import de.htwBerlin.ai.kbe.pojo.Song;


public interface ISongPersistence {

    /**
     * @return List of all songs saved
     */
    Collection<Song> getAllSongs();


    Song getSongByID(Integer id) throws NoSuchElementException;


    Song deleteSong(Integer id) throws NoSuchElementException;


    Integer addSong(Song song);


    boolean updateSong(Song item) throws NoSuchElementException;
}