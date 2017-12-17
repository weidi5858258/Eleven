/*
* Copyright (C) 2014 The CyanogenMod Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.cyanogenmod.eleven.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.cyanogenmod.eleven.Config;

/**
 * This is used by the music playback service to track the music tracks it is playing
 * It has extra meta data to determine where the track came from so that we can show the appropriate
 * song playing indicator
 * openFile: path = content://media/external/audio/media/197148
 * MusicPlaybackTrack{mId=197148, mSourceId=-1, mSourceType=NA, mSourcePosition=0}
 *
 MusicPlaybackTrack{mId=197119, mSourceId=-1, mSourceType=NA, mSourcePosition=0}
 MusicPlaybackTrack{mId=197162, mSourceId=-1, mSourceType=NA, mSourcePosition=1}
 MusicPlaybackTrack{mId=234385, mSourceId=-1, mSourceType=NA, mSourcePosition=2}
 ......
 MusicPlaybackTrack{mId=197198, mSourceId=-1, mSourceType=NA, mSourcePosition=1917}
 MusicPlaybackTrack{mId=197235, mSourceId=-1, mSourceType=NA, mSourcePosition=1918}
 MusicPlaybackTrack{mId=197179, mSourceId=-1, mSourceType=NA, mSourcePosition=1919}

 234409|/storage/2430-1702/BaiduNetdisk/music/缈熸儬姘?- 閾佺獥娉?mp3|
 缈熸儬姘?- 閾佺獥娉?mp3|19900257|audio/mpeg|1509799931|0|1509799930|
 閾佺獥娉聥Xr|497450|94||88|0||0|1|0|0|0|||94W 聼 聙 |杩熷織寮簗88聥Xr 197663025|閾佺獥娉?
 */
public class MusicPlaybackTrack implements Parcelable {
    /**
     * The track id
     */
    public long mId;

    /**
     * Where was this track added from? Artist id/Album id/Playlist id
     */
    public long mSourceId;

    /**
     * Where was this track added from?  Artist/Album/Playlist
     *
     IdType.NA,(0)
     IdType.Artist,(1)
     IdType.Album,(2)
     IdType.Playlist;(3)
     */
    public Config.IdType mSourceType;

    /**
     * This is only used for playlists since it is possible that a playlist can contain the same
     * song multiple times.  So to prevent the song indicator showing up multiple times, we need
     * to keep track of the position
     */
    public int mSourcePosition;

    /**
     * Parcelable creator
     */
    public static final Creator<MusicPlaybackTrack> CREATOR = new Creator<MusicPlaybackTrack>() {
        @Override
        public MusicPlaybackTrack createFromParcel(Parcel source) {
            return new MusicPlaybackTrack(source);
        }

        @Override
        public MusicPlaybackTrack[] newArray(int size) {
            return new MusicPlaybackTrack[size];
        }
    };

    public MusicPlaybackTrack(long id, long sourceId, Config.IdType type, int sourcePosition) {
        mId = id;
        mSourceId = sourceId;
        mSourceType = type;
        mSourcePosition = sourcePosition;
    }

    public MusicPlaybackTrack(Parcel in) {
        mId = in.readLong();
        mSourceId = in.readLong();
        mSourceType = Config.IdType.getTypeById(in.readInt());
        mSourcePosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mSourceId);
        dest.writeInt(mSourceType.mId);
        dest.writeInt(mSourcePosition);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MusicPlaybackTrack) {
            MusicPlaybackTrack other = (MusicPlaybackTrack)o;
            if (other != null) {
                if (mId == other.mId
                        && mSourceId == other.mSourceId
                        && mSourceType == other.mSourceType
                        && mSourcePosition == other.mSourcePosition) {
                    return true;
                }

                return false;
            }
        }

        return super.equals(o);
    }

    @Override
    public String toString() {
        return "MusicPlaybackTrack{" +
                "mId=" + mId +
                ", mSourceId=" + mSourceId +
                ", mSourceType=" + mSourceType +
                ", mSourcePosition=" + mSourcePosition +
                '}';
    }
}
