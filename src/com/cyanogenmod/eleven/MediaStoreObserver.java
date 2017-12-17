package com.cyanogenmod.eleven;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.weidi.utils.EventBusUtils;

/**
 * Created by weidi5858258 on 2017/12/16.
 */

public class MediaStoreObserver extends ContentObserver implements Runnable {

    // milliseconds to delay before calling refresh to aggregate events
    private static final long REFRESH_DELAY = 500;
    private Handler mHandler;

    public MediaStoreObserver(Handler handler) {
        super(handler);
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        // if a change is detected, remove any scheduled callback
        // then post a new one. This is intended to prevent closely
        // spaced events from generating multiple refresh calls
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, REFRESH_DELAY);
    }

    @Override
    public void run() {
        // actually call refresh when the delayed callback fires
        Log.e("ELEVEN", "calling refresh!");
        // refresh();
        EventBusUtils.postAsync(
                MusicPlaybackService.class,
                MusicPlaybackService.REFRESH,
                null);
    }

}
