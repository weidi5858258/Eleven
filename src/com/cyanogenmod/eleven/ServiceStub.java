package com.cyanogenmod.eleven;

import android.os.RemoteException;

import com.cyanogenmod.eleven.service.MusicPlaybackTrack;

import java.lang.ref.WeakReference;

/**
 * Created by weidi5858258 on 2017/12/16.
 */
public class ServiceStub extends IElevenService.Stub {

    private final WeakReference<MusicPlaybackService> mService;

    public ServiceStub(final MusicPlaybackService service) {
        mService = new WeakReference<MusicPlaybackService>(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openFile(final String path) throws RemoteException {
        mService.get().openFile(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(final long[] list, final int position, long sourceId, int sourceType)
            throws RemoteException {
        mService.get().open(list, position, sourceId, Config.IdType.getTypeById(sourceType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() throws RemoteException {
        mService.get().stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() throws RemoteException {
        mService.get().pause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play() throws RemoteException {
        mService.get().play();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prev(boolean forcePrevious) throws RemoteException {
        mService.get().prev(forcePrevious);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void next() throws RemoteException {
        mService.get().gotoNext(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enqueue(final long[] list, final int action, long sourceId, int sourceType)
            throws RemoteException {
        mService.get().enqueue(list, action, sourceId, Config.IdType.getTypeById(sourceType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setQueuePosition(final int index) throws RemoteException {
        mService.get().setQueuePosition(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShuffleMode(final int shufflemode) throws RemoteException {
        mService.get().setShuffleMode(shufflemode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRepeatMode(final int repeatmode) throws RemoteException {
        mService.get().setRepeatMode(repeatmode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveQueueItem(final int from, final int to) throws RemoteException {
        mService.get().moveQueueItem(from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh() throws RemoteException {
        mService.get().refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playlistChanged() throws RemoteException {
        mService.get().playlistChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaying() throws RemoteException {
        return mService.get().isPlaying();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] getQueue() throws RemoteException {
        return mService.get().getQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getQueueItemAtPosition(int position) throws RemoteException {
        return mService.get().getQueueItemAtPosition(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueueSize() throws RemoteException {
        return mService.get().getQueueSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueueHistoryPosition(int position) throws RemoteException {
        return mService.get().getQueueHistoryPosition(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueueHistorySize() throws RemoteException {
        return mService.get().getQueueHistorySize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getQueueHistoryList() throws RemoteException {
        return mService.get().getQueueHistoryList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long duration() throws RemoteException {
        return mService.get().duration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() throws RemoteException {
        return mService.get().position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long seek(final long position) throws RemoteException {
        return mService.get().seek(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seekRelative(final long deltaInMs) throws RemoteException {
        mService.get().seekRelative(deltaInMs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAudioId() throws RemoteException {
        return mService.get().getAudioId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MusicPlaybackTrack getCurrentTrack() throws RemoteException {
        return mService.get().getCurrentTrack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MusicPlaybackTrack getTrack(int index) throws RemoteException {
        return mService.get().getTrack(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNextAudioId() throws RemoteException {
        return mService.get().getNextAudioId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getPreviousAudioId() throws RemoteException {
        return mService.get().getPreviousAudioId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getArtistId() throws RemoteException {
        return mService.get().getArtistId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAlbumId() throws RemoteException {
        return mService.get().getAlbumId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getArtistName() throws RemoteException {
        return mService.get().getArtistName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTrackName() throws RemoteException {
        return mService.get().getTrackName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlbumName() throws RemoteException {
        return mService.get().getAlbumName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() throws RemoteException {
        return mService.get().getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueuePosition() throws RemoteException {
        return mService.get().getQueuePosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getShuffleMode() throws RemoteException {
        return mService.get().getShuffleMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRepeatMode() throws RemoteException {
        return mService.get().getRepeatMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeTracks(final int first, final int last) throws RemoteException {
        return mService.get().removeTracks(first, last);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeTrack(final long id) throws RemoteException {
        return mService.get().removeTrack(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeTrackAtPosition(final long id, final int position)
            throws RemoteException {
        return mService.get().removeTrackAtPosition(id, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMediaMountedCount() throws RemoteException {
        return mService.get().getMediaMountedCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAudioSessionId() throws RemoteException {
        return mService.get().getAudioSessionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShakeToPlayEnabled(boolean enabled) {
        mService.get().setShakeToPlayEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLockscreenAlbumArt(boolean enabled) {
        mService.get().setLockscreenAlbumArt(enabled);
    }

}
