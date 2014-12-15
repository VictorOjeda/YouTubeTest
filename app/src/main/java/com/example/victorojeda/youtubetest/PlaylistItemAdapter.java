package com.example.victorojeda.youtubetest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by victorojeda on 12/8/14.
 */
public class PlaylistItemAdapter extends ArrayAdapter<PlaylistItem> {
    public static final String TAG = "PlaylistItemAdapter";

    private int mPositionSelected;

    public PlaylistItemAdapter(Context context, int resource, List<PlaylistItem> objects) {
        super(context, resource, objects);

        mPositionSelected = 0;
    }

    public int getPositionSelected() {
        return mPositionSelected;
    }

    public void setPositionSelected (int positionSelected) {
        mPositionSelected = positionSelected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.playlist_item, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.thumbnailView = (ImageView) convertView.findViewById(R.id.playlist_thumbnail);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.playlist_title);
            viewHolder.totalVideosView = (TextView) convertView.findViewById(R.id.playlist_total_videos);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
            viewHolder.thumbnailView.setImageBitmap(null);
        }

        if(position == mPositionSelected) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.selected_background));
        } else {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.list_background));
        }

        PlaylistItem currentPlaylistItem = getItem(position);

        viewHolder.titleView.setText(currentPlaylistItem.getTitle());
        viewHolder.totalVideosView.setText("You have " + currentPlaylistItem.getTotalVideos() + " videos to watch!");
        viewHolder.imageURL = currentPlaylistItem.getThumbnailURL();

        new ImageLoader().execute(viewHolder);
        return convertView;
    }

    static class ViewHolderItem {
        TextView titleView;
        TextView totalVideosView;
        ImageView thumbnailView;
        String imageURL;
    }

    private class ImageLoader extends AsyncTask<ViewHolderItem, Void, Bitmap> {

        ViewHolderItem mViewHolder;
        @Override
        protected Bitmap doInBackground(ViewHolderItem... params) {
            mViewHolder = params[0];

            InputStream in = null;
            try {
                String imageURL = mViewHolder.imageURL;
                in = (InputStream) new URL(imageURL).getContent();

                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                return BitmapFactory.decodeResource(PlaylistItemAdapter.this.getContext().getResources(), R.drawable.ic_launcher);
            } finally {
                if(in != null){
                    try {
                        in.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return BitmapFactory.decodeResource(PlaylistItemAdapter.this.getContext().getResources(), R.drawable.ic_launcher);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //Display InstagramDataAndView image in the ImageView widget
            if(result != null) {
                mViewHolder.thumbnailView.setImageBitmap(result);
            } else {
                Log.d(TAG, "IMAGE DOES NOT EXIST");
            }
        }
    }
}
