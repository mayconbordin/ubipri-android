package com.gppdi.ubipri.ui;

import android.view.View;
import butterknife.ButterKnife;
import java.util.List;

public class ButterKnives {
    private ButterKnives() {
        throw new AssertionError("No instances");
    }

    private static final ButterKnife.Action<View> HIDE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    public static void hide(List<View> viewList) {
        ButterKnife.apply(viewList, HIDE);
    }
}