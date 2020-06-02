package com.esgipa.smartplayer.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.esgipa.smartplayer.R;
import com.esgipa.smartplayer.data.model.Song;
import com.esgipa.smartplayer.music.MetaDataExtractor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SongSharedViewModel extends ViewModel {
    private MetaDataExtractor metaDataExtractor;
    private MutableLiveData<List<Song>> songList;
    private MutableLiveData<Song> currentSong;
    private MutableLiveData<Integer> currentSongIndex;

    public SongSharedViewModel() {
        songList = new MutableLiveData<>();
        currentSong = new MutableLiveData<>();
        currentSongIndex = new MutableLiveData<>();
    }

    public void setContext(Context context) {
        metaDataExtractor = new MetaDataExtractor(context);
    }
    public LiveData<List<Song>> getSongList() {
        loadSongs();
        return songList;
    }

    public LiveData<Song> getSong(int position) {
        loadSongs();
        if(songList.getValue() != null) {
            currentSong.setValue(songList.getValue().get(position));
        }
        return currentSong;
    }

    public LiveData<Integer> getCurrentSongIndex() {
        return currentSongIndex;
    }

    public void setCurrentSongIndex(int position) {
        currentSongIndex.postValue(position);
    }

    public void setNextSong(int position) {
        if (position == songList.getValue().size() - 1) {
            setCurrentSongIndex(0);
        } else {
            setCurrentSongIndex(++position);
        }
    }

    public void setPreviousSong(int position) {
        if (position == 0) {
            setCurrentSongIndex(songList.getValue().size() - 1);
        } else {
            setCurrentSongIndex(--position);
        }
    }

    private void loadSongs() {
        List<Song> localSongList = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();

        for (Field field : fields) {
            try {
                int resourceId = field.getInt(field);
                localSongList.add(metaDataExtractor.extract(resourceId));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        songList.setValue(localSongList);
    }
}