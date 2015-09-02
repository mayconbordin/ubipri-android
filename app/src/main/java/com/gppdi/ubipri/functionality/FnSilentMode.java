package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author mayconbordin
 */
public class FnSilentMode extends FnAudio<Boolean> {
    public FnSilentMode(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean isEnabled() {
        return (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT);
    }

    @Override
    public void toggle(Boolean value) {
        if (value) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
