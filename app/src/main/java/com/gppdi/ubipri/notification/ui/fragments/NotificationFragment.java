package com.gppdi.ubipri.notification.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.utils.NotificationUtil;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FORMAT = "format";
    private static final String ARG_TIMESTAMP = "timestamp";
    private static final String ARG_MESSAGE = "message";

    private int format;
    private long timestamp;
    private String message;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param format Notification format.
     * @param timestamp Notification timestamp.
     * @param message Notification message.
     * @return A new instance of fragment NotificationFragment.
     */
    public static NotificationFragment newInstance(int format, long timestamp, String message) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FORMAT, format);
        args.putLong(ARG_TIMESTAMP, timestamp);
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            format = getArguments().getInt(ARG_FORMAT);
            timestamp = getArguments().getLong(ARG_TIMESTAMP);
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        ImageView formatImageView = (ImageView) rootView.findViewById(R.id.notificationFormatImage);
        TextView titleTextView = (TextView) rootView.findViewById(R.id.notificationFormat);
        TextView timeTextView = (TextView) rootView.findViewById(R.id.notificationTime);
        TextView messageTextView = (TextView) rootView.findViewById(R.id.notificationMessage);

        formatImageView.setImageResource(NotificationUtil.getNotificationIcon(format));
        titleTextView.setText(NotificationUtil.getNotificationFormatString(format));
        timeTextView.setText(SimpleDateFormat.getDateInstance().format(timestamp));
        messageTextView.setText(message);

        return rootView;
    }
}
