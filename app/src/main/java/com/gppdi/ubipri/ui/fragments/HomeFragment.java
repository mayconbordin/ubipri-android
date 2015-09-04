package com.gppdi.ubipri.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.functionality.FunctionalityManager;
import com.gppdi.ubipri.ui.adapter.FunctionalityAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author mayconbordin
 */
public class HomeFragment extends BaseFragment {
    @InjectView(R.id.listFunctionalities) ListView listFunctionalities;
    @Inject FunctionalityManager functionalityManager;
    private FunctionalityAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new FunctionalityAdapter(getActivity(), functionalityManager.getSupportedFunctionalitiesAsItems());
        listFunctionalities.setAdapter(adapter);
    }
}
