package de.htw.ai.kbe.servlet.pojo;


/**
 * Song class for XML song elements
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Song {

    private Integer id;
    @NotNull
    private String title;
    private String artist;
    private String album;
    private Integer released;


    public Song(Integer id, String title, String artist, String album, Integer released) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.released = released;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getReleased() {
        return released;
    }

    public void setReleased(Integer released) {
        this.released = released;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", released=" + released +
                '}';
    }
}

