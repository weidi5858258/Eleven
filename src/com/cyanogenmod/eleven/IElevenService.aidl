package com.cyanogenmod.eleven;

import com.cyanogenmod.eleven.service.MusicPlaybackTrack;

interface IElevenService
{



    void openFile(String path);
    void open(in long [] list, int position, long sourceId, int sourceType);
    void play();
    void pause();
    void stop();
    void prev(boolean forcePrevious);
    void next();
    void seekRelative(long deltaInMs);
    void setQueuePosition(int index);
    void setRepeatMode(int repeatmode);
    void setShuffleMode(int shufflemode);
    void moveQueueItem(int from, int to);
    void refresh();
    void playlistChanged();
    void setShakeToPlayEnabled(boolean enabled);
    void setLockscreenAlbumArt(boolean enabled);
    void enqueue(in long [] list, int action, long sourceId, int sourceType);

    int getQueueSize();
    int getQueuePosition();
    int getQueueHistoryPosition(int position);
    int getQueueHistorySize();
    int[] getQueueHistoryList();
    int getRepeatMode();
    int getShuffleMode();
    int getAudioSessionId();
    int getMediaMountedCount();
    int removeTrack(long id);
    int removeTracks(int first, int last);

    // 歌曲长度
    long duration();
    // 歌曲播放到哪个位置了
    long position();
    // 跳到哪个地方开始播放
    long seek(long pos);
    long getAudioId();
    long getPreviousAudioId();
    long getNextAudioId();
    long getArtistId();
    long getAlbumId();
    long getQueueItemAtPosition(int position);
    long [] getQueue();

    boolean isPlaying();
    boolean removeTrackAtPosition(long id, int position);

    String getPath();
    // 艺术家
    String getArtistName();
    // 轨道，音轨
    String getTrackName();
    // 专辑
    String getAlbumName();

    MusicPlaybackTrack getCurrentTrack();
    MusicPlaybackTrack getTrack(int index);
}
