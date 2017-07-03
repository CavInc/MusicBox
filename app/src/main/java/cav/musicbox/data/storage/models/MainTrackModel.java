package cav.musicbox.data.storage.models;

/**
 * Created by cav on 30.06.17.
 */
public class MainTrackModel {
    private String mArtist;
    private String mTrack;
    private String mFile;
    private int id; // номер трека в базе

    public MainTrackModel(String artist, String track) {
        mArtist = artist;
        mTrack = track;
    }

    public MainTrackModel(String artist, String track, String file) {
        mArtist = artist;
        mTrack = track;
        mFile = file;
    }

    public MainTrackModel(int id, String artist, String track, String file) {
        this.id = id;
        mArtist = artist;
        mTrack = track;
        mFile = file;
    }

    public int getId() {
        return id;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getTrack() {
        return mTrack;
    }

    public String getFile() {
        return mFile;
    }
}
