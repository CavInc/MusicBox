package cav.musicbox.data.storage.models;

/**
 * Created by cav on 30.06.17.
 */
public class MainTrackModel {
    private String mArtist;
    private String mTrack;
    private String mFile;

    public MainTrackModel(String artist, String track) {
        mArtist = artist;
        mTrack = track;
    }

    public MainTrackModel(String artist, String track, String file) {
        mArtist = artist;
        mTrack = track;
        mFile = file;
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
