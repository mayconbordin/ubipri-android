package com.gppdi.ubipri.notification.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.data.models.Notification;
import com.gppdi.ubipri.notification.utils.NotificationUtil;

import java.text.SimpleDateFormat;

public class NotificationActivity extends AppCompatActivity {

    private static final String ARG_NOTIFICATION = "param_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            fillViews(new Notification(bundle));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
