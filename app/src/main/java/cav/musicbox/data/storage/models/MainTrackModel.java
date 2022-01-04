package cav.musicbox.data.storage.models;

/**
 * Created by cav on 30.06.17.
 */
public class MainTrackModel {
    private String mArtist;
    private String mTrack;
    private String mFile;
    private String mAlbum;
    private int id; // номер трека в базе
    private int mPlayList; // плейлист

    public MainTrackModel(String artist, String track) {
        mArtist = artist;
        mTrack = track;
    }

    public MainTrackModel(String artist, String track, String file) {
        mArtist = artist;
        mTrack = track;
        mFile = file;
    }

    public MainTrackModel(String artist, String track, String album, String file) {
        mArtist = artist;
        mTrack = track;
        mAlbum = album;
        mFile = file;
    }

    public MainTrackModel(int id, String artist, String track, String file, int playList) {
        this.id = id;
        mArtist = artist;
        mTrack = track;
        mFile = file;
        mPlayList =playList;
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

    public int getPlayList() {
        return mPlayList;
    }

    public String getAlbum() {
        return mAlbum;
    }
}
