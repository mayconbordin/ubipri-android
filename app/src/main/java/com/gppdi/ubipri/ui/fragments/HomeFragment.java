package com.gppdi.ubipri.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.functionality.FunctionalityManager;
import static com.gppdi.ubipri.location.LocationConstants.*;

import com.gppdi.ubipri.ui.activities.BaseActivity;
import com.gppdi.ubipri.ui.adapter.FunctionalityAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author mayconbordin
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";

    @InjectView(R.id.listFunctionalities) ListView listFunctionalities;
    @InjectView(R.id.txtEnvironmentName) TextView environmentName;

    @Inject FunctionalityManager functionalityManager;
    private FunctionalityAdapter adapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onEnvironmentChange(intent);
        }
    };

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

    @Override
    public void onResume() {
        super.onResume();

        // register listener for changes on the environment
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(EVENT_ENVIRONMENT_CHANGED));
    }

    protected void onEnvironmentChange(Intent intent) {
        Log.i(TAG, "Updating list of functionalities.");
        adapter.notifyDataSetChanged();

        environmentName.setText(intent.getStringExtra(ENVIRONMENT_NAME));
    }
}
