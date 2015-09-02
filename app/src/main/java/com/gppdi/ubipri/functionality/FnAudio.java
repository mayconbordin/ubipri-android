package com.gppdi.ubipri.functionality;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author mayconbordin
 */
public abstract class FnAudio<T> extends FnAbstract<T> {
    protected AudioManager mAudioManager;

    public FnAudio(Context ctx) {
        super(ctx);

        mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean exists() {
        return (mAudioManager != null);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
