package com.example.victorojeda.youtubetest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.victorojeda.youtubetest.parsers.YoutubePlaylistParser;
import com.example.victorojeda.youtubetest.requests.HttpManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorojeda on 12/8/14.
 */
public class PlaylistListFragment extends ListFragment {
    private static final String TAG = "PlaylistListFragment";
    private static final String CHANNEL_ID = "https://www.googleapis.com/youtube/v3/playlists?part=contentDetails%2Csnippet&channelId=UC_x5XG1OV2P6uZZ5FSM9Ttw&maxResults=50&key=AIzaSyDd2EgpqubdjOCeZdS0-Hiy9LW7E_31-pc";
                                              //https://www.googleapis.com/youtube/v3/playlists?part=contentDetails%2Csnippet&channelId=UC_x5XG1OV2P6uZZ5FSM9Ttw&maxResults=50&key=AIzaSyDd2EgpqubdjOCeZdS0-Hiy9LW7E_31-pc"
    private static final String LIST_DATA_KEY = "mPlaylistItemList";
    private static final String SELECTED_ROW_KEY = "selectedRow";
    private OnPlaylistSelectedListener mOnPlaylistSelectedListener;
    private PlaylistItemAdapter mPlaylistItemAdapter;
    private List<PlaylistItem> mPlaylistItemList;
    private View mPreviousView; //Used to know the selected row.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnPlaylistSelectedListener) {
            mOnPlaylistSelectedListener = (OnPlaylistSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*for (int i = 0; i < 50; i++) {
            PlaylistItem playlistItem = new PlaylistItem();
            playlistItem.setId("Id test " + i);
            playlistItem.setThumbnailURL("test");
            playlistItem.setTitle("Title test " + i);
            playlistItem.setTotalVideos(4);
            mPlaylistItemList.add(playlistItem);
        }*/
        //Initializing with empty playlist
        mPlaylistItemList = new ArrayList<PlaylistItem>();
        if (savedInstanceState == null) {
            if (isOnline()) {
                //requestData("https://www.googleapis.com/youtube/v3/channels?id=UC_x5XG1OV2P6uZZ5FSM9Ttw&key=AIzaSyDd2EgpqubdjOCeZdS0-Hiy9LW7E_31-pc&part=snippet,contentDetails,statistics");
                requestData(CHANNEL_ID);
            } else {
                Toast.makeText(this.getActivity(), "Network is not available", Toast.LENGTH_LONG).show();
            }
        } else {
            mPlaylistItemList = savedInstanceState.getParcelableArrayList(LIST_DATA_KEY);

        }

        //Setting the adapter
        mPlaylistItemAdapter = new PlaylistItemAdapter(getActivity().getApplicationContext(), R.layout.playlist_item, mPlaylistItemList);
        setListAdapter(mPlaylistItemAdapter);
        if (savedInstanceState != null) {
            mPlaylistItemAdapter.setPositionSelected(savedInstanceState.getInt(SELECTED_ROW_KEY));
        }
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            if(netInfo.getType() != ConnectivityManager.TYPE_WIFI && netInfo.getType() != ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this.getActivity(), "This app does not work without WIFI, do not use Carrier (3G, 4G) internet access", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("NewApi")
    private void requestData(String... uri) {
        //Creating new Task
        BackgroundDownloader task = new BackgroundDownloader();

        //Running serialized or parallel depending on api version
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            task.execute(uri);
        }else{
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        //Setting the color for selected position and cleaning the previously selected.
        if(mPreviousView != null) {
            mPreviousView.setBackgroundColor(getResources().getColor(R.color.list_background));
        } else {
            l.invalidateViews();
        }

        mPlaylistItemAdapter.setPositionSelected(position);
        v.setBackgroundColor(getResources().getColor(R.color.selected_background));
        mPreviousView = v;
        l.invalidateViews();

        Log.d(TAG, "Picture Clicked: " + getListAdapter().getItem(position));
        mOnPlaylistSelectedListener.onPlaylistSelected(((PlaylistItem) getListAdapter().getItem(position)).getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LIST_DATA_KEY, (ArrayList) mPlaylistItemList);
        outState.putInt(SELECTED_ROW_KEY, mPlaylistItemAdapter.getPositionSelected());
        super.onSaveInstanceState(outState);
    }

    private class BackgroundDownloader extends AsyncTask<String, String, List<PlaylistItem>> {


        static final String TAG = "BackgroundDownloader";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<PlaylistItem> doInBackground(String... params) {
            String content = HttpManager.getDataHttpURL(params[0]);
            List<PlaylistItem> instagramDataList = YoutubePlaylistParser.parseFeed(PlaylistListFragment.this.getActivity(), content);

            return instagramDataList;
        }

        @Override
        protected void onPostExecute(List<PlaylistItem> result) {

            if(result == null || "".equals(result)){
                Toast.makeText(PlaylistListFragment.this.getActivity(), "Can not connect to web service", Toast.LENGTH_LONG).show();
                return;
            }

            for(PlaylistItem data : result){
                mPlaylistItemAdapter.add(data);
                mPlaylistItemAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }
}
