package com.gppdi.ubipri.maps;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.gppdi.ubipri.R;

public class MarkerInfoWindowAdapter implements InfoWindowAdapter {
    private Context mContext;

    public MarkerInfoWindowAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return View.inflate(mContext, R.layout.environment_info_window, null);
    }
}