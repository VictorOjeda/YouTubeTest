package com.example.victorojeda.youtubetest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by victorojeda on 12/7/14.
 */
public class YouTubeFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "YouTubeFragment";
    public static final String API_KEY = "AIzaSyDd2EgpqubdjOCeZdS0-Hiy9LW7E_31-pc";

    private String mPlayListId = "PLOU2XLYxmsIJaacrFiQbQGGrPXIWvj1Wr";
    private YouTubePlayer mYouTubePlayer;

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
            Log.d(TAG, "onLoading");
        }

        @Override
        public void onLoaded(String s) {
            Log.d(TAG, "onLoaded");
        }

        @Override
        public void onAdStarted() {
            Log.d(TAG, "onAdStarted");
        }

        @Override
        public void onVideoStarted() {
            Log.d(TAG, "onVideoStarted");
        }

        @Override
        public void onVideoEnded() {
            Log.d(TAG, "onVideoEnded");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Toast.makeText(getActivity().getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
        }
    };

    YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            Log.d(TAG, "onPlaying");
        }

        @Override
        public void onPaused() {
            Log.d(TAG, "onPaused");
        }

        @Override
        public void onStopped() {
            Log.d(TAG, "onStopped");
        }

        @Override
        public void onBuffering(boolean b) {
            Log.d(TAG, "onBuffering");
        }

        @Override
        public void onSeekTo(int i) {
            Log.d(TAG, "onSeekTo");
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate");
        initialize(API_KEY, this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getActivity().getApplicationContext(), "Failed to initialize", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess");
        mYouTubePlayer = youTubePlayer;
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        if (!wasRestored) {
            youTubePlayer.cuePlaylist(mPlayListId);
        }

        Display display = ((WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            youTubePlayer.setFullscreen(true);
        else
            youTubePlayer.setFullscreen(false);
    }

    public void playNewPlaylist(String playlistId) {
        mPlayListId = playlistId;
        mYouTubePlayer.cuePlaylist(mPlayListId);
    }

    public void setFullscreen(boolean fullscreen) {
        if(mYouTubePlayer != null)
            mYouTubePlayer.setFullscreen(fullscreen);
    }

}
