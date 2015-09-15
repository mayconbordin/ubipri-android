package com.gppdi.ubipri.notification.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.data.models.Notification;
import com.gppdi.ubipri.notification.utils.NotificationUtil;

import java.text.SimpleDateFormat;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    private static final String ARG_NOTIFICATION = "param_notification";

    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            notification = new Notification(bundle);
            fillViews(notification);
            if(!notification.isDeleted()) {
                notification.setState(Notification.STATE_READ);
                notification.save();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.notificationActionDelete:
                String op = "deleted";
                if(!notification.isDeleted()) {
                    notification.setState(Notification.STATE_DELETED);
                } else {
                    notification.setState(Notification.STATE_READ);
                    op = "restored";
                }
                notification.save();
                Log.i(TAG, "Notification " + op);
                this.finish();
                // FIXME: 15/09/15 Message list should be updated after deletion
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillViews(Notification notification) {
        ImageView formatImageView = (ImageView) findViewById(R.id.notificationFormatImage);
        TextView titleTextView = (TextView) findViewById(R.id.notificationFormat);
        TextView timeTextView = (TextView) findViewById(R.id.notificationTime);
        TextView messageTextView = (TextView) findViewById(R.id.notificationMessage);

        formatImageView.setImageResource(NotificationUtil.getNotificationIcon(notification));
        titleTextView.setText(getString(NotificationUtil.getNotificationFormatStringRes(notification)));
        timeTextView.setText(SimpleDateFormat.getDateInstance().format(notification.getTimestamp()));
        messageTextView.setText(notification.getMessage());
    }
}
