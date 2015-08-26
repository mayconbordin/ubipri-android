package com.gppdi.ubipri.notification.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.Notification;
import com.gppdi.ubipri.notification.api.NotificationClient;
import com.gppdi.ubipri.notification.data.dao.NotificationDAO;
import com.gppdi.ubipri.notification.services.NotificationService;
import com.gppdi.ubipri.notification.ui.adapters.NotificationAdapter;

import java.util.List;

public class NotificationHistoryFragment extends Fragment {

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
