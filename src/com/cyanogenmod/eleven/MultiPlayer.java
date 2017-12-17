package com.cyanogenmod.eleven;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.cyanogenmod.eleven.utils.SrtManager;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by weidi5858258 on 2017/12/15.
 */

public class MultiPlayer implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "MultiPlayer";
    private static final int TRACK_ENDED = 1;
    private static final int TRACK_WENT_TO_NEXT = 2;
    private static final int SERVER_DIED = 3;
    private static final int LYRICS = 7;

    // Context
    private final WeakReference<MusicPlaybackService> mService;

    private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();

    private MediaPlayer mNextMediaPlayer;

    private Handler mHandler;

    private boolean mIsInitialized = false;

    private SrtManager mSrtManager;

    private String mNextMediaPath;

    /**
     * Constructor of <code>MultiPlayer</code>
     */
    public MultiPlayer(final MusicPlaybackService service) {
        mService = new WeakReference<MusicPlaybackService>(service);
        mSrtManager = new SrtManager() {

            @Override
            public void onTimedText(String text) {
                mHandler.obtainMessage(LYRICS, text).sendToTarget();
            }
        };
    }

    /**
     * @param path The path of the file, or the http/rtsp URL of the stream
     *             you want to play
     */
    public void setDataSource(final String path) {
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
        if (mIsInitialized) {
            loadSrt(path);
            setNextDataSource(null);
        }
    }

    private void loadSrt(final String path) {
        mSrtManager.reset();

        Uri uri = Uri.parse(path);
        String filePath = null;

        if (path.startsWith("content://")) {
            // resolve the content resolver path to a file path
            Cursor cursor = null;
            try {
                final String[] proj = {MediaStore.Audio.Media.DATA};
                cursor = mService.get().getContentResolver().query(
                        uri,
                        proj,
                        null,
                        null,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    filePath = cursor.getString(0);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        } else {
            filePath = uri.getPath();
        }

        if (!TextUtils.isEmpty(filePath)) {
            final int lastIndex = filePath.lastIndexOf('.');
            if (lastIndex != -1) {
                String newPath = filePath.substring(0, lastIndex) + ".srt";
                final File f = new File(newPath);

                mSrtManager.initialize(mCurrentMediaPlayer, f);
            }
        }
    }

    /**
     * @param player The {@link MediaPlayer} to use
     * @param path   The path of the file, or the http/rtsp URL of the stream
     *               you want to play
     * @return True if the <code>player</code> has been prepared and is
     * ready to play, false otherwise
     */
    private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
        try {
            player.reset();
            player.setOnPreparedListener(null);
            if (path.startsWith("content://")) {
                player.setDataSource(mService.get(), Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            player.prepare();
        } catch (final IOException todo) {
            // TODO: notify the user why the file couldn't be opened
            return false;
        } catch (final IllegalArgumentException todo) {
            // TODO: notify the user why the file couldn't be opened
            return false;
        }
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        return true;
    }

    /**
     * Set the MediaPlayer to start when this MediaPlayer finishes playback.
     *
     * @param path The path of the file, or the http/rtsp URL of the stream
     *             you want to play
     */
    public void setNextDataSource(final String path) {
        mNextMediaPath = null;
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null);
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "Next media player is current one, continuing");
        } catch (IllegalStateException e) {
            Log.e(TAG, "Media player not initialized!");
            return;
        }
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer.release();
            mNextMediaPlayer = null;
        }
        if (path == null) {
            return;
        }
        mNextMediaPlayer = new MediaPlayer();
        mNextMediaPlayer.setAudioSessionId(getAudioSessionId());
        if (setDataSourceImpl(mNextMediaPlayer, path)) {
            mNextMediaPath = path;
            mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
        } else {
            if (mNextMediaPlayer != null) {
                mNextMediaPlayer.release();
                mNextMediaPlayer = null;
            }
        }
    }

    /**
     * Sets the handler
     *
     * @param handler The handler to use
     */
    public void setHandler(final Handler handler) {
        mHandler = handler;
    }

    /**
     * @return True if the player is ready to go, false otherwise
     */
    public boolean isInitialized() {
        return mIsInitialized;
    }

    /**
     * Starts or resumes playback.
     */
    public void start() {
        mCurrentMediaPlayer.start();
        mSrtManager.play();
    }

    /**
     * Pauses playback. Call start() to resume.
     */
    public void pause() {
        mCurrentMediaPlayer.pause();
        mSrtManager.pause();
    }

    /**
     * Resets the MediaPlayer to its uninitialized state.
     */
    public void stop() {
        mCurrentMediaPlayer.reset();
        mSrtManager.reset();
        mIsInitialized = false;
    }

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    public void release() {
        mCurrentMediaPlayer.release();
        mSrtManager.release();
        mSrtManager = null;
    }

    /**
     * Gets the duration of the file.
     *
     * @return The duration in milliseconds
     */
    public long duration() {
        return mCurrentMediaPlayer.getDuration();
    }

    /**
     * Gets the current playback position.
     *
     * @return The current position in milliseconds
     */
    public long position() {
        return mCurrentMediaPlayer.getCurrentPosition();
    }

    /**
     * Gets the current playback position.
     *
     * @param whereto The offset in milliseconds from the start to seek to
     * @return The offset in milliseconds from the start to seek to
     */
    public long seek(final long whereto) {
        mCurrentMediaPlayer.seekTo((int) whereto);
        mSrtManager.seekTo(whereto);
        return whereto;
    }

    /**
     * Sets the volume on this player.
     *
     * @param vol Left and right volume scalar
     */
    public void setVolume(final float vol) {
        mCurrentMediaPlayer.setVolume(vol, vol);
    }

    /**
     * Sets the audio session ID.
     *
     * @param sessionId The audio session ID
     */
    public void setAudioSessionId(final int sessionId) {
        mCurrentMediaPlayer.setAudioSessionId(sessionId);
    }

    /**
     * Returns the audio session ID.
     *
     * @return The current audio session ID.
     */
    public int getAudioSessionId() {
        return mCurrentMediaPlayer.getAudioSessionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onError(final MediaPlayer mp, final int what, final int extra) {
        Log.w(TAG, "Music Server Error what: " + what + " extra: " + extra);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                final MusicPlaybackService service = mService.get();
                final TrackErrorInfo errorInfo =
                        new TrackErrorInfo(service.getAudioId(), service.getTrackName());

                mIsInitialized = false;
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = new MediaPlayer();
                Message msg = mHandler.obtainMessage(SERVER_DIED, errorInfo);
                mHandler.sendMessageDelayed(msg, 2000);
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCompletion(final MediaPlayer mp) {
        if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
            mCurrentMediaPlayer.release();
            mCurrentMediaPlayer = mNextMediaPlayer;
            loadSrt(mNextMediaPath);
            mNextMediaPath = null;
            mNextMediaPlayer = null;
            mHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT);
        } else {
            mHandler.sendEmptyMessage(TRACK_ENDED);
        }
    }

}
