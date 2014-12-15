package com.example.victorojeda.youtubetest;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by victorojeda on 12/8/14.
 */
public class PlaylistItem implements Parcelable {
    private String mTitle;
    private String mId;
    private int mTotalVideos;
    private String mThumbnailURL;

    public PlaylistItem () {

    }

    public PlaylistItem (String title, String id, int totalVideos, String thumbnailURL) {
        mTitle = title;
        mId = id;
        mTotalVideos = totalVideos;
        mThumbnailURL = thumbnailURL;
    }

    protected PlaylistItem(Parcel in) {
        mTitle = in.readString();
        mId = in.readString();
        mTotalVideos = in.readInt();
        mThumbnailURL = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mId);
        dest.writeInt(mTotalVideos);
        dest.writeString(mThumbnailURL);
    }

    public static final Parcelable.Creator<PlaylistItem> CREATOR = new Parcelable.Creator<PlaylistItem>() {
        @Override
        public PlaylistItem createFromParcel(Parcel in) {
            return new PlaylistItem(in);
        }

        @Override
        public PlaylistItem[] newArray(int size) {
            return new PlaylistItem[size];
        }
    };

    public  String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public int getTotalVideos() {
        return mTotalVideos;
    }

    public void setTotalVideos(int mTotalVideos) {
        this.mTotalVideos = mTotalVideos;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setThumbnailURL(String mThumbnailURL) {
        this.mThumbnailURL = mThumbnailURL;
    }
}
