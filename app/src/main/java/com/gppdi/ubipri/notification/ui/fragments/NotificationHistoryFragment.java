package com.gppdi.ubipri.notification.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.api.ApiNotificationService;
import com.gppdi.ubipri.notification.data.dao.NotificationDAO;
import com.gppdi.ubipri.notification.data.models.Notification;
import com.gppdi.ubipri.notification.ui.activities.NotificationActivity;
import com.gppdi.ubipri.notification.ui.adapters.NotificationAdapter;
import com.gppdi.ubipri.ui.fragments.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationHistoryFragment extends BaseFragment {

    private static final String TAG = "NotificationsFragment";

    @Inject ApiNotificationService apiNotificationService;

    private ListView listView;
    private SwipeRefreshLayout swipeView;
    private TextView updateTextView;

    private NotificationDAO notificationDAO;
    private NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification_hist, container, false);
        listView = (ListView) rootView.findViewById(R.id.notificationsList);
        swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.notificationsSwipeUpdater);
        updateTextView = (TextView) rootView.findViewById(R.id.notificationsUpdateMessage);

        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notificationDAO = new NotificationDAO();
        notificationAdapter = new NotificationAdapter(this.getActivity(),
                R.layout.row_notification_hist, notificationDAO.newest());

        listView.setAdapter(notificationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Notification notification = (Notification) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.putExtras(notification.getBundle());
                startActivity(intent);
            }
        });

        swipeView.setColorSchemeColors(R.color.orange_logo);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateHistory();
            }
        });
        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });
        updateHistory();
    }

    @Override
    public void onResume() {
        notificationAdapter.update(notificationDAO.newest());
        super.onResume();
    }

    private void updateHistory() {
        updateTextView.setText(R.string.notification_update_in_progress);
        updateTextView.setVisibility(View.VISIBLE);

        // Get the latest notification stored in the local database
        Notification lastNotification = notificationDAO.newestSingle();
        if(lastNotification == null) {
            lastNotification = new Notification();
        }

        // Get the new messages from the webservice
        apiNotificationService.historyUpdate(lastNotification, new Callback<List<Notification>>() {
            @Override
            public void success(List<Notification> notifications, Response response) {
                if(notifications != null) {
                    notificationDAO.createOrUpdate(notifications);
                    notificationAdapter.update(notificationDAO.newest());
                }
                swipeView.setRefreshing(false);
                if(notificationAdapter.getCount() > 0) {
                    updateTextView.setVisibility(View.GONE);
                } else {
                    updateTextView.setText(R.string.notification_history_is_empty);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Unable to update the message history");
                Log.e(TAG, error.toString());
                swipeView.setRefreshing(false);
                updateTextView.setText(R.string.notification_update_error);
            }
        });
    }
}
