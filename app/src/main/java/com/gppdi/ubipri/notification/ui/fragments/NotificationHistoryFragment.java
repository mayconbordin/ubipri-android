package com.gppdi.ubipri.notification.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.api.NotificationClient;
import com.gppdi.ubipri.notification.data.dao.NotificationDAO;
import com.gppdi.ubipri.notification.data.models.Notification;
import com.gppdi.ubipri.notification.services.NotificationService;
import com.gppdi.ubipri.notification.ui.adapters.NotificationAdapter;

import java.util.List;

public class NotificationHistoryFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification_hist, container, false);
        listView = (ListView) rootView.findViewById(R.id.listNotifications);

        updateHistory();
        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Notification notification = (Notification) parent.getItemAtPosition(position);
                // TODO: 31/08/15 Display the full message
            }
        });

        return rootView;
    }

    private void populateListView() {
        // Load messages from the database
        List<Notification> notifications = new NotificationDAO().newest();

        // Add the messages to the ListView through an adapter
        NotificationAdapter adapter = new NotificationAdapter(this.getActivity(), R.layout.row_notification_hist, notifications);
        listView.setAdapter(adapter);
    }

    private void updateHistory() {
        // Get the new messages from the webservice
        NotificationClient client = NotificationService.createService(NotificationClient.class);
        List<Notification> notifications = client.historyUpdate("user");

        if(notifications.isEmpty()) {
            Log.i(TAG, "Notification history is up to date");
            return;
        }

        // Store the received messages in the database
        ActiveAndroid.beginTransaction();
        try {
            for (Notification notification : notifications) {
                notification.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
