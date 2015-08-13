package com.gppdi.ubipri.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gppdi.ubipri.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mayconbordin
 */
public class HomeFragment extends Fragment {
    private ListView listFunctionalities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        List<String> items = new ArrayList<>();
        items.add("Camera");
        items.add("WiFi");


        listFunctionalities = (ListView) rootView.findViewById(R.id.listFunctionalities);
        listFunctionalities.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.functionality_row, R.id.itemName, items));

        return rootView;
    }
}
