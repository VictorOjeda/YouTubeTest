package com.example.victorojeda.youtubetest;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;



public class YouTubeMainActivity extends ActionBarActivity implements OnPlaylistSelectedListener{
    private static final String TAG = "YouTubeMainActivity";
    private static final String YOUTUBE_PLAYER_FRAGMENT = "YouTubeFragment";
    private static final String CHANNEL_LIST_FRAGMENT = "ChannelFragment";

    private YouTubeFragment mYouTubeFragment;
    private PlaylistListFragment mPlaylistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_main);

        if (findViewById(R.id.fragment_container) != null) {

            // If device was rotated retrieve fragments instead of creating them.
            if (savedInstanceState != null) {
                Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int rotation = display.getRotation();

                mYouTubeFragment = (YouTubeFragment) getSupportFragmentManager().findFragmentByTag(YOUTUBE_PLAYER_FRAGMENT);
                mPlaylistFragment = (PlaylistListFragment) getSupportFragmentManager().findFragmentByTag(CHANNEL_LIST_FRAGMENT);
                return;
            }

            //Create youtube fragment
            if(findViewById(R.id.video_container) != null) {
                mYouTubeFragment = new YouTubeFragment();
                mYouTubeFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.video_container, mYouTubeFragment, YOUTUBE_PLAYER_FRAGMENT).commit();
            }

            //Create playlist listfragment
            if(findViewById(R.id.list_container) != null) {
                mPlaylistFragment = new PlaylistListFragment();
                mPlaylistFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.list_container, mPlaylistFragment, CHANNEL_LIST_FRAGMENT).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.you_tube_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaylistSelected(String playlistID) {
        //Update the selected playlist
        Log.d(TAG, "onPlaylistSelected video id: " + playlistID);
        mYouTubeFragment.playNewPlaylist(playlistID);
    }
}
