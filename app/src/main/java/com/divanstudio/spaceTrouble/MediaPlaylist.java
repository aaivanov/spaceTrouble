package com.divanstudio.spaceTrouble;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WJ_DDA on 25.04.2017.
 */

public class MediaPlaylist {
    // TODO 1) Сейчас музыка играется в треде и там запускается цикл по поиску трека в менеджере
    // TODO Можно оптимизировать поиск, если использовать State здесь или в треде.

    // TODO 2) Можно добавить play(res_id). Тогда можно будет избвавиться от лишнего параметра в конструкторах textControl, imgControl

    /** Словарь звуков. Формат: {"Метка звука": MediaPlayer} */
    private Map<String, MediaPlayer> mediaSources = new HashMap<>();
    private float volume = 1.0f;

    // Конструктор
    public MediaPlaylist () {}

    public void setVolumeSFX (float vol) { this.volume = vol; }

    public void addMedia(Context context, String soundName, int id_res) {
        this.mediaSources.put(soundName, MediaPlayer.create(context, id_res));
    }

    private void playMedia (String soundName, boolean set_looping) {
        this.mediaSources.get(soundName).setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaSources.get(soundName).setLooping(set_looping);
        this.mediaSources.get(soundName).setVolume(this.volume, this.volume);
        this.mediaSources.get(soundName).start();
    }

    public void stop(String soundName, int seek_timer) {
        if (this.mediaSources.get(soundName).isPlaying()) {
            // Останавливаем проигрыватель
            this.mediaSources.get(soundName).stop();

            // Подготавливаем проигрыватель
            try {
                this.mediaSources.get(soundName).prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Трек ставим сначала
            this.mediaSources.get(soundName).seekTo(seek_timer);
        }
    }

    public void stop(String soundName) {
        this.stop(soundName, 0);
    }

    public void stop() {
        for (String Key : this.mediaSources.keySet()) {
            this.stop(Key);
        }
    }

    public void pause(String soundName){
        if (this.mediaSources.get(soundName).isPlaying()) {
            this.mediaSources.get(soundName).pause();
        }
    }

    public void pause() {
        for (String Key : this.mediaSources.keySet()) {
            this.pause(Key);
        }
    }

    public int play(String soundName, boolean is_reset, boolean set_looping, boolean is_solo) {
        // Поиск в хэше.
        // Если трек должен быть solo - останавливаем остальные треки
        boolean found_key = false;
        for (String Key : this.mediaSources.keySet()) {
            if (soundName.equals(Key)) {
                found_key = true;
                continue;
            }

            if (is_solo) {
                this.stop(Key);
            }
        }

        // Если трек нашли, играем его
        if (found_key) {
            // Если трек играется и его не нужно сбрасывать, то ничего не делаем
            if ((!is_reset) && this.mediaSources.get(soundName).isPlaying()) { return 0; }

            // Если трек нужно сбросить, когда он играется, сбрасываем
            if (is_reset && this.mediaSources.get(soundName).isPlaying()) {
                this.stop(soundName);
                this.playMedia(soundName, set_looping);
            }

            // Если трек не играется - играем его
            if (!this.mediaSources.get(soundName).isPlaying()) {
                this.playMedia(soundName, set_looping);
            }

            return 0;
        }

        return 1;
    }

    public int play(String soundName, boolean is_reset, boolean set_looping) {
        return this.play(soundName, is_reset, set_looping, false);
    }

    public int play(String soundName, boolean is_reset) {
        return this.play(soundName, is_reset, false);
    }

    public int play(String soundName) {
        return this.play(soundName, false);
    }

    public void clear() {
        this.mediaSources = null;
    }
}
