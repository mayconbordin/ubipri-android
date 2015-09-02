package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author mayconbordin
 */
public class FnMediaVolume extends FnAudio<Boolean> {
    public FnMediaVolume(Context ctx) {
        super(ctx);
    }

    @Override
    public void toggle(Boolean value) {
        if (value) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_ALLOW_RINGER_MODES);
        }
    }
}
