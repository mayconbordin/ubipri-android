package com.gppdi.ubipri.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.gppdi.ubipri.ui.activities.BaseActivity;

import butterknife.ButterKnife;

/**
 * @author mayconbordin
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject extras
        Dart.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle inState) {
        super.onViewCreated(view, inState);

        // Inject views
        ButterKnife.inject(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inject ourselves into the activity graph
        BaseActivity.get(this).inject(this);
    }

    @Override
    public void onDestroyView() {
        // Clear view references
        ButterKnife.reset(this);

        super.onDestroyView();
    }
}
