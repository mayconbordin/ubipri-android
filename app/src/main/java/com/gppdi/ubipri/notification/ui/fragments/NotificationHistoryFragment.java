package com.gppdi.ubipri.notification.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.api.ApiNotificationService;
import com.gppdi.ubipri.notification.data.dao.NotificationDAO;
import com.gppdi.ubipri.notification.data.models.Notification;
import com.gppdi.ubipri.notification.ui.adapters.NotificationAdapter;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationHistoryFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    @Inject ApiNotificationService apiNotificationService;

    private ListView listView;

    private List<Notification> notificationList;
    private NotificationDAO notificationDAO;
    private NotificationAdapter notificationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification_hist, container, false);
        listView = (ListView) rootView.findViewById(R.id.listNotifications);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Notification notification = (Notification) parent.getItemAtPosition(position);
                // TODO: 31/08/15 Display the full message
            }
        });

        notificationDAO = new NotificationDAO();
        notificationList = notificationDAO.newest();
        notificationAdapter = new NotificationAdapter(this.getActivity(), R.layout.row_notification_hist, notificationList);
        listView.setAdapter(notificationAdapter);

        updateHistory();

        return rootView;
    }

    private void updateHistory() {
        // Get the latest notification stored in the local database
        Notification lastNotification = notificationDAO.newestByIdSingle();

        // Get the new messages from the webservice
        apiNotificationService.historyUpdate(lastNotification, new Callback<List<Notification>>() {
            @Override
            public void success(List<Notification> notifications, Response response) {
                if(notifications != null) {
                    notificationDAO.createOrUpdate(notifications);
                    notificationList = notificationDAO.newest();
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error updating the message history");
            }
        });
    }
}
