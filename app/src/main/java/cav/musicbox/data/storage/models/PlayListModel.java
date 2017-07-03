package cav.musicbox.data.storage.models;

/**
 * Created by cav on 03.07.17.
 */
public class PlayListModel {
    private int mId;
    private String mTitle;
    private int mVolume;

    public PlayListModel(int id, String title, int volume) {
        mId = id;
        mTitle = title;
        mVolume = volume;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getVolume() {
        return mVolume;
    }
}
