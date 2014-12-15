package com.example.victorojeda.youtubetest.parsers;

import android.content.Context;
import android.util.Log;

import com.example.victorojeda.youtubetest.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorojeda on 12/8/14.
 */
public class YoutubePlaylistParser {

    private static final String TAG = "YoutubePlaylistParser";

    public static List<PlaylistItem> parseFeed(Context context, String content){

        try {
            JSONObject response = new JSONObject(content);
            JSONArray data = response.getJSONArray("items");
            List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                PlaylistItem playlistItem = new PlaylistItem();

                playlistItem.setId(obj.getString("id"));
                playlistItem.setTitle(obj.getJSONObject("snippet").getString("title"));
                playlistItem.setTotalVideos(obj.getJSONObject("contentDetails").getInt("itemCount"));
                playlistItem.setThumbnailURL(obj.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"));

                playlistItemList.add(playlistItem);
            }

            return playlistItemList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
