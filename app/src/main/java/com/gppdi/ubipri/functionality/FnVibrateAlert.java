package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author mayconbordin
 */
public class FnVibrateAlert extends FnAudio<Boolean> {
    public FnVibrateAlert(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean isEnabled() {
        return (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE);
    }

    @Override
    public void toggle(Boolean value) {
        if (value) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
