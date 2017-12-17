package com.cyanogenmod.eleven;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weidi5858258 on 2017/12/16.
 */

public class QueueUpdateTask extends AsyncTask<Void, Void, List<MediaSession.QueueItem>> {

    private Context mContext;
    private MediaSession mSession;
    private long[] mQueue;

    public QueueUpdateTask(Context context, MediaSession session, long[] queue) {
        this.mContext = context;
        this.mSession = session;
        mQueue = queue != null ? Arrays.copyOf(queue, queue.length) : null;
    }

    @Override
    protected List<MediaSession.QueueItem> doInBackground(Void... params) {
        if (mQueue == null || mQueue.length == 0) {
            return null;
        }

        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.Media._ID).append(" IN (");
        for (int i = 0; i < mQueue.length; i++) {
            if (i != 0) {
                selection.append(",");
            }
            selection.append(mQueue[i]);
        }
        selection.append(")");

        Cursor c = this.mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.AudioColumns._ID,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST},
                selection.toString(), null, null);
        if (c == null) {
            return null;
        }

        try {
            LongSparseArray<MediaDescription> descsById = new LongSparseArray<>();
            final int idColumnIndex = c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID);
            final int titleColumnIndex = c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);
            final int artistColumnIndex = c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST);

            while (c.moveToNext() && !isCancelled()) {
                final MediaDescription desc = new MediaDescription.Builder()
                        .setTitle(c.getString(titleColumnIndex))
                        .setSubtitle(c.getString(artistColumnIndex))
                        .build();
                final long id = c.getLong(idColumnIndex);
                descsById.put(id, desc);
            }

            List<MediaSession.QueueItem> items = new ArrayList<>();
            for (int i = 0; i < mQueue.length; i++) {
                MediaDescription desc = descsById.get(mQueue[i]);
                if (desc == null) {
                    // shouldn't happen except in corner cases like
                    // music being deleted while we were processing
                    desc = new MediaDescription.Builder().build();
                }
                items.add(new MediaSession.QueueItem(desc, i));
            }
            return items;
        } finally {
            c.close();
        }
    }

    @Override
    protected void onPostExecute(List<MediaSession.QueueItem> items) {
        if (!isCancelled()) {
            mSession.setQueue(items);
        }
    }

}
